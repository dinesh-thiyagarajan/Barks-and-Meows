package com.dineshworkspace.vaccine.dataModels

import kotlinx.serialization.Serializable

@Serializable
data class Vaccine(val vaccineId: String, val vaccineName: String)