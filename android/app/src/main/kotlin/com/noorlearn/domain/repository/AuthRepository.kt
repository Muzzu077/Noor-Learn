package com.noorlearn.domain.repository

import com.noorlearn.domain.model.User

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun signUp(email: String, password: String, name: String): Result<User>
    suspend fun getCurrentUser(): User?
    suspend fun signOut()
}
