package com.dineshworkspace.vaccine.repository

import com.dineshworkspace.vaccine.dataModels.VaccineNote
import com.dineshworkspace.vaccine.dataSource.VaccineDataSource
import kotlinx.coroutines.flow.Flow

class VaccineRepositoryImpl(private val vaccineDataSource: VaccineDataSource) : VaccineRepository {
    override suspend fun getVaccineNotesForPet(petId: String): Flow<List<VaccineNote>> =
        vaccineDataSource.getVaccineNotesForPet(petId = petId)

    override suspend fun addVaccineNoteForPet(
        petId: String,
        vaccineNote: VaccineNote
    ): Flow<String> =
        vaccineDataSource.addVaccineNoteForPet(petId = petId, vaccineNote = vaccineNote)
}