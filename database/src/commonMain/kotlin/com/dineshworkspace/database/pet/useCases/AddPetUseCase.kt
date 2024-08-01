package com.dineshworkspace.database.pet.useCases

import com.dineshworkspace.database.pet.dataModels.Pet
import com.dineshworkspace.database.pet.repository.PetRepository

class AddPetUseCase(private val petRepository: PetRepository) {
    suspend operator fun invoke(pet: Pet) =
        petRepository.addPet(pet)
}