package com.sebsach.projectpilot.presentation.screens

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.sebsach.projectpilot.utils.FirebaseUtils

/**
 * @author Sebastian Sacharczuk
 * @github https://github.com/sebastiansacharczuk
 */

data class Message(var content: String, var author: String)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(
    projectId: String,
    username: String,
    chat: List<Map<String, Any>>
) {
    val listState = rememberLazyListState()
    var text by remember { mutableStateOf("") }
    val chatList = remember { mutableStateListOf(*chat.map {
        Message((it["content"] as? String) ?: "", (it["author"] as? String) ?: "")
    }.toTypedArray()) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = null) {
        FirebaseUtils.allProjects().document(projectId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val chatSnapshot = snapshot.get("chat")
                    if (chatSnapshot != null) {
                        val chatFromSnapshot = (chatSnapshot as List<Map<String, Any>>).map {
                            Message((it["content"] as? String) ?: "", (it["author"] as? String) ?: "")
                        }
                        chatList.clear()
                        chatList.addAll(chatFromSnapshot)
                    } else {
                        Log.d(ContentValues.TAG, "Chat data: null")
                    }
                } else {
                    Log.d(ContentValues.TAG, "Current data: null")
                }
            }
    }

    // This effect runs whenever messages changes and scrolls to the end of the list
    LaunchedEffect(chatList) {
        listState.animateScrollToItem(index = if(chatList.size > 0) chatList.size - 1 else 0)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(state = listState, modifier = Modifier.padding(bottom = 70.dp)) {
            items(chatList.toList()) { message ->
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = message.author,
                        color = Color.Gray
                    )
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFD5D5D5)),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = message.content,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = {},
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            Button(
                onClick = {
                    FirebaseUtils.addMessage(projectId, text, username)
                    text = ""
                    keyboardController?.hide()
                          },
                modifier = Modifier.padding(start = 8.dp),
                enabled = text.isNotBlank()  // Enable button only when text is not blank
            ) {
                Text(text = "Send")
            }
        }
    }
}
