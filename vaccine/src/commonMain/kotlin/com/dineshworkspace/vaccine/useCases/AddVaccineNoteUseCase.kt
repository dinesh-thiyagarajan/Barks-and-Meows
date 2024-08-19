package com.dineshworkspace.vaccine.useCases

import com.dineshworkspace.vaccine.dataModels.VaccineNote
import com.dineshworkspace.vaccine.repository.VaccineRepository

class AddVaccineNoteUseCase(private val vaccineRepository: VaccineRepository) {
    suspend operator fun invoke(petId: String, vaccineNote: VaccineNote) =
        vaccineRepository.addVaccineNoteForPet(petId = petId, vaccineNote = vaccineNote)
}