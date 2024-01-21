package com.sebsach.projectpilot.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebsach.projectpilot.utils.FirebaseUtils

/**
 * @author Sebastian Sacharczuk
 * @github https://github.com/sebastiansacharczuk
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    id: String,
    tasks: List<Map<String, Any>>,
    isLeader: Boolean
) {
    var showDialog by remember { mutableStateOf(false) }
    var inputNewTask by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("New Task") },
            text = {
                TextField(
                    value = inputNewTask,
                    onValueChange = { inputNewTask = it }
                )
            },
            confirmButton = {
                Button(onClick = {
                    FirebaseUtils.addNewTask(id, inputNewTask)
                    inputNewTask = ""
                    showDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = {
                    inputNewTask = ""
                    showDialog = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f) // This makes the Box fill all available space above the deadline field
                .padding(14.dp)
                .background(Color(0xFFE6E6E6))
                .clip(RoundedCornerShape(16.dp)),  // Apply clip to the parent Box
        ) {
            Column {

                IconButton(
                    onClick = { showDialog = true },
                    modifier = Modifier.align(Alignment.End),
                    enabled = isLeader
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize() // This makes the LazyColumn fill all available space
                        .clip(RoundedCornerShape(16.dp)) // This gives the LazyColumn rounded corners
                    ,
                    contentPadding = PaddingValues(6.dp)
                ) {
                    items(tasks) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = 4.dp,
                            shape = RoundedCornerShape(16.dp)  // Add rounded corners to the card
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item["task"].toString(),
                                    fontSize = 20.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Checkbox(
                                    checked = item["done"] as Boolean,
                                    onCheckedChange = null,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color(0xFF006400),
                                        uncheckedColor = Color(0xFF8B0000)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp) // Adjust this to change the height of the deadline field box
                .background(Color(0xFFE6E6E6)) // This makes the background color the same as the LazyColumn
                .padding(14.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Deadline field")
        }
    }
}

