package com.app.useCases

import com.app.repositories.PetRepository

class DeletePetUseCase(private val petRepository: PetRepository) {
    suspend operator fun invoke(petId: String) =
        petRepository.deletePet(petId = petId)
}
