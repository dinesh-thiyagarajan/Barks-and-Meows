package com.dineshworkspace.database.pet.useCases

import com.dineshworkspace.database.pet.repository.PetRepository

class GetPetDetailsUseCase(private val petRepository: PetRepository) {
    suspend operator fun invoke(petId: String) = petRepository.getPetDetails(petId = petId)
}