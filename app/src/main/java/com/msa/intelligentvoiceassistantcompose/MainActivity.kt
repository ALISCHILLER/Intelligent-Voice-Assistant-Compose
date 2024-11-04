package com.msa.intelligentvoiceassistantcompose

import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.msa.intelligentvoiceassistantcompose.ui.theme.IntelligentVoiceAssistantComposeTheme
import java.util.Locale
import android.Manifest

class MainActivity : ComponentActivity() {

    private lateinit var textToSpeech: TextToSpeech
    private val REQUEST_RECORD_AUDIO_PERMISSION = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale("fa")
            }
        }

        setContent {
            VoiceAssistantScreen(this)
        }
    }

    // درخواست مجوز میکروفون
    fun requestMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_RECORD_AUDIO_PERMISSION)
        }
    }

    override fun onDestroy() {
        textToSpeech.stop()
        textToSpeech.shutdown()
        super.onDestroy()
    }


    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        IntelligentVoiceAssistantComposeTheme {
            VoiceAssistantScreen(this)
        }
    }
}