package com.dineshworkspace.database.pet.useCases

import com.dineshworkspace.database.pet.repository.PetRepository

class GetPetCategoriesUseCase(private val petRepository: PetRepository) {
    suspend operator fun invoke() = petRepository.getPetCategories()
}