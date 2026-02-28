package com.app.vaccine.useCases

import com.app.vaccine.repository.VaccineRepository

class GetVaccineRemindersUseCase(private val vaccineRepository: VaccineRepository) {
    suspend operator fun invoke(petIds: List<String>) =
        vaccineRepository.getVaccineReminders(petIds = petIds)
}
