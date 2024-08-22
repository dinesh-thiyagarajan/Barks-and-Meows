package com.dineshworkspace.vaccine.useCases

import com.dineshworkspace.vaccine.dataModels.VaccineNote
import com.dineshworkspace.vaccine.repository.VaccineRepository

class AddVaccineNoteUseCase(private val vaccineRepository: VaccineRepository) {
    suspend operator fun invoke(vaccineNote: VaccineNote) =
        vaccineRepository.addVaccineNoteForPet(vaccineNote = vaccineNote)
}