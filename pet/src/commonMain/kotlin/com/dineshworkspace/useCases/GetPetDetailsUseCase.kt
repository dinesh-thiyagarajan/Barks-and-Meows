package com.dineshworkspace.useCases

import com.dineshworkspace.repositories.PetRepository

class GetPetDetailsUseCase(private val petRepository: PetRepository) {
    suspend operator fun invoke(petId: String) = petRepository.getPetDetails(petId = petId)
}