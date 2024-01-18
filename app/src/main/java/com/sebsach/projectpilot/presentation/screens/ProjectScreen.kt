package com.sebsach.projectpilot.presentation.screens

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

/**
 * @author Sebastian Sacharczuk
 * @github https://github.com/sebastiansacharczuk
 */
@Composable
fun ProjectScreen(){
    val tasks = mutableListOf<String>()
    Column {

        LazyColumn(
            modifier = Modifier.padding(10.dp)
        ) {
            items(tasks) { task ->
                Row{
                    Checkbox(checked = false, onCheckedChange = {})
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = task)
                }

            }
        }
    }
}