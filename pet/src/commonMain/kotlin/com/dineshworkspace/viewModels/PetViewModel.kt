package com.dineshworkspace.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dineshworkspace.dataModels.Pet
import com.dineshworkspace.dataModels.PetCategory
import com.dineshworkspace.useCases.AddPetUseCase
import com.dineshworkspace.useCases.GetPetCategoriesUseCase
import com.dineshworkspace.useCases.GetPetsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PetViewModel(
    private val addPetUseCase: AddPetUseCase,
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

    suspend fun addNewPet(pet: Pet) {
        _addPetUiState.value = AddPetUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            addPetUseCase.invoke(pet)
            _addPetUiState.value = AddPetUiState.Success
        }
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

    suspend fun getPetCategories() {
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
}