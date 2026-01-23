package com.dineshworkspace.vaccine.repository

import com.dineshworkspace.vaccine.dataModels.Vaccine
import com.dineshworkspace.vaccine.dataModels.VaccineNote
import kotlinx.coroutines.flow.Flow

interface VaccineRepository {
    suspend fun getVaccineNotesForPet(petId: String): Flow<List<VaccineNote>>
    suspend fun addVaccineNoteForPet(vaccineNote: VaccineNote): Flow<String>
    suspend fun deleteVaccineNote(petId: String, vaccineNoteId: String): Flow<Boolean>
    suspend fun getVaccines(): Flow<List<Vaccine>>
}