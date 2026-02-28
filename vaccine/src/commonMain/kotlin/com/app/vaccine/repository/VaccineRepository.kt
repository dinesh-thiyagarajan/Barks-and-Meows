package com.app.vaccine.repository

import com.app.vaccine.dataModels.Vaccine
import com.app.vaccine.dataModels.VaccineNote
import kotlinx.coroutines.flow.Flow

interface VaccineRepository {
    suspend fun getVaccineNotesForPet(petId: String): Flow<List<VaccineNote>>
    suspend fun addVaccineNoteForPet(vaccineNote: VaccineNote): Flow<String>
    suspend fun deleteVaccineNote(petId: String, vaccineNoteId: String): Flow<Boolean>
    suspend fun getVaccines(): Flow<List<Vaccine>>
    suspend fun getVaccineReminders(petIds: List<String>): Flow<List<VaccineNote>>
    suspend fun updateVaccineNote(vaccineNote: VaccineNote): Flow<Boolean>
}
