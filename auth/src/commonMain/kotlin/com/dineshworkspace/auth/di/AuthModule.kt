package com.dineshworkspace.auth.di

import com.dineshworkspace.auth.dataSource.AuthDataSource
import com.dineshworkspace.auth.repository.AuthRepository
import com.dineshworkspace.auth.repository.AuthRepositoryImpl
import com.dineshworkspace.auth.useCases.IsLoggedInUseCase
import com.dineshworkspace.auth.useCases.LoginUseCase
import com.dineshworkspace.auth.useCases.LogoutUseCase
import com.dineshworkspace.auth.viewModels.AuthViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single<FirebaseAuth> { Firebase.auth }
    single<String> { Firebase.auth.currentUser?.uid ?: "" }
    single<AuthDataSource> { AuthDataSource(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<LoginUseCase> { LoginUseCase(get()) }
    single<IsLoggedInUseCase> { IsLoggedInUseCase(get()) }
    single<LogoutUseCase> { LogoutUseCase(get()) }
    viewModel { AuthViewModel(get()) }
}