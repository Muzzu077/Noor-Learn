package com.noorlearn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noorlearn.domain.model.VocabularyWord
import com.noorlearn.data.local.preferences.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VocabularyBuilderViewModel @Inject constructor(
    private val userPreferences: UserPreferencesDataStore
) : ViewModel() {

    private val _words = MutableStateFlow<List<VocabularyWord>>(emptyList())
    val words: StateFlow<List<VocabularyWord>> = _words.asStateFlow()

    private val staticWords = listOf(
        VocabularyWord(1, "ٱللَّه", "Allah", "Allah / God", 2699, "ٱلْحَمْدُ لِلَّهِ رَبِّ ٱلْعَٰلَمِينَ", "All praise is due to Allah, Lord of the worlds."),
        VocabularyWord(2, "قَالَ", "Qala", "He said", 1709, "قَالَ إِنِّى عَبْدُ ٱللَّهِ", "He said, 'Indeed, I am the servant of Allah.'"),
        VocabularyWord(3, "ٱلَّذِى", "Alladhi", "The one who", 1442, "ٱلَّذِى خَلَقَ سَبْعَ سَمَٰوَٰتٍ", "He who created seven heavens."),
        VocabularyWord(4, "ءَامَنُواْ", "Amanu", "They believed", 812, "يَٰٓأَيُّهَا ٱلَّذِينَ ءَامَنُواْ", "O you who have believed!"),
        VocabularyWord(5, "عَلِيمٌ", "Alim", "All-Knowing", 162, "وَٱللَّهُ بِكُلِّ شَىْءٍ عَلِيمٌ", "And Allah is All-Knowing of all things."),
        VocabularyWord(6, "رَبّ", "Rabb", "Lord / Sustainer", 975, "رَبِّ ٱغْفِرْ لِى", "My Lord, forgive me."),
        VocabularyWord(7, "أَرْض", "Ard", "Earth / Land", 461, "خَلَقَ ٱلسَّمَٰوَٰتِ وَٱلْأَرْضَ", "He created the heavens and the earth."),
        VocabularyWord(8, "كِتَٰب", "Kitab", "Book / Scripture", 261, "ذَٰلِكَ ٱلْكِتَٰبُ لَا رَيْبَ ۛ فِيهِ", "This is the Book about which there is no doubt."),
        VocabularyWord(9, "رَحْمَٰن", "Rahman", "Most Merciful", 170, "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ", "In the name of Allah, the Entirely Merciful, the Especially Merciful."),
        VocabularyWord(10, "يَوْم", "Yawm", "Day", 405, "مَٰلِكِ يَوْمِ ٱلدِّينِ", "Sovereign of the Day of Recompense.")
    )

    init {
        viewModelScope.launch {
            userPreferences.masteredVocabIds.collectLatest { masteredIds ->
                _words.value = staticWords.map { word ->
                    word.copy(isMastered = masteredIds.contains(word.id))
                }
            }
        }
    }

    fun toggleMastery(wordId: Int) {
        viewModelScope.launch {
            userPreferences.toggleVocabMastery(wordId)
            userPreferences.updateStreak()
        }
    }
}
