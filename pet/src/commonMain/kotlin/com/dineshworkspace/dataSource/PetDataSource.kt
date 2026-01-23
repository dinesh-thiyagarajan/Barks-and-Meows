package com.dineshworkspace.dataSource

import barksandmeows.pet.generated.resources.Res
import barksandmeows.pet.generated.resources.ic_cat
import barksandmeows.pet.generated.resources.ic_dog
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

    suspend fun updatePet(pet: Pet) =
        firestore.collection(baseEnv).document(petCollection)
            .collection(userId)
            .document(pet.id)
            .set(data = pet, buildSettings = {
                encodeDefaults = true
            })

    suspend fun getPets() = flow {
        try {
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
        } catch (e: Exception) {
            // Handle Firestore permission errors gracefully (e.g., after logout)
            emit(emptyList())
        }
    }

    suspend fun getPetDetails(petId: String) = flow {
        try {
            firestore.collection(baseEnv).document(petCollection).collection(userId)
                .document(petId)
                .snapshots.collect { documentSnapshot ->
                    val pet = documentSnapshot.data<Pet>()
                    emit(pet)
                }
        } catch (e: Exception) {
            // Handle Firestore permission errors gracefully (e.g., after logout)
            // Flow will complete without emitting
        }
    }

    suspend fun deletePet(petId: String) = flow {
        firestore.collection(baseEnv).document(petCollection)
            .collection(userId)
            .document(petId)
            .delete()
        emit(true)
    }

    suspend fun getPetCategories() = flow {
        val petCategories = listOf(
            PetCategory(1, "Dog", drawableResource = Res.drawable.ic_dog),
            PetCategory(2, "Cat", drawableResource = Res.drawable.ic_cat)
        )
        emit(petCategories)
    }
}