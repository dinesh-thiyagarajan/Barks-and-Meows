package com.dineshworkspace.repositories

import com.dineshworkspace.dataModels.Pet
import com.dineshworkspace.dataModels.PetCategory
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    suspend fun addPet(pet: Pet)
    suspend fun updatePet(pet: Pet)
    suspend fun getPets(): Flow<List<Pet>>
    suspend fun getPetCategories(): Flow<List<PetCategory>>
    suspend fun getPetDetails(petId: String): Flow<Pet>
    suspend fun deletePet(petId: String): Flow<Boolean>
}