package home.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dineshworkspace.database.pet.dataModels.Pet
import com.dineshworkspace.database.pet.dataModels.PetCategory
import com.dineshworkspace.database.pet.useCases.AddPetUseCase
import com.dineshworkspace.database.pet.useCases.GetPetCategoriesUseCase
import com.dineshworkspace.database.pet.useCases.GetPetsUseCase
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

    suspend fun addNewPet(pet: Pet) {
        viewModelScope.launch(Dispatchers.IO) {
            addPetUseCase.invoke(pet)
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

}

sealed interface GetPetsUiState {
    data class Success(val pets: List<Pet>) : GetPetsUiState
    data object Loading : GetPetsUiState
    data object Error : GetPetsUiState
}