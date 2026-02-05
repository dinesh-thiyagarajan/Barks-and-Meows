package com.app.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.dataModels.Pet
import com.app.dataModels.PetCategory
import com.app.useCases.AddPetUseCase
import com.app.useCases.GetPetCategoriesUseCase
import com.app.useCases.GetPetsUseCase
import com.app.useCases.UpdatePetUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PetViewModel(
    private val addPetUseCase: AddPetUseCase,
    private val updatePetUseCase: UpdatePetUseCase,
    private val getPetsUseCase: GetPetsUseCase,
    private val getPetCategoriesUseCase: GetPetCategoriesUseCase
) : ViewModel() {

    val getPetsUiState: StateFlow<GetPetsUiState> get() = _getPetsUiState
    private val _getPetsUiState: MutableStateFlow<GetPetsUiState> = MutableStateFlow(
        GetPetsUiState.Loading
    )

    val petCategories: StateFlow<List<PetCategory>> get() = _petCategories
    private val _petCategories: MutableStateFlow<List<PetCategory>> = MutableStateFlow(
        listOf()
    )

    val addPetUiState: StateFlow<AddPetUiState> get() = _addPetUiState
    private val _addPetUiState: MutableStateFlow<AddPetUiState> = MutableStateFlow(
        AddPetUiState.NotStarted
    )

    val updatePetUiState: StateFlow<UpdatePetUiState> get() = _updatePetUiState
    private val _updatePetUiState: MutableStateFlow<UpdatePetUiState> = MutableStateFlow(
        UpdatePetUiState.NotStarted
    )

    suspend fun addNewPet(pet: Pet) {
        _addPetUiState.value = AddPetUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                addPetUseCase.invoke(pet)
                _addPetUiState.value = AddPetUiState.Success
            } catch (e: Exception) {
                _addPetUiState.value = AddPetUiState.Error(e.message ?: "Failed to add pet. Check your permissions.")
            }
        }
    }

    suspend fun updatePet(pet: Pet) {
        _updatePetUiState.value = UpdatePetUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updatePetUseCase.invoke(pet)
                _updatePetUiState.value = UpdatePetUiState.Success
            } catch (e: Exception) {
                _updatePetUiState.value = UpdatePetUiState.Error(e.message ?: "Failed to update pet. Check your permissions.")
            }
        }
    }

    fun resetUpdatePetUiState() {
        _updatePetUiState.value = UpdatePetUiState.NotStarted
    }

    suspend fun getPets() {
        viewModelScope.launch(Dispatchers.IO) {
            getPetsUseCase.invoke()
                .flowOn(Dispatchers.IO)
                .collect {
                    _getPetsUiState.value = GetPetsUiState.Success(pets = it)
                }
        }
    }

    fun getPetCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            getPetCategoriesUseCase.invoke().flowOn(Dispatchers.IO)
                .collect {
                    _petCategories.value = it
                }
        }
    }

    fun updateSelectedCategory(selectedId: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            _petCategories.value = _petCategories.value.map { category ->
                category.copy(selected = category.id == selectedId)
            }
        }
    }
}

sealed interface GetPetsUiState {
    data class Success(val pets: List<Pet>) : GetPetsUiState
    data object Loading : GetPetsUiState
    data object Error : GetPetsUiState
}

sealed interface AddPetUiState {
    data object Success : AddPetUiState
    data object Loading : AddPetUiState
    data object NotStarted : AddPetUiState
    data class Error(val message: String) : AddPetUiState
}

sealed interface UpdatePetUiState {
    data object Success : UpdatePetUiState
    data object Loading : UpdatePetUiState
    data object NotStarted : UpdatePetUiState
    data class Error(val message: String) : UpdatePetUiState
}
