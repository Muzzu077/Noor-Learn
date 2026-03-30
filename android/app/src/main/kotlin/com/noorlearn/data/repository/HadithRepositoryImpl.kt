package com.noorlearn.data.repository

import com.noorlearn.domain.model.Hadith
import com.noorlearn.domain.repository.HadithRepository
import com.noorlearn.data.remote.EdgeFunctionService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HadithRepositoryImpl @Inject constructor(
    private val edgeFunctionService: EdgeFunctionService
) : HadithRepository {

    private val allHadiths = listOf(
        Hadith(
            id = 1L,
            source = "Sahih al-Bukhari",
            topic = "Knowledge",
            arabicText = "مَنْ سَلَكَ طَرِيقًا يَلْتَمِسُ فِيهِ عِلْمًا سَهَّلَ اللَّهُ لَهُ بِهِ طَرِيقًا إِلَى الْجَنَّةِ",
            translationEn = "Whoever takes a path in search of knowledge, Allah will make easy for him the path to Paradise.",
            narratorChain = "Abu Hurairah (RA)",
            grade = "Sahih"
        ),
        Hadith(
            id = 2L,
            source = "Sahih al-Bukhari",
            topic = "Qur'an",
            arabicText = "خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ",
            translationEn = "The best among you are those who learn the Qur'an and teach it.",
            narratorChain = "Uthman ibn Affan (RA)",
            grade = "Sahih"
        ),
        Hadith(
            id = 3L,
            source = "Sahih Muslim",
            topic = "Character",
            arabicText = "إِنَّ مِنْ أَحَبِّكُمْ إِلَيَّ وَأَقْرَبِكُمْ مِنِّي مَجْلِسًا يَوْمَ الْقِيَامَةِ أَحَاسِنَكُمْ أَخْلَاقًا",
            translationEn = "The dearest and closest of you to me on the Day of Resurrection will be those who are best in character.",
            narratorChain = "Jabir ibn Abdullah (RA)",
            grade = "Sahih"
        ),
        Hadith(
            id = 4L,
            source = "Sahih al-Bukhari",
            topic = "Prayer",
            arabicText = "مِفْتَاحُ الْجَنَّةِ الصَّلَاةُ",
            translationEn = "The key to Paradise is prayer.",
            narratorChain = "Jabir ibn Abdullah (RA)",
            grade = "Sahih"
        ),
        Hadith(
            id = 5L,
            source = "Sunan at-Tirmidhi",
            topic = "Kindness",
            arabicText = "إِنَّ اللَّهَ رَفِيقٌ يُحِبُّ الرِّفْقَ فِي الْأَمْرِ كُلِّهِ",
            translationEn = "Indeed, Allah is Gentle and loves gentleness in all matters.",
            narratorChain = "Aisha (RA)",
            grade = "Sahih"
        ),
        Hadith(
            id = 6L,
            source = "Sahih Muslim",
            topic = "Faith",
            arabicText = "لَا يُؤْمِنُ أَحَدُكُمْ حَتَّى يُحِبَّ لِأَخِيهِ مَا يُحِبُّ لِنَفْسِهِ",
            translationEn = "None of you truly believes until he loves for his brother what he loves for himself.",
            narratorChain = "Anas ibn Malik (RA)",
            grade = "Sahih"
        ),
        Hadith(
            id = 7L,
            source = "Sahih al-Bukhari",
            topic = "Patience",
            arabicText = "عَجَبًا لِأَمْرِ الْمُؤْمِنِ إِنَّ أَمْرَهُ كُلَّهُ خَيْرٌ",
            translationEn = "How wonderful is the affair of the believer, for all his affairs are good.",
            narratorChain = "Suhaib ar-Rumi (RA)",
            grade = "Sahih"
        ),
        Hadith(
            id = 8L,
            source = "Sunan Abu Dawud",
            topic = "Fasting",
            arabicText = "مَنْ صَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ",
            translationEn = "Whoever fasts Ramadan out of faith and seeking reward, his previous sins will be forgiven.",
            narratorChain = "Abu Hurairah (RA)",
            grade = "Sahih"
        ),
        Hadith(
            id = 9L,
            source = "Sahih Muslim",
            topic = "Dhikr",
            arabicText = "كَلِمَتَانِ خَفِيفَتَانِ عَلَى اللِّسَانِ ثَقِيلَتَانِ فِي الْمِيزَانِ حَبِيبَتَانِ إِلَى الرَّحْمَنِ سُبْحَانَ اللَّهِ وَبِحَمْدِهِ سُبْحَانَ اللَّهِ الْعَظِيمِ",
            translationEn = "Two words are light on the tongue, heavy on the Scale, and beloved to the Most Merciful: SubhanAllahi wa bihamdihi, SubhanAllahil-Azim.",
            narratorChain = "Abu Hurairah (RA)",
            grade = "Sahih"
        ),
        Hadith(
            id = 10L,
            source = "Sahih al-Bukhari",
            topic = "Charity",
            arabicText = "مَا نَقَصَتْ صَدَقَةٌ مِنْ مَالٍ",
            translationEn = "Charity does not decrease wealth.",
            narratorChain = "Abu Hurairah (RA)",
            grade = "Sahih"
        ),
        Hadith(
            id = 11L,
            source = "Sunan at-Tirmidhi",
            topic = "Speech",
            arabicText = "مَنْ كَانَ يُؤْمِنُ بِاللَّهِ وَالْيَوْمِ الْآخِرِ فَلْيَقُلْ خَيْرًا أَوْ لِيَصْمُتْ",
            translationEn = "Whoever believes in Allah and the Last Day, let him speak good or remain silent.",
            narratorChain = "Abu Hurairah (RA)",
            grade = "Sahih"
        ),
        Hadith(
            id = 12L,
            source = "Sahih Muslim",
            topic = "Worship",
            arabicText = "أَحَبُّ الْأَعْمَالِ إِلَى اللَّهِ أَدْوَمُهَا وَإِنْ قَلَّ",
            translationEn = "The most beloved deeds to Allah are those done consistently, even if they are small.",
            narratorChain = "Aisha (RA)",
            grade = "Sahih"
        ),
        Hadith(
            id = 13L,
            source = "Sahih al-Bukhari",
            topic = "Supplication",
            arabicText = "الدُّعَاءُ هُوَ الْعِبَادَةُ",
            translationEn = "Supplication (du'a) is the essence of worship.",
            narratorChain = "An-Nu'man ibn Bashir (RA)",
            grade = "Sahih"
        ),
        Hadith(
            id = 14L,
            source = "Sunan Ibn Majah",
            topic = "Knowledge",
            arabicText = "طَلَبُ الْعِلْمِ فَرِيضَةٌ عَلَى كُلِّ مُسْلِمٍ",
            translationEn = "Seeking knowledge is an obligation upon every Muslim.",
            narratorChain = "Anas ibn Malik (RA)",
            grade = "Hasan"
        ),
        Hadith(
            id = 15L,
            source = "Sahih al-Bukhari",
            topic = "Parents",
            arabicText = "رِضَا الرَّبِّ فِي رِضَا الْوَالِدِ وَسَخَطُ الرَّبِّ فِي سَخَطِ الْوَالِدِ",
            translationEn = "The pleasure of the Lord lies in the pleasure of the parent, and the displeasure of the Lord lies in the displeasure of the parent.",
            narratorChain = "Abdullah ibn Amr (RA)",
            grade = "Sahih"
        )
    )

    override suspend fun getDailyHadith(): Hadith = withContext(Dispatchers.IO) {
        val dayOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        allHadiths[dayOfYear % allHadiths.size]
    }

    override suspend fun getHadithsBySource(source: String): List<Hadith> = withContext(Dispatchers.IO) {
        if (source == "All") allHadiths
        else allHadiths.filter { it.source.contains(source, ignoreCase = true) }
    }

    override suspend fun explainHadith(hadithId: String): Result<String> {
        val hadith = allHadiths.find { it.id.toString() == hadithId }
            ?: return Result.failure(Exception("Hadith not found"))
        return edgeFunctionService.askChatbot(
            "Explain this hadith briefly: \"${hadith.translationEn}\" from ${hadith.source}, narrated by ${hadith.narratorChain}. Topic: ${hadith.topic}"
        )
    }
}
