package com.app.vaccine.useCases

import com.app.vaccine.dataModels.VaccineNote
import com.app.vaccine.repository.VaccineRepository

class AddVaccineNoteUseCase(private val vaccineRepository: VaccineRepository) {
    suspend operator fun invoke(vaccineNote: VaccineNote) =
        vaccineRepository.addVaccineNoteForPet(vaccineNote = vaccineNote)
}