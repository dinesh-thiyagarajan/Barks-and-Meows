package com.dineshworkspace.database.pet.dataModels

import kotlinx.serialization.Serializable

@Serializable
data class Pet(
    val id: String, val name: String, val age: Int, val image: String, val petCategory: PetCategory
)

@Serializable
data class PetCategory(
    val id: Int, val category: String, var selected: Boolean = false
)