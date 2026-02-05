package com.app.reminder.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.dataModels.Pet
import com.app.reminder.dataModels.FeedingReminder
import com.app.reminder.useCases.AddReminderUseCase
import com.app.reminder.useCases.DeleteReminderUseCase
import com.app.reminder.useCases.GetReminderByIdUseCase
import com.app.reminder.useCases.GetRemindersUseCase
import com.app.reminder.useCases.UpdateLastFedTimeUseCase
import com.app.reminder.useCases.UpdateReminderUseCase
import com.app.useCases.GetPetsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class ReminderViewModel(
    private val getRemindersUseCase: GetRemindersUseCase,
    private val addReminderUseCase: AddReminderUseCase,
    private val updateReminderUseCase: UpdateReminderUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase,
    private val getReminderByIdUseCase: GetReminderByIdUseCase,
    private val updateLastFedTimeUseCase: UpdateLastFedTimeUseCase,
    private val getPetsUseCase: GetPetsUseCase
) : ViewModel() {

    val remindersUiState: StateFlow<RemindersUiState> get() = _remindersUiState
    private val _remindersUiState: MutableStateFlow<RemindersUiState> = MutableStateFlow(
        RemindersUiState.Loading
    )

    val petsUiState: StateFlow<PetsUiState> get() = _petsUiState
    private val _petsUiState: MutableStateFlow<PetsUiState> = MutableStateFlow(
        PetsUiState.Loading
    )

    val addReminderUiState: StateFlow<AddReminderUiState> get() = _addReminderUiState
    private val _addReminderUiState: MutableStateFlow<AddReminderUiState> = MutableStateFlow(
        AddReminderUiState.NotStarted
    )

    val selectedReminderUiState: StateFlow<SelectedReminderUiState> get() = _selectedReminderUiState
    private val _selectedReminderUiState: MutableStateFlow<SelectedReminderUiState> = MutableStateFlow(
        SelectedReminderUiState.Loading
    )

    fun getReminders() {
        viewModelScope.launch(Dispatchers.IO) {
            getRemindersUseCase.invoke()
                .flowOn(Dispatchers.IO)
                .collect { reminders ->
                    _remindersUiState.value = RemindersUiState.Success(reminders = reminders)
                }
        }
    }

    fun getPets() {
        viewModelScope.launch(Dispatchers.IO) {
            getPetsUseCase.invoke()
                .flowOn(Dispatchers.IO)
                .collect { pets ->
                    _petsUiState.value = PetsUiState.Success(pets = pets)
                }
        }
    }

    fun addReminder(pet: Pet, intervalMinutes: Int) {
        _addReminderUiState.value = AddReminderUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val reminder = FeedingReminder(
                    id = generateReminderId(),
                    petId = pet.id,
                    petName = pet.name,
                    intervalMinutes = intervalMinutes,
                    isEnabled = true,
                    lastFedTime = null, // Initially null - shows "Never"
                    createdAt = kotlin.time.Clock.System.now().toEpochMilliseconds()
                )
                addReminderUseCase.invoke(reminder)
                _addReminderUiState.value = AddReminderUiState.Success(reminder)
            } catch (e: Exception) {
                _addReminderUiState.value = AddReminderUiState.Error(
                    e.message ?: "Failed to add reminder"
                )
            }
        }
    }

    fun updateReminder(reminder: FeedingReminder) {
        // Optimistically update local state first for immediate UI feedback
        val currentState = _remindersUiState.value
        if (currentState is RemindersUiState.Success) {
            val updatedReminders = currentState.reminders.map {
                if (it.id == reminder.id) reminder else it
            }
            _remindersUiState.value = RemindersUiState.Success(updatedReminders)
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateReminderUseCase.invoke(reminder)
            } catch (ignored: Exception) {
                // Revert on error by refreshing from backend
                getReminders()
            }
        }
    }

    fun deleteReminder(reminderId: String) {
        // Optimistically update local state first for immediate UI feedback
        val currentState = _remindersUiState.value
        if (currentState is RemindersUiState.Success) {
            val updatedReminders = currentState.reminders.filter { it.id != reminderId }
            _remindersUiState.value = RemindersUiState.Success(updatedReminders)
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteReminderUseCase.invoke(reminderId)
                    .flowOn(Dispatchers.IO)
                    .collect { /* deleted */ }
            } catch (ignored: Exception) {
                // Revert on error by refreshing from backend
                getReminders()
            }
        }
    }

    fun getReminderById(reminderId: String) {
        _selectedReminderUiState.value = SelectedReminderUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            getReminderByIdUseCase.invoke(reminderId)
                .flowOn(Dispatchers.IO)
                .collect { reminder ->
                    if (reminder != null) {
                        _selectedReminderUiState.value = SelectedReminderUiState.Success(reminder)
                    } else {
                        _selectedReminderUiState.value = SelectedReminderUiState.Error("Reminder not found")
                    }
                }
        }
    }

    fun markAsFed(reminderId: String) {
        val currentTime = kotlin.time.Clock.System.now().toEpochMilliseconds()

        // Optimistically update local state first for immediate UI feedback
        val currentState = _remindersUiState.value
        if (currentState is RemindersUiState.Success) {
            val updatedReminders = currentState.reminders.map { reminder ->
                if (reminder.id == reminderId) {
                    reminder.copy(lastFedTime = currentTime)
                } else {
                    reminder
                }
            }
            _remindersUiState.value = RemindersUiState.Success(updatedReminders)
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateLastFedTimeUseCase.invoke(reminderId, currentTime)
            } catch (ignored: Exception) {
                // Revert on error by refreshing from backend
                getReminders()
            }
        }
    }

    fun resetAddReminderState() {
        _addReminderUiState.value = AddReminderUiState.NotStarted
    }

    private fun generateReminderId(): String {
        return "reminder_${kotlin.time.Clock.System.now().toEpochMilliseconds()}"
    }
}

sealed interface RemindersUiState {
    data class Success(val reminders: List<FeedingReminder>) : RemindersUiState
    data object Loading : RemindersUiState
    data object Error : RemindersUiState
}

sealed interface PetsUiState {
    data class Success(val pets: List<Pet>) : PetsUiState
    data object Loading : PetsUiState
    data object Error : PetsUiState
}

sealed interface AddReminderUiState {
    data class Success(val reminder: FeedingReminder) : AddReminderUiState
    data object Loading : AddReminderUiState
    data object NotStarted : AddReminderUiState
    data class Error(val message: String) : AddReminderUiState
}

sealed interface SelectedReminderUiState {
    data class Success(val reminder: FeedingReminder) : SelectedReminderUiState
    data object Loading : SelectedReminderUiState
    data class Error(val message: String) : SelectedReminderUiState
}
