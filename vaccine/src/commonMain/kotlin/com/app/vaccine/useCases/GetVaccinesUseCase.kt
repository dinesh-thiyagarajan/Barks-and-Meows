package com.app.vaccine.useCases

import com.app.vaccine.repository.VaccineRepository

class GetVaccinesUseCase(private val vaccineRepository: VaccineRepository) {
    suspend operator fun invoke() =
        vaccineRepository.getVaccines()
}
