package com.noorlearn.data.repository

import com.noorlearn.domain.model.Prophet
import com.noorlearn.domain.repository.ProphetRepository
import com.noorlearn.data.remote.dto.ProphetDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProphetRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : ProphetRepository {
    override suspend fun getProphetStories(): List<Prophet> = withContext(Dispatchers.IO) {
        try {
            val response = supabaseClient.postgrest["prophets"].select().decodeList<ProphetDto>()
            if (response.isNotEmpty()) {
                return@withContext response.map { dto ->
                    Prophet(
                        id = dto.id.toIntOrNull() ?: dto.id.hashCode(),
                        name = dto.nameEnglish,
                        arabicName = dto.nameArabic,
                        period = dto.timelineEra,
                        summary = dto.shortSummary,
                        moralLesson = dto.fullStory,
                        imageUrl = dto.imageUrl,
                        audioUrl = dto.audioUrl
                    )
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("ProphetRepository", "Error fetching from Supabase, using fallback", e)
        }
        
        // Return local fallback data if Supabase is empty or fails
        return@withContext getLocalFallbackProphets()
    }

    private fun getLocalFallbackProphets(): List<Prophet> {
        return listOf(
            Prophet(
                id = 1,
                name = "Adam (AS)",
                arabicName = "آدَم",
                period = "The Beginning",
                summary = "The first human and prophet. Created from clay by Allah, he was taught the names of all things. After eating from the forbidden tree, he and Hawa were sent to Earth where he sought forgiveness.",
                moralLesson = "Allah is the Most Forgiving. Repentance is always accepted when it is sincere.",
                imageUrl = "",
                audioUrl = ""
            ),
            Prophet(
                id = 2,
                name = "Nuh (AS) - Noah",
                arabicName = "نُوح",
                period = "Pre-Historic",
                summary = "Preached for 950 years to an idol-worshipping nation. When they rejected him, Allah commanded him to build an ark to save the believers and pairs of animals from a great flood.",
                moralLesson = "Patience and steadfastness in faith, even when facing a majority who disagree with you.",
                imageUrl = "",
                audioUrl = ""
            ),
            Prophet(
                id = 3,
                name = "Ibrahim (AS) - Abraham",
                arabicName = "إِبْرَاهِيم",
                period = "Ancient Era",
                summary = "Known as the Friend of Allah (Khalilullah). He rejected idol worship, survived being thrown into a fire by King Nimrod, built the Kaaba with his son Ismail, and established the rites of Hajj.",
                moralLesson = "Absolute trust in Allah's plan and unwavering monotheism against all odds.",
                imageUrl = "",
                audioUrl = ""
            ),
            Prophet(
                id = 4,
                name = "Musa (AS) - Moses",
                arabicName = "مُوسَىٰ",
                period = "c. 13th Century BCE",
                summary = "Sent to the tyrannical Pharaoh of Egypt to free the Children of Israel. Known for miracles like the splitting of the sea and receiving the Torah on Mount Sinai.",
                moralLesson = "Truth will ultimately conquer falsehood, and Allah always protects the oppressed.",
                imageUrl = "",
                audioUrl = ""
            ),
            Prophet(
                id = 5,
                name = "Isa (AS) - Jesus",
                arabicName = "عِيسَىٰ",
                period = "1st Century CE",
                summary = "Miraculously born to the Virgin Maryam. He performed miracles by Allah's permission, such as healing the blind and raising the dead, and brought the Injeel (Gospel).",
                moralLesson = "Compassion, humility, and the importance of spiritual devotion over worldly materialism.",
                imageUrl = "",
                audioUrl = ""
            ),
            Prophet(
                id = 6,
                name = "Muhammad (SAW) - Muhammad",
                arabicName = "مُحَمَّد",
                period = "570 – 632 CE",
                summary = "The Final Messenger of Allah, known as the 'Seal of the Prophets'. He received the revelation of the Holy Qur'an over 23 years, unifying Arabia under the banner of Islam.",
                moralLesson = "Excellence in character. The Prophet was sent as a mercy to all of mankind, demonstrating perfect justice, love, and leadership.",
                imageUrl = "",
                audioUrl = ""
            )
        )
    }

    override suspend fun getStory(prophetId: String): Prophet = withContext(Dispatchers.IO) {
        getProphetStories().firstOrNull { it.id.toString() == prophetId }
            ?: throw IllegalArgumentException("Prophet with id $prophetId not found")
    }
}
