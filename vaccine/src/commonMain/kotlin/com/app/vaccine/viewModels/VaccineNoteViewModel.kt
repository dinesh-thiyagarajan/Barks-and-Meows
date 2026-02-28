package com.app.vaccine.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.vaccine.dataModels.Vaccine
import com.app.vaccine.dataModels.VaccineNote
import com.app.vaccine.scheduler.VaccineReminderSchedulerProvider
import com.app.vaccine.useCases.AddVaccineNoteUseCase
import com.app.vaccine.useCases.DeleteVaccineNoteUseCase
import com.app.vaccine.useCases.GetVaccineNotesUseCase
import com.app.vaccine.useCases.GetVaccineRemindersUseCase
import com.app.vaccine.useCases.GetVaccinesUseCase
import com.app.vaccine.useCases.UpdateVaccineNoteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class VaccineNoteViewModel(
    private val addVaccineNoteUseCase: AddVaccineNoteUseCase,
    private val getVaccineNotesUseCase: GetVaccineNotesUseCase,
    private val getVaccinesUseCase: GetVaccinesUseCase,
    private val deleteVaccineNoteUseCase: DeleteVaccineNoteUseCase,
    private val vaccineReminderSchedulerProvider: VaccineReminderSchedulerProvider,
    private val getVaccineRemindersUseCase: GetVaccineRemindersUseCase,
    private val updateVaccineNoteUseCase: UpdateVaccineNoteUseCase,
) : ViewModel() {

    val addVaccineNoteUiState: StateFlow<AddVaccineNoteUiState> get() = _addVaccineNoteUiState
    private val _addVaccineNoteUiState: MutableStateFlow<AddVaccineNoteUiState> =
        MutableStateFlow(AddVaccineNoteUiState.FetchingVaccines)

    val getVaccineNoteUiState: StateFlow<GetVaccineNotesUiState> get() = _getVaccineNoteUiState
    private val _getVaccineNoteUiState: MutableStateFlow<GetVaccineNotesUiState> =
        MutableStateFlow(GetVaccineNotesUiState.Loading)

    val vaccineRemindersUiState: StateFlow<VaccineRemindersUiState> get() = _vaccineRemindersUiState
    private val _vaccineRemindersUiState: MutableStateFlow<VaccineRemindersUiState> =
        MutableStateFlow(VaccineRemindersUiState.Loading)

    fun addVaccineNote(vaccineNote: VaccineNote) {
        _addVaccineNoteUiState.value = AddVaccineNoteUiState.AddingVaccineNote
        viewModelScope.launch(Dispatchers.IO) {
            addVaccineNoteUseCase.invoke(vaccineNote = vaccineNote)
                .catch {
                    _addVaccineNoteUiState.value = AddVaccineNoteUiState.Error(it.message)
                }
                .collect { docId ->
                    vaccineNote.reminderTimestamp?.let { reminderTs ->
                        vaccineReminderSchedulerProvider.scheduleVaccineReminder(
                            vaccineNoteId = docId,
                            petName = vaccineNote.petName ?: "",
                            vaccineName = vaccineNote.vaccine.vaccineName,
                            reminderTimestamp = reminderTs
                        )
                    }
                    _addVaccineNoteUiState.value =
                        AddVaccineNoteUiState.VaccineNotesAddedSuccessfully
                }
        }
    }

    fun getVaccineNotes(petId: String) {
        _getVaccineNoteUiState.value = GetVaccineNotesUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            getVaccineNotesUseCase.invoke(petId = petId)
                .flowOn(Dispatchers.IO)
                .catch {
                    _getVaccineNoteUiState.value = GetVaccineNotesUiState.Error(it.message)
                }
                .collect {
                    _getVaccineNoteUiState.value =
                        GetVaccineNotesUiState.Success(it)
                }
        }
    }

    fun getVaccinesList() {
        _addVaccineNoteUiState.value = AddVaccineNoteUiState.FetchingVaccines
        viewModelScope.launch(Dispatchers.IO) {
            getVaccinesUseCase.invoke().collect {
                _addVaccineNoteUiState.value = AddVaccineNoteUiState.VaccinesFetchedSuccessfully(it)
            }
        }
    }

    fun deleteVaccineNote(petId: String, vaccineNoteId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            vaccineReminderSchedulerProvider.cancelVaccineReminder(vaccineNoteId)
            deleteVaccineNoteUseCase.invoke(petId = petId, vaccineNoteId = vaccineNoteId)
                .catch {
                    // Handle error if needed
                }
                .collect {
                    // Successfully deleted, the getVaccineNotes flow will automatically update
                }
        }
    }

    fun getVaccineReminders(petIds: List<String>) {
        _vaccineRemindersUiState.value = VaccineRemindersUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            getVaccineRemindersUseCase.invoke(petIds = petIds)
                .catch {
                    _vaccineRemindersUiState.value = VaccineRemindersUiState.Error(it.message)
                }
                .collect {
                    _vaccineRemindersUiState.value = VaccineRemindersUiState.Success(it)
                }
        }
    }

    fun removeVaccineReminder(vaccineNote: VaccineNote) {
        viewModelScope.launch(Dispatchers.IO) {
            vaccineReminderSchedulerProvider.cancelVaccineReminder(vaccineNote.id)
            val updatedNote = vaccineNote.copy(reminderTimestamp = null)
            updateVaccineNoteUseCase.invoke(vaccineNote = updatedNote)
                .catch {
                    // Handle error if needed
                }
                .collect {
                    // Successfully updated, the flow will automatically refresh
                }
        }
    }
}

sealed interface AddVaccineNoteUiState {
    data object FetchingVaccines : AddVaccineNoteUiState
    data class VaccinesFetchedSuccessfully(val vaccineList: List<Vaccine>) : AddVaccineNoteUiState
    data object AddingVaccineNote : AddVaccineNoteUiState
    data object VaccineNotesAddedSuccessfully : AddVaccineNoteUiState
    data class Error(val errorMessage: String?) : AddVaccineNoteUiState
}

sealed interface GetVaccineNotesUiState {
    data object Loading : GetVaccineNotesUiState
    data class Success(val vaccineNotesList: List<VaccineNote>) :
        GetVaccineNotesUiState

    data class Error(val errorMessage: String?) : GetVaccineNotesUiState
}

sealed interface VaccineRemindersUiState {
    data object Loading : VaccineRemindersUiState
    data class Success(val vaccineNotesList: List<VaccineNote>) : VaccineRemindersUiState
    data class Error(val errorMessage: String?) : VaccineRemindersUiState
}
