package home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dineshworkspace.database.pet.useCases.AddPetUseCase
import com.dineshworkspace.database.pet.useCases.GetPetsUseCase
import com.dineshworkspace.database.pet.dataModels.Pet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val addPetUseCase: AddPetUseCase, private val getPetsUseCase: GetPetsUseCase) : ViewModel() {

    val getPetsUiState: StateFlow<GetPetsUiState> get() = _getPetsUiState
    private val _getPetsUiState: MutableStateFlow<GetPetsUiState> = MutableStateFlow(
        GetPetsUiState.Loading
    )

    suspend fun addNewPet(pet: Pet) {
        viewModelScope.launch(Dispatchers.IO) {
            addPetUseCase.invoke(pet)
        }
    }

    suspend fun getPets() {
        viewModelScope.launch(Dispatchers.IO) {
            val pets = getPetsUseCase.invoke()
        }
    }

}

sealed interface GetPetsUiState {
    data object Success : GetPetsUiState
    data object Loading : GetPetsUiState
    data object Error : GetPetsUiState
}