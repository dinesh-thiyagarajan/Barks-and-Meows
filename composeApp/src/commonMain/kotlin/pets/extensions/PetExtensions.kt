package pets.extensions

import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.ic_app_logo
import com.dineshworkspace.dataModels.Pet
import com.dineshworkspace.uicomponents.dataModels.PetCategory
import com.dineshworkspace.uicomponents.dataModels.PetData

fun Pet.toPetData(): PetData {
    return PetData(
        id = id,
        name = name,
        age = age,
        image = if (petCategory.category == "Dog") Res.drawable.ic_app_logo else Res.drawable.ic_app_logo,
        petCategory = PetCategory(
            id = petCategory.id,
            category = petCategory.category,
            selected = petCategory.selected
        )
    )
}