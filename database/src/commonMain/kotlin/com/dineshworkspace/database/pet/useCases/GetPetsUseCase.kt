package com.dineshworkspace.database.pet.useCases

import com.dineshworkspace.database.pet.repository.PetRepository

class GetPetsUseCase(private val petRepository: PetRepository) {
    suspend operator fun invoke() = petRepository.getPets()
}