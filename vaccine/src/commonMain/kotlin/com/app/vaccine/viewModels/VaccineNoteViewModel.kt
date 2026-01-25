package com.app.vaccine.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.vaccine.dataModels.Vaccine
import com.app.vaccine.dataModels.VaccineNote
import com.app.vaccine.useCases.AddVaccineNoteUseCase
import com.app.vaccine.useCases.DeleteVaccineNoteUseCase
import com.app.vaccine.useCases.GetVaccineNotesUseCase
import com.app.vaccine.useCases.GetVaccinesUseCase
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
) : ViewModel() {

    val addVaccineNoteUiState: StateFlow<AddVaccineNoteUiState> get() = _addVaccineNoteUiState
    private val _addVaccineNoteUiState: MutableStateFlow<AddVaccineNoteUiState> =
        MutableStateFlow(AddVaccineNoteUiState.FetchingVaccines)

    val getVaccineNoteUiState: StateFlow<GetVaccineNotesUiState> get() = _getVaccineNoteUiState
    private val _getVaccineNoteUiState: MutableStateFlow<GetVaccineNotesUiState> =
        MutableStateFlow(GetVaccineNotesUiState.Loading)


    fun addVaccineNote(vaccineNote: VaccineNote) {
        _addVaccineNoteUiState.value = AddVaccineNoteUiState.AddingVaccineNote
        viewModelScope.launch(Dispatchers.IO) {
            addVaccineNoteUseCase.invoke(vaccineNote = vaccineNote)
                .catch {
                    _addVaccineNoteUiState.value = AddVaccineNoteUiState.Error(it.message)
                }
                .collect {
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
            deleteVaccineNoteUseCase.invoke(petId = petId, vaccineNoteId = vaccineNoteId)
                .catch {
                    // Handle error if needed
                }
                .collect {
                    // Successfully deleted, the getVaccineNotes flow will automatically update
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

