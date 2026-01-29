package com.app.auth.di

import com.app.auth.dataSource.AuthDataSource
import com.app.auth.repository.AuthRepository
import com.app.auth.repository.AuthRepositoryImpl
import com.app.auth.useCases.DeleteAccountUseCase
import com.app.auth.useCases.ForgotPasswordUseCase
import com.app.auth.useCases.GoogleSignInUseCase
import com.app.auth.useCases.IsLoggedInUseCase
import com.app.auth.useCases.LoginUseCase
import com.app.auth.useCases.LogoutUseCase
import com.app.auth.useCases.SignUpUseCase
import com.app.auth.viewModels.AuthViewModel
import com.app.auth.viewModels.ProfileViewModel
import com.app.auth.viewModels.SignUpViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import org.koin.core.qualifier.named
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single<FirebaseAuth> { Firebase.auth }
    single<String>(named("user_id")) { Firebase.auth.currentUser?.uid ?: "" }
    single<AuthDataSource> { AuthDataSource(firebaseAuth = get()) }
    single<AuthRepository> { AuthRepositoryImpl(authDataSource = get()) }
    single<LoginUseCase> { LoginUseCase(authRepository = get()) }
    single<SignUpUseCase> { SignUpUseCase(authRepository = get()) }
    single<GoogleSignInUseCase> { GoogleSignInUseCase(authRepository = get()) }
    single<IsLoggedInUseCase> { IsLoggedInUseCase(authRepository = get()) }
    single<LogoutUseCase> { LogoutUseCase(authRepository = get()) }
    single<DeleteAccountUseCase> { DeleteAccountUseCase(authRepository = get()) }
    single<ForgotPasswordUseCase> { ForgotPasswordUseCase(authRepository = get()) }
    viewModel { AuthViewModel(loginUseCase = get(), googleSignInUseCase = get(), forgotPasswordUseCase = get()) }
    viewModel { SignUpViewModel(signUpUseCase = get()) }
    viewModel { ProfileViewModel(logoutUseCase = get(), deleteAccountUseCase = get(), firebaseAuth = get()) }
}