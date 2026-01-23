package com.app.useCases

import com.app.repositories.PetRepository

class GetPetsUseCase(private val petRepository: PetRepository) {
    suspend operator fun invoke() = petRepository.getPets()
}