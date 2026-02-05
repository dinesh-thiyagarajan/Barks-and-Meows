package vaccine.extensions

import com.app.uicomponents.dataModels.Vaccine
import com.app.uicomponents.dataModels.VaccineNoteData
import com.app.vaccine.dataModels.VaccineNote

fun VaccineNote.toVaccineData(): VaccineNoteData {
    return VaccineNoteData(
        id = this.id,
        petId = petId,
        vaccine = Vaccine(vaccine.vaccineId, vaccine.vaccineName),
        note = note
    )
}
