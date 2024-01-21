package com.sebsach.projectpilot.presentation.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.sebsach.projectpilot.presentation.ProjectActivity
import com.sebsach.projectpilot.utils.FirebaseUtils
import kotlinx.coroutines.launch

/**
 * @author Sebastian Sacharczuk
 * @github https://github.com/sebastiansacharczuk
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListScreen() {
    var projectRefsList by remember { mutableStateOf(listOf<Map<String, String>>()) }
    val loading = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var inputProjectName by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val username = FirebaseUtils.currentUserEmail()

    LaunchedEffect(key1 = Unit) {
        scope.launch {

            val userId = FirebaseUtils.currentUserId()
            if (userId != null) {
                projectRefsList = FirebaseUtils.getUserProjectRefs(userId)
                loading.value = false
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Project name") },
            text = {
                TextField(
                    value = inputProjectName,
                    onValueChange = { inputProjectName = it },
                    label = { Text(text = "name")}
                )
                TextField(
                    value = inputProjectName,
                    onValueChange = { inputProjectName = it },
                    label = { Text(text = "name")}
                )
            },
            confirmButton = {
                Button(onClick = {
                    if(username != null)
                        FirebaseUtils.createProject(inputProjectName)
                    else
                        println("problem occurred")
                    inputProjectName = ""
                    showDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = {
                    inputProjectName = ""
                    showDialog = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (loading.value) {
            // Display a loading indicator
            Text(text = "Loading projects")
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator()
        } else {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.Start
            ){

                Button(
                    onClick = { showDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text(text = "New Project")
                    // Icon(imageVector = Icons.Default.Add, contentDescription = "create project")
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (projectRefsList.isEmpty()) {
                        item(key = null) {
                            Text(text = "Recently you have no active projects")
                        }
                    } else {
                        items(projectRefsList) { projectRef ->
                            Button(
                                onClick = {
                                    context.startActivity(
                                        Intent(context, ProjectActivity::class.java)
                                            .putExtra("id", projectRef["id"])
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFFEBEBEB
                                    ), contentColor = Color.Black
                                )
                            ) {
                                projectRef["name"]?.let { Text(text = it) }
                            }
                        }
                    }
                }
            }
        }
    }
}

