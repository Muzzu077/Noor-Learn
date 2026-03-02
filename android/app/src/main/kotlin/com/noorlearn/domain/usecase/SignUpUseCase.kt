package com.noorlearn.domain.usecase

import com.noorlearn.domain.model.User
import com.noorlearn.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, name: String): Result<User> {
        return authRepository.signUp(email, password, name)
    }
}
