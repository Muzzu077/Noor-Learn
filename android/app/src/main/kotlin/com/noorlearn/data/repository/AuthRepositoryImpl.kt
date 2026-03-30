package com.noorlearn.data.repository

import com.noorlearn.domain.model.User
import com.noorlearn.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            supabaseClient.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            val currentUser = supabaseClient.auth.currentUserOrNull()
                ?: return@withContext Result.failure(Exception("Sign in failed: no user returned"))
            Result.success(
                User(
                    id = currentUser.id,
                    name = currentUser.userMetadata?.get("name")?.toString()?.trim('"') ?: "",
                    email = currentUser.email ?: email,
                    roleMode = "adult",
                    streakDays = 0,
                    isPremium = false,
                    createdAt = currentUser.createdAt.toString()
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(email: String, password: String, name: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                this.data = kotlinx.serialization.json.buildJsonObject {
                    put("name", kotlinx.serialization.json.JsonPrimitive(name))
                }
            }
            val currentUser = supabaseClient.auth.currentUserOrNull()
                ?: return@withContext Result.failure(Exception("Sign up failed: no user returned"))
            Result.success(
                User(
                    id = currentUser.id,
                    name = name,
                    email = currentUser.email ?: email,
                    roleMode = "adult",
                    streakDays = 0,
                    isPremium = false,
                    createdAt = currentUser.createdAt.toString()
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? = withContext(Dispatchers.IO) {
        val currentUser = supabaseClient.auth.currentUserOrNull() ?: return@withContext null
        User(
            id = currentUser.id,
            name = currentUser.userMetadata?.get("name")?.toString()?.trim('"') ?: "",
            email = currentUser.email ?: "",
            roleMode = "adult",
            streakDays = 0,
            isPremium = false,
            createdAt = currentUser.createdAt.toString()
        )
    }

    override suspend fun signOut() = withContext(Dispatchers.IO) {
        supabaseClient.auth.signOut()
    }
}
