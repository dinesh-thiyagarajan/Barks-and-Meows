package com.app.vaccine.dataSource

import com.app.vaccine.dataModels.Vaccine
import com.app.vaccine.dataModels.VaccineNote
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class VaccineDataSource(
    private val firestore: FirebaseFirestore,
    private val userId: String,
    private val petCollection: String,
    private val baseEnv: String,
    private val vaccineNotesCollection: String
) {

    fun getVaccineNotesForPet(petId: String) = flow {
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

    fun addVaccineNoteForPet(vaccineNote: VaccineNote) = flow {
        val docId =
            firestore.collection(baseEnv).document(petCollection).collection(userId)
                .document(vaccineNote.petId)
                .collection(vaccineNotesCollection).add(vaccineNote, buildSettings = {
                    encodeDefaults = true
                }).id
        emit(docId)
    }

    fun deleteVaccineNote(petId: String, vaccineNoteId: String) = flow {
        firestore.collection(baseEnv).document(petCollection).collection(userId)
            .document(petId)
            .collection(vaccineNotesCollection)
            .document(vaccineNoteId)
            .delete()
        emit(true)
    }

    fun getVaccines() = flow {
        val vaccineList: MutableList<Vaccine> = mutableListOf()
        // Dog Vaccines
        vaccineList.add(Vaccine(vaccineId = "dog_dhpp", "DHPP (Distemper, Hepatitis, Parvovirus, Parainfluenza)"))
        vaccineList.add(Vaccine(vaccineId = "dog_rabies", "Rabies"))
        vaccineList.add(Vaccine(vaccineId = "dog_bordetella", "Bordetella (Kennel Cough)"))
        vaccineList.add(Vaccine(vaccineId = "dog_leptospirosis", "Leptospirosis"))
        vaccineList.add(Vaccine(vaccineId = "dog_lyme", "Lyme Disease"))
        vaccineList.add(Vaccine(vaccineId = "dog_canine_influenza", "Canine Influenza"))
        vaccineList.add(Vaccine(vaccineId = "dog_coronavirus", "Canine Coronavirus"))
        vaccineList.add(Vaccine(vaccineId = "dog_rattlesnake", "Rattlesnake Vaccine"))
        vaccineList.add(Vaccine(vaccineId = "dog_parainfluenza", "Canine Parainfluenza"))
        vaccineList.add(Vaccine(vaccineId = "dog_giardia", "Giardia"))

        // Cat Vaccines
        vaccineList.add(
            Vaccine(vaccineId = "cat_fvrcp", "FVRCP (Feline Viral Rhinotracheitis, Calicivirus, Panleukopenia)")
        )
        vaccineList.add(Vaccine(vaccineId = "cat_rabies", "Rabies"))
        vaccineList.add(Vaccine(vaccineId = "cat_felv", "FeLV (Feline Leukemia Virus)"))
        vaccineList.add(Vaccine(vaccineId = "cat_fiv", "FIV (Feline Immunodeficiency Virus)"))
        vaccineList.add(Vaccine(vaccineId = "cat_chlamydia", "Chlamydia"))
        vaccineList.add(Vaccine(vaccineId = "cat_bordetella", "Bordetella"))
        vaccineList.add(Vaccine(vaccineId = "cat_fip", "FIP (Feline Infectious Peritonitis)"))
        vaccineList.add(Vaccine(vaccineId = "cat_ringworm", "Feline Ringworm"))
        vaccineList.add(Vaccine(vaccineId = "cat_giardia", "Feline Giardia"))

        emit(vaccineList)
    }

    fun getVaccineReminders(petIds: List<String>): Flow<List<VaccineNote>> {
        if (petIds.isEmpty()) return flowOf(emptyList())

        val flows = petIds.map { petId ->
            getVaccineNotesForPet(petId).map { notes ->
                notes.filter { it.reminderTimestamp != null }
            }
        }

        return combine(flows) { arrays ->
            arrays.flatMap { it.toList() }
        }
    }

    fun updateVaccineNote(vaccineNote: VaccineNote) = flow {
        firestore.collection(baseEnv).document(petCollection).collection(userId)
            .document(vaccineNote.petId)
            .collection(vaccineNotesCollection)
            .document(vaccineNote.id)
            .set(vaccineNote, buildSettings = { encodeDefaults = true })
        emit(true)
    }
}
