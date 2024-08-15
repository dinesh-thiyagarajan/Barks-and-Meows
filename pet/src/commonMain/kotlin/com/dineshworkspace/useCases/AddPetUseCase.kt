package com.dineshworkspace.useCases

import com.dineshworkspace.dataModels.Pet
import com.dineshworkspace.repositories.PetRepository

class AddPetUseCase(private val petRepository: PetRepository) {
    suspend operator fun invoke(pet: Pet) =
        petRepository.addPet(pet)
}