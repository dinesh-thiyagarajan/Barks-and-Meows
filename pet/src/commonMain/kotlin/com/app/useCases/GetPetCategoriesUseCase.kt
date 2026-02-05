package com.app.useCases

import com.app.repositories.PetRepository

class GetPetCategoriesUseCase(private val petRepository: PetRepository) {
    suspend operator fun invoke() = petRepository.getPetCategories()
}
