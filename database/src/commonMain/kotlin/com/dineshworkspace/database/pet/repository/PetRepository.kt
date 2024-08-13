package com.dineshworkspace.database.pet.repository

import com.dineshworkspace.database.pet.dataModels.Pet
import com.dineshworkspace.database.pet.dataModels.PetCategory
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    suspend fun addPet(pet: Pet)
    suspend fun getPets(): Flow<List<Pet>>
    suspend fun getPetCategories(): Flow<List<PetCategory>>
    suspend fun getPetDetails(petId: String): Flow<Pet>
}