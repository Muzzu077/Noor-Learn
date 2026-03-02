package com.noorlearn.data.remote

import com.noorlearn.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class EdgeFunctionService @Inject constructor() {
    private val httpClient = HttpClient(Android)

    suspend fun askChatbot(question: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val url = "${BuildConfig.SUPABASE_URL}/functions/v1/ai-proxy"
            
            val messagesArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", question)
                })
            }
            
            val jsonBody = JSONObject().apply {
                put("messages", messagesArray)
            }.toString()

            val response = httpClient.post(url) {
                header("Authorization", "Bearer ${BuildConfig.SUPABASE_KEY}")
                contentType(ContentType.Application.Json)
                setBody(jsonBody)
            }
            
            val responseText = response.bodyAsText()
            println("EdgeFunctionService Response: ${response.status} -> $responseText")
            
            if (response.status.value in 200..299) {
                val responseJson = JSONObject(responseText)
                
                if (responseJson.has("reply")) {
                    val answer = responseJson.getString("reply")
                    Result.success(answer)
                } else if (responseJson.has("choices")) {
                    val answer = responseJson.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                    Result.success(answer)
                } else {
                    Result.success(responseText) // Fallback to raw text if parsing fails but status is 200
                }
            } else {
                Result.failure(Exception("HTTP Error ${response.status.value}: $responseText"))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(Exception("Network/Client Error: ${e.localizedMessage}"))
        }
    }
}
