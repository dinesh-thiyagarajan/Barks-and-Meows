package com.dineshworkspace.database.pet.repository

import com.dineshworkspace.database.pet.dataModels.Pet
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    suspend fun addPet(pet: Pet)
    suspend fun getPets(): Flow<List<Pet>>
}