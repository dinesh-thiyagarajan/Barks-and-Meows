package com.dineshworkspace.database.pet

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore

const val PET_COLLECTION = "Pets"

class PetDataSource(val firestore: FirebaseFirestore = Firebase.firestore ) {

    suspend fun addPet() {
        firestore.collection(PET_COLLECTION)
    }

}