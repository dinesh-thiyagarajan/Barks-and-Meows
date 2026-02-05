package com.app.useCases

import com.app.dataModels.Pet
import com.app.repositories.PetRepository

class AddPetUseCase(private val petRepository: PetRepository) {
    suspend operator fun invoke(pet: Pet) =
        petRepository.addPet(pet)
}
