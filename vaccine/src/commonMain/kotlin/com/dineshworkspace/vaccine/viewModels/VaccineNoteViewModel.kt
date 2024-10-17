package com.dineshworkspace.vaccine.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dineshworkspace.vaccine.dataModels.Vaccine
import com.dineshworkspace.vaccine.dataModels.VaccineNote
import com.dineshworkspace.vaccine.useCases.AddVaccineNoteUseCase
import com.dineshworkspace.vaccine.useCases.GetVaccineNotesUseCase
import com.dineshworkspace.vaccine.useCases.GetVaccinesUseCase
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
) : ViewModel() {

    val vaccineNoteUiState: StateFlow<VaccineNoteUiState> get() = _vaccineNoteUiState
    private val _vaccineNoteUiState: MutableStateFlow<VaccineNoteUiState> =
        MutableStateFlow(VaccineNoteUiState.Default)


    suspend fun addVaccineNote(vaccineNote: VaccineNote) {
        viewModelScope.launch(Dispatchers.IO) {
            addVaccineNoteUseCase.invoke(vaccineNote = vaccineNote)
                .catch {
                    _vaccineNoteUiState.value = VaccineNoteUiState.Error(it.message)
                }
                .collect {
                    _vaccineNoteUiState.value = VaccineNoteUiState.VaccineNotesAddedSuccessfully
                }
        }
    }


    suspend fun getVaccineNotes(petId: String) {
        _vaccineNoteUiState.value = VaccineNoteUiState.FetchingVaccines
        viewModelScope.launch(Dispatchers.IO) {
            getVaccineNotesUseCase.invoke(petId = petId)
                .flowOn(Dispatchers.IO)
                .catch {
                    _vaccineNoteUiState.value = VaccineNoteUiState.Error(it.message)
                }
                .collect {
                    _vaccineNoteUiState.value =
                        VaccineNoteUiState.VaccineNotesFetchedSuccessfully(it)
                }
        }
    }

    suspend fun getVaccinesList() {
        viewModelScope.launch(Dispatchers.IO) {
            getVaccinesUseCase.invoke().collect {
                _vaccineNoteUiState.value = VaccineNoteUiState.VaccinesFetchedSuccessfully(it)
            }
        }
    }

}

sealed interface VaccineNoteUiState {
    data object Default : VaccineNoteUiState
    data object FetchingVaccines : VaccineNoteUiState
    data class VaccinesFetchedSuccessfully(val vaccineList: List<Vaccine>) : VaccineNoteUiState
    data class VaccineNotesFetchedSuccessfully(val vaccineNotesList: List<VaccineNote>) :
        VaccineNoteUiState

    data object AddingVaccineNote : VaccineNoteUiState
    data object VaccineNotesAddedSuccessfully : VaccineNoteUiState
    data class Error(val errorMessage: String?) : VaccineNoteUiState
}