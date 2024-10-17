package com.dineshworkspace.vaccine.useCases

import com.dineshworkspace.vaccine.repository.VaccineRepository

class GetVaccinesUseCase(private val vaccineRepository: VaccineRepository) {
    suspend operator fun invoke() =
        vaccineRepository.getVaccines()
}