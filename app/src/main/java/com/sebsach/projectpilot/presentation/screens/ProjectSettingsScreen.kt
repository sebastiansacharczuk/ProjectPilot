package com.sebsach.projectpilot.presentation.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.sebsach.projectpilot.models.UserModel
import com.sebsach.projectpilot.presentation.SearchActivity
import com.sebsach.projectpilot.utils.FirebaseUtils


/**
 * @author Sebastian Sacharczuk
 * github: https://github.com/sebastiansacharczuk
 */


@Composable
fun ProjectSettingsScreen(ids: List<String?>, currUID: String, isLeader: Boolean, projectId: String, projectName: String) {
    val context = LocalContext.current
    var showExitDialog by remember { mutableStateOf(false) }
    var selectedMember by remember { mutableStateOf<String?>(null) }
    var members by remember { mutableStateOf(listOf<UserModel>()) }
    var expanded by remember { mutableStateOf(false) }
    var clickCount by remember { mutableIntStateOf(0) }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = {
                Column {
                    Text("Do you want to leave?")
                    if (isLeader) {
                        Text(text = "Remember: If you leave as leader\n whole project will be closed")
                    }
                }
                    },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        FirebaseUtils.removeUserFromProject(projectName, projectId, currUID)
                        if (isLeader) {
                            FirebaseUtils.deleteProject(projectId)
                            (context as Activity).finish()
                        }
                              },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Leave")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showExitDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
                ) {
                    Text("Stay")
                }
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        Column {
            LaunchedEffect(key1 = ids) {
                FirebaseUtils.usersById(ids) { users ->
                    members = users
                }
            }
            Row {
                Button(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector =
                        if (expanded) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Up/Down"
                    )
                    Text("Members")
                }
                Spacer(modifier = Modifier.width(10.dp))
                if (isLeader) {
                    IconButton(
                        onClick = {
                            context.startActivity(
                                Intent(context, SearchActivity::class.java)
                                    .putExtra("project_id", projectId)
                                    .putExtra("project_name", projectName)
                            )
                        },
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    }
                }
            }
            Column {
                if (expanded) {
                    LazyColumn {
                        items(items = members) { member ->
                            Row {
                                Button(
                                    onClick = {
                                        if (selectedMember == member.uid) {
                                            clickCount++
                                        } else {
                                            clickCount = 1
                                        }
                                        selectedMember = member.uid
                                    },
                                    enabled = isLeader && member.uid != currUID
                                ) {
                                    Text(text = member.username)
                                }
                                if (selectedMember == member.uid && clickCount % 2 != 0) {
                                    IconButton(onClick = {
                                        FirebaseUtils.removeUserFromProject(projectName, projectId, member.uid)
                                        clickCount++
                                    }) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete",)
                                    }
                                    IconButton(onClick = {
                                        FirebaseUtils.changeLeader(member.uid, projectId)
                                        clickCount++
                                    }) {
                                        Icon(imageVector = ImageVector.vectorResource(com.sebsach.projectpilot.R.drawable.baseline_add_moderator_24) , contentDescription = "leadership")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Button(
            onClick = { showExitDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(text = "Leave Project")
        }
    }
}

