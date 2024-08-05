package com.dineshworkspace.database.pet.dataModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pet(
    val id: String, val name: String, val age: Int, val image: String, val petType: PetType
)

@Serializable
sealed interface PetType {
    @Serializable
    @SerialName("Dog")
    data object Dog : PetType

    @Serializable
    @SerialName("Cat")
    data object Cat : PetType
}