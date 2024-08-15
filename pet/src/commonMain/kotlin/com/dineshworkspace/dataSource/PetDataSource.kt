package com.dineshworkspace.dataSource

import com.dineshworkspace.dataModels.Pet
import com.dineshworkspace.dataModels.PetCategory
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow


class PetDataSource(
    private val firestore: FirebaseFirestore,
    private val userId: String,
    private val baseEnv: String,
    private val petCollection: String
) {

    suspend fun addPet(pet: Pet) =
        firestore.collection(baseEnv).document(petCollection)
            .collection(userId)
            .document(pet.id)
            .set(data = pet, buildSettings = {
                encodeDefaults = true
            })


    suspend fun getPets() = flow {
        val petsList = mutableListOf<Pet>()
        firestore.collection(baseEnv).document(petCollection)
            .collection(userId)
            .snapshots.collect { querySnapshot ->
                petsList.clear()
                querySnapshot.documents.forEach { documentSnapshot ->
                    val pet = documentSnapshot.data<Pet>()
                    petsList.add(pet)
                }
                emit(petsList)
            }
    }

    suspend fun getPetDetails(petId: String) = flow {
        firestore.collection(baseEnv).document(petCollection).collection(userId)
            .document(petId)
            .snapshots.collect { documentSnapshot ->
                val pet = documentSnapshot.data<Pet>()
                emit(pet)
            }
    }

    suspend fun getPetCategories() = flow {
        val petCategories = listOf(
            PetCategory(1, "Dog"),
            PetCategory(2, "Cat"),
            PetCategory(3, "Fish"),
            PetCategory(4, "Bird"),
            PetCategory(5, "Parrot"),
        )
        emit(petCategories)
    }
}