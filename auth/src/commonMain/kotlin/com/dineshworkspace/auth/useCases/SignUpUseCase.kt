package com.dineshworkspace.auth.useCases

import com.dineshworkspace.auth.repository.AuthRepository

class SignUpUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.signUpWithEmailAndPassword(email = email, password = password)
}
