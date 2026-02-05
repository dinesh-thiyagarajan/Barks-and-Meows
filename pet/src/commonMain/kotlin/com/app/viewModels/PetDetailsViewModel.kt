package com.app.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.dataModels.Pet
import com.app.useCases.DeletePetUseCase
import com.app.useCases.GetPetDetailsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PetDetailsViewModel(
    private val getPetDetailsUseCase: GetPetDetailsUseCase,
    private val deletePetUseCase: DeletePetUseCase,
) : ViewModel() {

    val petDetailsUiState: StateFlow<GetPetDetailsUiState> get() = _petDetailsUiState
    private val _petDetailsUiState: MutableStateFlow<GetPetDetailsUiState> = MutableStateFlow(
        GetPetDetailsUiState.Loading
    )

    suspend fun getPetDetails(petId: String) {
        getPetDetailsUseCase.invoke(petId = petId).catch {
            _petDetailsUiState.value = GetPetDetailsUiState.Error(it.message)
        }.collect {
            _petDetailsUiState.value = GetPetDetailsUiState.Success(pet = it)
        }
    }

    suspend fun deletePet(petId: String, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePetUseCase.invoke(petId = petId)
                .catch {
                    // Handle error if needed
                }
                .collect {
                    withContext(Dispatchers.Main) {
                        onSuccess()
                    }
                }
        }
    }
}

sealed interface GetPetDetailsUiState {
    data class Success(val pet: Pet) : GetPetDetailsUiState
    data object Loading : GetPetDetailsUiState
    data class Error(val errorMessage: String?) : GetPetDetailsUiState
}
