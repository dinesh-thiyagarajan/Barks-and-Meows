package com.dineshworkspace.auth

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