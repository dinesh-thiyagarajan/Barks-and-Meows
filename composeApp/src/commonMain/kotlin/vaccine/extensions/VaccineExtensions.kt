package vaccine.extensions

import com.dineshworkspace.uicomponents.dataModels.Vaccine
import com.dineshworkspace.uicomponents.dataModels.VaccineNoteData
import com.dineshworkspace.vaccine.dataModels.VaccineNote

fun VaccineNote.toVaccineData(): VaccineNoteData {
    return VaccineNoteData(
        id = this.id,
        petId = petId,
        vaccine = Vaccine(vaccine.vaccineId, vaccine.vaccineName),
        note = note
    )
}