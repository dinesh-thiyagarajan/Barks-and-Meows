package com.dineshworkspace.vaccine.repository

import com.dineshworkspace.vaccine.dataModels.VaccineNote
import kotlinx.coroutines.flow.Flow

interface VaccineRepository {
    suspend fun getVaccineNotesForPet(petId: String): Flow<List<VaccineNote>>
    suspend fun addVaccineNoteForPet(petId: String, vaccineNote: VaccineNote): Flow<String>
}