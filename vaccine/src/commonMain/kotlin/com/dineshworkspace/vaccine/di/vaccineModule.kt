package com.dineshworkspace.vaccine.di

import com.dineshworkspace.vaccine.viewModels.VaccineNoteViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vaccineModule = module {
    viewModel { VaccineNoteViewModel() }
}