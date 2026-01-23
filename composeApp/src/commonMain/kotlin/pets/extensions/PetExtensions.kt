package pets.extensions

import com.app.dataModels.Pet
import com.app.extensions.getPetIcon
import com.app.uicomponents.dataModels.PetCategory
import com.app.uicomponents.dataModels.PetData

/**
 * Extension function to convert Pet domain model to PetData UI model.
 * Uses getPetIcon helper from pet module to access icons properly.
 */
fun Pet.toPetData(): PetData {
    return PetData(
        id = id,
        name = name,
        age = age,
        image = getPetIcon(petCategory.category),
        petCategory = PetCategory(
            id = petCategory.id,
            category = petCategory.category,
            selected = petCategory.selected
        )
    )
}
