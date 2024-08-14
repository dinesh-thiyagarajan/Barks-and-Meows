package com.dineshworkspace.uicomponents.dataModels


data class PetData(
    val id: String,
    val name: String,
    val age: Int,
    val image: String? = null,
    val petCategory: PetCategory
)

data class PetCategory(
    val id: Int, val category: String, var selected: Boolean = false
)