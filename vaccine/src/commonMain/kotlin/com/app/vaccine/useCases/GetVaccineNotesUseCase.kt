package com.app.vaccine.useCases

import com.app.vaccine.repository.VaccineRepository

class GetVaccineNotesUseCase(private val vaccineRepository: VaccineRepository) {
    suspend operator fun invoke(petId: String) =
        vaccineRepository.getVaccineNotesForPet(petId = petId)
}