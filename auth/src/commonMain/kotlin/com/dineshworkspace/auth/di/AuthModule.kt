package com.dineshworkspace.auth.di

import com.dineshworkspace.auth.useCases.LoginUseCase
import com.dineshworkspace.auth.dataSource.AuthDataSource
import com.dineshworkspace.auth.repository.AuthRepository
import com.dineshworkspace.auth.repository.AuthRepositoryImpl
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import org.koin.dsl.module

val authModule = module {
    single<FirebaseAuth> { Firebase.auth }
    single<AuthDataSource> { AuthDataSource(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<LoginUseCase> { LoginUseCase(get()) }
}