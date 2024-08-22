package com.dineshworkspace.vaccine.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dineshworkspace.vaccine.dataModels.VaccineNote
import com.dineshworkspace.vaccine.useCases.AddVaccineNoteUseCase
import com.dineshworkspace.vaccine.useCases.GetVaccineNoteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class VaccineNoteViewModel(
    private val addVaccineNoteUseCase: AddVaccineNoteUseCase,
    private val getVaccineNoteUseCase: GetVaccineNoteUseCase
) : ViewModel() {

    val addVaccineNoteUiState: StateFlow<AddVaccineNoteUiState> get() = _addVaccineNoteUiState
    private val _addVaccineNoteUiState: MutableStateFlow<AddVaccineNoteUiState> =
        MutableStateFlow(AddVaccineNoteUiState.NotStarted)

    val getVaccineNotesUiState: StateFlow<GetVaccineNotesUiState> get() = _getVaccineNotesUiState
    private val _getVaccineNotesUiState: MutableStateFlow<GetVaccineNotesUiState> =
        MutableStateFlow(GetVaccineNotesUiState.Loading)


    suspend fun addVaccineNote(vaccineNote: VaccineNote) {
        _addVaccineNoteUiState.value = AddVaccineNoteUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            addVaccineNoteUseCase.invoke(vaccineNote = vaccineNote)
                .catch {
                    _addVaccineNoteUiState.value = AddVaccineNoteUiState.Error(it.message)
                }
                .collect {
                    _addVaccineNoteUiState.value = AddVaccineNoteUiState.Success
                }
        }
    }


    suspend fun getVaccineNotes(petId: String) {
        _getVaccineNotesUiState.value = GetVaccineNotesUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            getVaccineNoteUseCase.invoke(petId = petId)
                .flowOn(Dispatchers.IO)
                .catch {
                    _getVaccineNotesUiState.value = GetVaccineNotesUiState.Error(it.message)
                }
                .collect {
                    _getVaccineNotesUiState.value = GetVaccineNotesUiState.Success(it)
                }
        }
    }

}

sealed interface AddVaccineNoteUiState {
    data object Success : AddVaccineNoteUiState
    data object Loading : AddVaccineNoteUiState
    data object NotStarted : AddVaccineNoteUiState
    data class Error(val errorMessage: String?) : AddVaccineNoteUiState
}

sealed interface GetVaccineNotesUiState {
    data class Success(val vaccineNotesList: List<VaccineNote>) : GetVaccineNotesUiState
    data object Loading : GetVaccineNotesUiState
    data class Error(val errorMessage: String?) : GetVaccineNotesUiState
}