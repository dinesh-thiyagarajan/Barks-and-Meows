package com.dineshworkspace.dataModels

import kotlinx.serialization.Serializable

@Serializable
data class Pet(
    val id: String,
    val name: String,
    val age: Int,
    val image: String? = null,
    val petCategory: PetCategory
)

@Serializable
data class PetCategory(
    val id: Int, val category: String, var selected: Boolean = false
)