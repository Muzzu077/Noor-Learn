package com.noorlearn.data.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

sealed interface SpeechState {
    object Idle : SpeechState
    object Listening : SpeechState
    object Processing : SpeechState
    data class Success(val text: String) : SpeechState
    data class Error(val message: String) : SpeechState
}

@Singleton
class AndroidSpeechRecognizer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var speechRecognizer: SpeechRecognizer? = null
    
    private val _state = MutableStateFlow<SpeechState>(SpeechState.Idle)
    val state: StateFlow<SpeechState> = _state.asStateFlow()

    fun startListening() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _state.value = SpeechState.Error("Speech recognition is not available on this device.")
            return
        }

        android.os.Handler(android.os.Looper.getMainLooper()).post {
            try {
                speechRecognizer?.destroy()
                
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                    setRecognitionListener(object : RecognitionListener {
                        override fun onReadyForSpeech(params: Bundle?) {
                            _state.value = SpeechState.Listening
                        }

                        override fun onBeginningOfSpeech() {}

                        override fun onRmsChanged(rmsdB: Float) {}

                        override fun onBufferReceived(buffer: ByteArray?) {}

                        override fun onEndOfSpeech() {
                            _state.value = SpeechState.Processing
                        }

                        override fun onError(error: Int) {
                            val message = when (error) {
                                SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                                SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Audio permission denied"
                                SpeechRecognizer.ERROR_NETWORK -> "Network error"
                                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                                SpeechRecognizer.ERROR_NO_MATCH -> "Could not understand speech"
                                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Speech recognizer is busy"
                                SpeechRecognizer.ERROR_SERVER -> "Server error"
                                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input detected"
                                else -> "Speech error: $error"
                            }
                            _state.value = SpeechState.Error(message)
                        }

                        override fun onResults(results: Bundle?) {
                            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                            if (!matches.isNullOrEmpty()) {
                                _state.value = SpeechState.Success(matches[0])
                            } else {
                                _state.value = SpeechState.Error("No match found")
                            }
                        }

                        override fun onPartialResults(partialResults: Bundle?) {
                            val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                            if (!matches.isNullOrEmpty()) {
                                _state.value = SpeechState.Success(matches[0])
                            }
                        }

                        override fun onEvent(eventType: Int, params: Bundle?) {}
                    })
                }

                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-SA")
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ar-SA")
                    putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "ar-SA")
                    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                }
                
                speechRecognizer?.startListening(intent)
                _state.value = SpeechState.Listening
            } catch (e: Exception) {
                _state.value = SpeechState.Error(e.message ?: e.toString())
            }
        }
    }

    fun stopListening() {
        android.os.Handler(android.os.Looper.getMainLooper()).post {
            try {
                speechRecognizer?.stopListening()
            } catch (e: Exception) {
                android.util.Log.e("AndroidSpeechRecognizer", "Error stopping speech recognition", e)
            }
        }
    }

    fun reset() {
        _state.value = SpeechState.Idle
    }

    fun destroy() {
        android.os.Handler(android.os.Looper.getMainLooper()).post {
            speechRecognizer?.destroy()
            speechRecognizer = null
            _state.value = SpeechState.Idle
        }
    }
}
