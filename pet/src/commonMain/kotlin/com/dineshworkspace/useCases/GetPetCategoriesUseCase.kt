package com.dineshworkspace.useCases

import com.dineshworkspace.repositories.PetRepository

class GetPetCategoriesUseCase(private val petRepository: PetRepository) {
    suspend operator fun invoke() = petRepository.getPetCategories()
}