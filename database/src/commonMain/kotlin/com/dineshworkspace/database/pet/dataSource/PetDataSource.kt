package com.dineshworkspace.database.pet.dataSource

import com.dineshworkspace.common.Config
import com.dineshworkspace.database.pet.dataModels.Pet
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow


class PetDataSource(private val firestore: FirebaseFirestore, private val userId: String) {

    suspend fun addPet(pet: Pet) {
        firestore.collection(Config.BASE_ENV).document(Config.PETS_COLLECTION)
            .collection(userId)
            .document
            .set(data = pet, buildSettings = {
                encodeDefaults = true
            })
    }

    suspend fun getPets() = flow {
        val petsList = mutableListOf<Pet>()
        firestore.collection(Config.BASE_ENV).document(Config.PETS_COLLECTION)
            .collection(userId).snapshots.collect { querySnapshot ->
                petsList.clear()
                querySnapshot.documents.forEach { documentSnapshot ->
                    val pet = documentSnapshot.data<Pet>()
                    petsList.add(pet)
                }
                emit(petsList)
            }
    }
}