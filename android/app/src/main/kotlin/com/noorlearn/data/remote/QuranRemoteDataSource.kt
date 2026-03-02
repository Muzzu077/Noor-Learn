package com.noorlearn.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuranRemoteDataSource @Inject constructor(
    private val supabaseClient: SupabaseClient
) {
    // Add remote calls here for sync
}
