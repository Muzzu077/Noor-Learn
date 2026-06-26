package com.noorlearn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noorlearn.domain.model.Prophet
import com.noorlearn.domain.model.ParaStory
import com.noorlearn.domain.repository.ProphetRepository
import com.noorlearn.domain.usecase.AskChatbotUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProphetStoriesViewModel @Inject constructor(
    private val prophetRepository: ProphetRepository,
    private val askChatbotUseCase: AskChatbotUseCase
) : ViewModel() {

    private val _prophets = MutableStateFlow<List<Prophet>>(emptyList())
    val prophets: StateFlow<List<Prophet>> = _prophets.asStateFlow()

    private val _paraStories = MutableStateFlow<List<ParaStory>>(emptyList())
    val paraStories: StateFlow<List<ParaStory>> = _paraStories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Explain Story state
    private val _explainProphetId = MutableStateFlow<Int?>(null)
    val explainProphetId: StateFlow<Int?> = _explainProphetId.asStateFlow()

    private val _explanation = MutableStateFlow<String?>(null)
    val explanation: StateFlow<String?> = _explanation.asStateFlow()

    private val _isExplaining = MutableStateFlow(false)
    val isExplaining: StateFlow<Boolean> = _isExplaining.asStateFlow()

    private val staticStories = listOf(
        ParaStory(
            juzNumber = 1,
            title = "The Creation of Adam & Banu Israil",
            arabicTitle = "الجزء الأول: خلق آدم وبنو إسرائيل",
            story = "Juz 1 begins with Al-Fatiha and transitions into Surah Al-Baqarah, recounting the creation of Adam (AS), his role as a vicegerent on earth, and the commands given to angels. It then detailedly describes the covenants, trials, and history of Banu Israil (Children of Israel), reminding us of the importance of maintaining covenant fidelity.",
            themes = listOf("Creation", "Covenants", "Guidance", "Obedience"),
            lessons = listOf("Repentance is always accepted by Allah.", "Arrogance leads to downfall (as in Iblis).", "Maintaining covenants with Allah is crucial.")
        ),
        ParaStory(
            juzNumber = 2,
            title = "The Changing of the Qiblah & Trials of Patience",
            arabicTitle = "الجزء الثاني: تحويل القبلة والابتلاء",
            story = "Juz 2 describes a major turning point: the command to change the Qiblah direction from Jerusalem (Al-Quds) to Mecca (Al-Ka'bah). It introduces the pillars of patient endurance and prayer, guidelines for food, fasting in Ramadan, struggle, pilgrimage, and family laws, demonstrating how faith builds a community.",
            themes = listOf("Community Development", "Change of Direction", "Patience", "Ramadan"),
            lessons = listOf("True piety is obedience to Allah's instructions.", "Fasting builds self-restraint and taqwa.", "Patience and prayer are our greatest helpers in trials.")
        ),
        ParaStory(
            juzNumber = 3,
            title = "Sovereignty of Allah & Charity Lessons",
            arabicTitle = "الجزء الثالث: آية الكرسي والإنفاق",
            story = "Juz 3 contains Ayah Al-Kursi, the verse of the Throne, illustrating Allah's absolute power and control. It also recounts stories of Ibrahim (AS) questioning resurrection to strengthen faith and details the laws of charity (spending in the way of Allah), warning against interest and transactions with riba.",
            themes = listOf("Divine Power", "Resurrection", "Charity", "Financial Justice"),
            lessons = listOf("Allah has absolute control over everything in the heavens and earth.", "Charity must be given with pure intentions, without reminders of generosity.", "Interest destroys economic harmony.")
        ),
        ParaStory(
            juzNumber = 30,
            title = "The Great Event & Final Protection",
            arabicTitle = "الجزء الثلاثون: النبأ العظيم والمعوذات",
            story = "Juz 30 (Juz Amma) consists of short, powerful surahs focused on the afterlife, creation, the Day of Judgment, and the ultimate sovereignty of Allah. It culminates in Surah Al-Ikhlas, Al-Falaq, and An-Nas, which teach the absolute oneness of God and provide a framework for seeking refuge in Him.",
            themes = listOf("Afterlife", "Reflection on Nature", "Unity of God", "Divine Refuge"),
            lessons = listOf("The Day of Judgment is certain and we must prepare for it.", "Reflecting on nature leads to recognizing the Creator.", "Seeking refuge in Allah protects us from all visible and invisible harm.")
        )
    )

    init {
        loadProphets()
        loadParaStories()
    }

    private fun loadProphets() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _prophets.value = prophetRepository.getProphetStories()
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadParaStories() {
        _paraStories.value = staticStories
    }

    fun explainProphetStory(prophet: Prophet) {
        _explainProphetId.value = prophet.id
        _explanation.value = null
        _isExplaining.value = true
        viewModelScope.launch {
            val prompt = "Explain the key lessons and historical background of the story of Prophet ${prophet.name} (${prophet.arabicName}) briefly (3-4 sentences). Outline the relevance to our daily life:\n\nSummary: ${prophet.summary}\nPeriod: ${prophet.period}\n\nKeep it simple and practical."
            val result = askChatbotUseCase(prompt)
            _explanation.value = result.getOrElse { "Could not load explanation. Please try again." }
            _isExplaining.value = false
        }
    }

    fun dismissExplanation() {
        _explainProphetId.value = null
        _explanation.value = null
    }
}
