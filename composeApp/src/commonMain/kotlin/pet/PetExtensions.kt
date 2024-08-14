package pet

import com.dineshworkspace.database.pet.dataModels.Pet
import com.dineshworkspace.uicomponents.dataModels.PetCategory
import com.dineshworkspace.uicomponents.dataModels.PetData

fun Pet.toPetData(): PetData {
    return PetData(
        id = id,
        name = name,
        age = age,
        image = image,
        petCategory = PetCategory(
            id = petCategory.id,
            category = petCategory.category,
            selected = petCategory.selected
        )
    )
}