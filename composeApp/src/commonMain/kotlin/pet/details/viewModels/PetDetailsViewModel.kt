package pet.details.viewModels

import androidx.lifecycle.ViewModel
import com.dineshworkspace.database.pet.dataModels.Pet
import com.dineshworkspace.database.pet.useCases.GetPetDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch

class PetDetailsViewModel(private val getPetDetailsUseCase: GetPetDetailsUseCase) : ViewModel() {

    val petDetailsUiState: StateFlow<GetPetDetailsUiState> get() = _petDetailsUiState
    private val _petDetailsUiState: MutableStateFlow<GetPetDetailsUiState> = MutableStateFlow(
        GetPetDetailsUiState.Loading
    )

    suspend fun getPetDetails(petId: String) {
        getPetDetailsUseCase.invoke(petId = petId).catch {
            _petDetailsUiState.value = GetPetDetailsUiState.Error
        }.collect {
            _petDetailsUiState.value = GetPetDetailsUiState.Success(pet = it)
        }
    }

}

sealed interface GetPetDetailsUiState {
    data class Success(val pet: Pet) : GetPetDetailsUiState
    data object Loading : GetPetDetailsUiState
    data object Error : GetPetDetailsUiState
}