package com.app.uicomponents.dataModels

import org.jetbrains.compose.resources.DrawableResource


data class PetData(
    val id: String,
    val name: String,
    val age: Int,
    var image: DrawableResource,
    val petCategory: PetCategory
)

data class PetCategory(
    val id: Int, val category: String, var selected: Boolean = false
)