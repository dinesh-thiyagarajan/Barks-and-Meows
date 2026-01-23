package com.app.extensions

import barksandmeows.pet.generated.resources.Res
import barksandmeows.pet.generated.resources.ic_app_logo
import barksandmeows.pet.generated.resources.ic_cat
import barksandmeows.pet.generated.resources.ic_dog
import org.jetbrains.compose.resources.DrawableResource

/**
 * Helper function to get the appropriate pet icon based on category.
 * This allows other modules to get pet icons without directly accessing internal Res.
 */
fun getPetIcon(category: String): DrawableResource {
    return when (category.lowercase()) {
        "dog" -> Res.drawable.ic_dog
        "cat" -> Res.drawable.ic_cat
        else -> Res.drawable.ic_app_logo
    }
}
