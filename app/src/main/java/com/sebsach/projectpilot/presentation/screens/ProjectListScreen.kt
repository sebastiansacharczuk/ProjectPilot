package com.sebsach.projectpilot.presentation.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
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
import com.sebsach.projectpilot.model.ProjectRefModel
import com.sebsach.projectpilot.presentation.ProjectActivity
import com.sebsach.projectpilot.utils.AndroidUtils
import com.sebsach.projectpilot.utils.FirebaseUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author Sebastian Sacharczuk
 * @github https://github.com/sebastiansacharczuk
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListScreen() {
    val projectRefsModel = remember { mutableStateOf(listOf<ProjectRefModel>()) }
    val loading = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var text by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val username = FirebaseUtils.currentUserEmail()

    LaunchedEffect(key1 = Unit) {
        scope.launch {
            delay(2000)
            val userId = FirebaseUtils.currentUserId()
            if (userId != null) {
                FirebaseUtils.allUsersCollectionReference()
                    .document(userId)
                    .collection("project_refs")
                    .get()
                    .addOnSuccessListener { result ->
                        projectRefsModel.value = result.toObjects(ProjectRefModel::class.java)
                        loading.value = false
                    }
                    .addOnFailureListener{ result ->
                        println(result.message)
                    }
            }
        }
    }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Project name") },
            text = {
                TextField(
                    value = text,
                    onValueChange = { text = it }
                )
            },
            confirmButton = {
                Button(onClick = {
                    if(username != null)
                        FirebaseUtils.createProject(text, username)
                    else
                        println("problem occurred")
                    text = ""
                    showDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = {
                    text = ""
                    showDialog = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (loading.value) {
            // Display a loading indicator
            Text(text = "Loading projects")
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator()
        } else {
            Button(onClick = { showDialog = true }) {
                Text("Create project")
                Icon(imageVector = Icons.Default.Add, contentDescription = "create project")
            }

            LazyColumn(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                if(projectRefsModel.value.isEmpty()){
                    item(key = null){
                        Text(text = "Recently you have no active projects")
                    }
                }
                else{
                    items(projectRefsModel.value) { projectRef ->
                        Button(
                            onClick = {
                                context.startActivity(Intent(context, ProjectActivity::class.java))
                            },
                            colors = ButtonDefaults.buttonColors( containerColor = Color(0xFFEBEBEB), contentColor = Color.Black)
                        ) {
                            Text(text = projectRef.name)
                        }
                    }
                }
            }
        }
    }
}
