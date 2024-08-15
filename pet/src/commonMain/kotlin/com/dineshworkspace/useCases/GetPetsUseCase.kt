package com.dineshworkspace.useCases

import com.dineshworkspace.repositories.PetRepository

class GetPetsUseCase(private val petRepository: PetRepository) {
    suspend operator fun invoke() = petRepository.getPets()
}