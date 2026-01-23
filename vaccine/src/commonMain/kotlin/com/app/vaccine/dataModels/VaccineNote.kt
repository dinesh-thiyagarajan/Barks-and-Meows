package com.app.vaccine.dataModels

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class VaccineNote(
    val id: String,
    val petId: String,
    val vaccine: Vaccine,
    val note: String,
    val timestamp: String,
    val doctorName: String
)
