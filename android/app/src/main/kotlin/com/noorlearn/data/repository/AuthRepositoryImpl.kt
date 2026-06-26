package com.noorlearn.data.repository

import com.noorlearn.domain.model.User
import com.noorlearn.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
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
            val response = supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                this.data = kotlinx.serialization.json.buildJsonObject {
                    put("name", kotlinx.serialization.json.JsonPrimitive(name))
                }
            }
            val currentUser = supabaseClient.auth.currentUserOrNull()
            if (currentUser != null) {
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
            } else {
                Result.failure(Exception("Verification email sent! Please check your inbox to verify your account before logging in."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            supabaseClient.auth.signInWith(io.github.jan.supabase.auth.providers.builtin.IDToken) {
                this.idToken = idToken
                this.provider = io.github.jan.supabase.auth.providers.Google
            }
            val currentUser = supabaseClient.auth.currentUserOrNull()
                ?: return@withContext Result.failure(Exception("Google Sign in failed: no user returned"))
            Result.success(
                User(
                    id = currentUser.id,
                    name = currentUser.userMetadata?.get("name")?.toString()?.trim('"') ?: "",
                    email = currentUser.email ?: "",
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
        try {
            supabaseClient.auth.awaitInitialization()
        } catch (e: Exception) {
            if (com.noorlearn.BuildConfig.DEBUG) {
                android.util.Log.e("AuthRepository", "Supabase init error", e)
            }
        }
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

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabaseClient.auth.resetPasswordForEmail(email = email)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAccount(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabaseClient.postgrest.rpc("delete_user_account")
            supabaseClient.auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
