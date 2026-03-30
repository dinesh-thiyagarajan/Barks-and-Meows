package di

import com.app.pet.di.petIosModule
import org.koin.core.module.Module

actual fun platformModules(): List<Module> = listOf(petIosModule)
