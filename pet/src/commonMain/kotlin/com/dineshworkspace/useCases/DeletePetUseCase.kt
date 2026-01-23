package com.dineshworkspace.useCases

import com.dineshworkspace.repositories.PetRepository

class DeletePetUseCase(private val petRepository: PetRepository) {
    suspend operator fun invoke(petId: String) =
        petRepository.deletePet(petId = petId)
}
