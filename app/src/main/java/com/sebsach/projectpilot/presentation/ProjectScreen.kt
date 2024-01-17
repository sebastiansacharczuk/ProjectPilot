package com.sebsach.projectpilot.presentation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import com.sebsach.projectpilot.model.ProjectRef
import com.sebsach.projectpilot.utils.FirebaseUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * @author Sebastian Sacharczuk
 * @github https://github.com/sebastiansacharczuk
 */

@Composable
fun ProjectsScreen() {
    val projectRefs = remember { mutableStateOf(listOf<ProjectRef>()) }
    val loading = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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
                        projectRefs.value = result.toObjects(ProjectRef::class.java)
                        loading.value = false
                    }
                    .addOnFailureListener{ result ->
                        println(result.message)
                    }
            }
        }
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
            LazyColumn(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                if(projectRefs.value.isEmpty()){
                    item(key = null){
                        Text(text = "Recently you have no active projects")
                    }
                }
                else{
                    items(projectRefs.value) { projectRef ->
                        Button(
                            onClick = {
                                context.startActivity(Intent(context, ChatActivity::class.java))
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