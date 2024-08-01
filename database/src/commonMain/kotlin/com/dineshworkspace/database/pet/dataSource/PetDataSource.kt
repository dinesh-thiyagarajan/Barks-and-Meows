package com.dineshworkspace.database.pet.dataSource

import com.dineshworkspace.database.pet.dataModels.Pet
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow

const val PET_COLLECTION = "Pets"
const val BASE_ENV = "Test"

class PetDataSource(private val firestore: FirebaseFirestore) {

    suspend fun addPet(pet: Pet) {
        firestore.collection(BASE_ENV).document.collection(PET_COLLECTION)
            .add(data = pet, buildSettings = {
                encodeDefaults = true
            })
    }

    suspend fun getPets() = flow {
        firestore.collection(BASE_ENV).document.collection(PET_COLLECTION).snapshots.collect { querySnapshot ->
            val pets = querySnapshot.documents.map { documentSnapshot ->
                documentSnapshot.data<Pet>()
            }
            emit(pets)
        }

    }

}