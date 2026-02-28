package com.app.vaccine.useCases

import com.app.vaccine.dataModels.VaccineNote
import com.app.vaccine.repository.VaccineRepository

class UpdateVaccineNoteUseCase(private val vaccineRepository: VaccineRepository) {
    suspend operator fun invoke(vaccineNote: VaccineNote) =
        vaccineRepository.updateVaccineNote(vaccineNote = vaccineNote)
}
