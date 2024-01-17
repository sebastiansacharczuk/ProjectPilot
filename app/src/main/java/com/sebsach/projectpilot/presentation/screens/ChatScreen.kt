package com.sebsach.projectpilot.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

/**
 * @author Sebastian Sacharczuk
 * @github https://github.com/sebastiansacharczuk
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    val (text, setText) = remember { mutableStateOf(TextFieldValue()) }
    val (messages, setMessages) = remember { mutableStateOf(listOf<String>()) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 60.dp)) {
            messages.forEach { message ->
                Text(text = message)
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = setText,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    setMessages(messages + text.text)
                    setText(TextFieldValue())
                },
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(text = "Send")
            }
        }
    }
}