package com.dineshworkspace.database.pet

interface PetRepository {
    suspend fun addPet(pet: Pet)
}