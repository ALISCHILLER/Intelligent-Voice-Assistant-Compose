@file:OptIn(ExperimentalMaterial3Api::class)

package com.msa.intelligentvoiceassistantcompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar

@Composable
fun VoiceAssistantScreen(activity: MainActivity) {
    val viewModel: VoiceAssistantViewModel = viewModel()
    var responseText by remember { mutableStateOf("منتظر دریافت ورودی...") }
    val userInput by viewModel.userInput // استفاده از userInput از ویو مدل
    val isListening = viewModel.isListening.value

    // درخواست مجوز میکروفون
    LaunchedEffect(Unit) {
        viewModel.requestMicrophonePermission(activity)
    }

    // به‌روزرسانی responseText به‌محض تغییر userInput
    LaunchedEffect(userInput) {
        responseText = generateResponse(userInput) // به‌روزرسانی پاسخ به‌محض تغییر ورودی
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("دستیار صوتی هوشمند") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(Color(0xFFF5F5F5)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            BasicTextField(
                value = userInput,
                onValueChange = { viewModel.userInput.value = it }, // به‌روزرسانی ویو مدل
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.White)
                    .padding(16.dp),
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black, fontSize = 16.sp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        viewModel.startListening(activity)
                    },
                    enabled = !isListening
                ) {
                    Text(text = if (isListening) "در حال گوش دادن..." else "شروع ضبط")
                }

                Button(
                    onClick = {
                        viewModel.userInput.value = "" // پاک کردن از ویو مدل
                    }
                ) {
                    Text(text = "پاک کردن")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "پاسخ:",
                color = Color(0xFF333333),
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Text(
                text = responseText,
                color = Color.Gray,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color(0xFFEFEFEF))
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // اگر نیاز به پردازش مجدد باشد می‌توانید اینجا کد اضافه کنید
                    // مثلاً ارسال پاسخ به یک بخش دیگر
                },
                enabled = userInput.isNotEmpty() && responseText != "منتظر دریافت ورودی..." && !isListening
            ) {
                Text(text = "پخش پاسخ")
            }
        }
    }
}

private fun generateResponse(input: String): String {
    return when {
        input.contains("سلام") -> "سلام! چطور می‌توانم کمکتان کنم؟"
        input.contains("وضعیت هوا") -> "امروز هوا آفتابی است."
        input.contains("چه خبر؟") -> "خبر خاصی نیست، اما من اینجا هستم تا به شما کمک کنم!"
        input.contains("کمک") -> "چگونه می‌توانم به شما کمک کنم؟"
        else -> "پاسخی برای سوال شما ندارم. لطفاً سوال دیگری بپرسید."
    }
}

