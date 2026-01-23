package com.app.uicomponents.dataModels

data class VaccineNoteData(
    val id: String,
    val petId: String,
    val vaccine: Vaccine,
    val note: String
)

data class Vaccine(val vaccineId: String, val vaccineName: String)