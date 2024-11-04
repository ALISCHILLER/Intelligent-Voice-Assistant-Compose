package com.msa.intelligentvoiceassistantcompose

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class VoiceAssistantViewModel(application: Application) : AndroidViewModel(application) {
    var userInput = mutableStateOf("")
    var isListening = mutableStateOf(false)

    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(application)

    init {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                isListening.value = true
            }

            override fun onBeginningOfSpeech() {
                isListening.value = true
            }

            override fun onRmsChanged(rmsdB: Float) {
                // تغییرات شدت صدا، اینجا می‌توانید برای نمایش به کاربر استفاده کنید
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // بافر صوتی دریافت شده
            }

            override fun onEndOfSpeech() {
                isListening.value = false
            }

            override fun onError(error: Int) {
                isListening.value = false
                userInput.value = when (error) {
                    SpeechRecognizer.ERROR_NETWORK -> "خطا در اتصال به شبکه."
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "پایان زمان اتصال به شبکه."
                    SpeechRecognizer.ERROR_AUDIO -> "خطا در ضبط صدا."
                    SpeechRecognizer.ERROR_CLIENT -> "خطای کلاینت."
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "مجوز لازم برای دسترسی به میکروفون وجود ندارد."
                    SpeechRecognizer.ERROR_NO_MATCH -> "هیچ متنی شناسایی نشد."
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "سرویس تشخیص گفتار در حال استفاده است."
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "زمان ضبط به پایان رسید."
                    else -> "خطای ناشناخته."
                }
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                userInput.value = matches?.get(0) ?: "متنی شناسایی نشد."
                isListening.value = false
            }

            override fun onPartialResults(partialResults: Bundle?) {
                // نتایج جزئی را می‌توانید پردازش کنید
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // رخدادهای دیگر، در صورت نیاز پردازش کنید
            }
        })
    }

    fun startListening(activity: MainActivity) {
        if (!isListening.value) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fa")
                putExtra(RecognizerIntent.EXTRA_PROMPT, "لطفاً صحبت کنید...")
            }
            speechRecognizer.startListening(intent)
        }
    }

    fun requestMicrophonePermission(activity: MainActivity) {
        activity.requestMicrophonePermission()
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizer.destroy()
    }
}
