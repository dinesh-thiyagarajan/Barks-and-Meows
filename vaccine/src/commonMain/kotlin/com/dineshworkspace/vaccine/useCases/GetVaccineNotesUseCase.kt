package com.dineshworkspace.vaccine.useCases

import com.dineshworkspace.vaccine.repository.VaccineRepository

class GetVaccineNotesUseCase(private val vaccineRepository: VaccineRepository) {
    suspend operator fun invoke(petId: String) =
        vaccineRepository.getVaccineNotesForPet(petId = petId)
}