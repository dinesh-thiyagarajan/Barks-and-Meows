package com.app.useCases

import com.app.repositories.PetRepository

class GetPetDetailsUseCase(private val petRepository: PetRepository) {
    suspend operator fun invoke(petId: String) = petRepository.getPetDetails(petId = petId)
}
