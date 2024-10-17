package com.dineshworkspace.vaccine.dataSource

import com.dineshworkspace.vaccine.dataModels.Vaccine
import com.dineshworkspace.vaccine.dataModels.VaccineNote
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow

class VaccineDataSource(
    private val firestore: FirebaseFirestore,
    private val userId: String,
    private val petCollection: String,
    private val baseEnv: String,
    private val vaccineNotesCollection: String
) {

    suspend fun getVaccineNotesForPet(petId: String) = flow {
        val vaccineNotes = mutableListOf<VaccineNote>()
        firestore.collection(baseEnv).document(petCollection).collection(userId).document(petId)
            .collection(vaccineNotesCollection).snapshots.collect { querySnapshot ->
                vaccineNotes.clear()
                querySnapshot.documents.forEach { documentSnapshot ->
                    val pet = documentSnapshot.data<VaccineNote>()
                    vaccineNotes.add(pet)
                }
                emit(vaccineNotes.toList())
            }
    }

    suspend fun addVaccineNoteForPet(vaccineNote: VaccineNote) = flow {
        val docId =
            firestore.collection(baseEnv).document(petCollection).collection(userId)
                .document(vaccineNote.petId)
                .collection(vaccineNotesCollection).add(vaccineNote, buildSettings = {
                    encodeDefaults = true
                }).id
        emit(docId)
    }

    suspend fun getVaccines() = flow {
        val vaccineList: MutableList<Vaccine> = mutableListOf()
        vaccineList.add(Vaccine(vaccineId = "gene", "Vaccine 1"))
        vaccineList.add(Vaccine(vaccineId = "gene1", "Vaccine 2"))
        vaccineList.add(Vaccine(vaccineId = "gene2", "Vaccine 3"))
        emit(vaccineList)
    }


}