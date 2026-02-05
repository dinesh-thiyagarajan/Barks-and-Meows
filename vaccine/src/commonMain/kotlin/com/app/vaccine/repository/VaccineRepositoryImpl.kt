package com.app.vaccine.repository

import com.app.vaccine.dataModels.Vaccine
import com.app.vaccine.dataModels.VaccineNote
import com.app.vaccine.dataSource.VaccineDataSource
import kotlinx.coroutines.flow.Flow

class VaccineRepositoryImpl(private val vaccineDataSource: VaccineDataSource) : VaccineRepository {
    override suspend fun getVaccineNotesForPet(petId: String): Flow<List<VaccineNote>> =
        vaccineDataSource.getVaccineNotesForPet(petId = petId)

    override suspend fun addVaccineNoteForPet(
        vaccineNote: VaccineNote
    ): Flow<String> =
        vaccineDataSource.addVaccineNoteForPet(vaccineNote = vaccineNote)

    override suspend fun deleteVaccineNote(petId: String, vaccineNoteId: String): Flow<Boolean> =
        vaccineDataSource.deleteVaccineNote(petId = petId, vaccineNoteId = vaccineNoteId)

    override suspend fun getVaccines(): Flow<List<Vaccine>> = vaccineDataSource.getVaccines()
}
