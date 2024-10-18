package com.dineshworkspace.dataModels

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.jetbrains.compose.resources.DrawableResource

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
    val id: Int,
    val category: String,
    @Transient
    val drawableResource: DrawableResource? = null,
    var selected: Boolean = false
)