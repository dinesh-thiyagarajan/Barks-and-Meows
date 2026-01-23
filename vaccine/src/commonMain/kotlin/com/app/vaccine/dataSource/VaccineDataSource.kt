package com.app.vaccine.dataSource

import com.app.vaccine.dataModels.Vaccine
import com.app.vaccine.dataModels.VaccineNote
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
                    val vaccineNote = documentSnapshot.data<VaccineNote>()
                    // Use Firestore document ID instead of the id field in the data
                    val vaccineNoteWithFirestoreId = vaccineNote.copy(id = documentSnapshot.id)
                    vaccineNotes.add(vaccineNoteWithFirestoreId)
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

    suspend fun deleteVaccineNote(petId: String, vaccineNoteId: String) = flow {
        firestore.collection(baseEnv).document(petCollection).collection(userId)
            .document(petId)
            .collection(vaccineNotesCollection)
            .document(vaccineNoteId)
            .delete()
        emit(true)
    }

    suspend fun getVaccines() = flow {
        val vaccineList: MutableList<Vaccine> = mutableListOf()
        // Dog Vaccines
        vaccineList.add(Vaccine(vaccineId = "dog_dhpp", "DHPP (Distemper, Hepatitis, Parvovirus, Parainfluenza)"))
        vaccineList.add(Vaccine(vaccineId = "dog_rabies", "Rabies"))
        vaccineList.add(Vaccine(vaccineId = "dog_bordetella", "Bordetella (Kennel Cough)"))
        vaccineList.add(Vaccine(vaccineId = "dog_leptospirosis", "Leptospirosis"))
        vaccineList.add(Vaccine(vaccineId = "dog_lyme", "Lyme Disease"))
        vaccineList.add(Vaccine(vaccineId = "dog_canine_influenza", "Canine Influenza"))
        vaccineList.add(Vaccine(vaccineId = "dog_coronavirus", "Canine Coronavirus"))

        // Cat Vaccines
        vaccineList.add(Vaccine(vaccineId = "cat_fvrcp", "FVRCP (Feline Viral Rhinotracheitis, Calicivirus, Panleukopenia)"))
        vaccineList.add(Vaccine(vaccineId = "cat_rabies", "Rabies"))
        vaccineList.add(Vaccine(vaccineId = "cat_felv", "FeLV (Feline Leukemia Virus)"))
        vaccineList.add(Vaccine(vaccineId = "cat_fiv", "FIV (Feline Immunodeficiency Virus)"))
        vaccineList.add(Vaccine(vaccineId = "cat_chlamydia", "Chlamydia"))
        vaccineList.add(Vaccine(vaccineId = "cat_bordetella", "Bordetella"))

        emit(vaccineList)
    }


}