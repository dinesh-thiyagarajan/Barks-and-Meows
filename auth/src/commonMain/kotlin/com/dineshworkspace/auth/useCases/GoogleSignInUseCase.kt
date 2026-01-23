package com.dineshworkspace.auth.useCases

import com.dineshworkspace.auth.repository.AuthRepository

class GoogleSignInUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(idToken: String) =
        authRepository.signInWithGoogle(idToken = idToken)
}
