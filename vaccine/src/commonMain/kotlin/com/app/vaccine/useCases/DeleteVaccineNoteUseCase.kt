package com.app.vaccine.useCases

import com.app.vaccine.repository.VaccineRepository

class DeleteVaccineNoteUseCase(private val vaccineRepository: VaccineRepository) {
    suspend operator fun invoke(petId: String, vaccineNoteId: String) =
        vaccineRepository.deleteVaccineNote(petId = petId, vaccineNoteId = vaccineNoteId)
}
