package com.sebsach.projectpilot.presentation


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.sebsach.projectpilot.models.ProjectModel
import com.sebsach.projectpilot.presentation.screens.ChatScreen
import com.sebsach.projectpilot.presentation.screens.ProjectSettingsScreen
import com.sebsach.projectpilot.presentation.screens.TasksScreen
import com.sebsach.projectpilot.ui.theme.ProjectPilotTheme
import com.sebsach.projectpilot.utils.AndroidUtils
import com.sebsach.projectpilot.utils.FirebaseUtils


/**
 * @author Sebastian Sacharczuk
 * github: https://github.com/sebastiansacharczuk
 */


data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@OptIn(ExperimentalMaterial3Api::class)
class ProjectActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectPilotTheme {
                val projectModel = remember { mutableStateOf<ProjectModel?>(null) }
                val currUID = FirebaseUtils.currentUserId()
                LaunchedEffect(Unit) {
                    val receivedId = intent.getStringExtra("id")
                    if (receivedId != null) {
                        FirebaseUtils.getProjectDetails(
                            id = receivedId,
                            onSuccess = { project ->
                                projectModel.value = project
                            },
                            onFailure = {
                                AndroidUtils.makeToast(
                                    this@ProjectActivity,
                                    "Loading project failed"
                                )
                                finish()
                            }
                        )
                    }
                }

                if (projectModel.value == null) {
                    LinearProgressIndicator()
                } else {
                    println(projectModel)

                    val navController = rememberNavController()
                    val items = listOf(
                        BottomNavigationItem(
                            title = "Tasks",
                            selectedIcon = Icons.Filled.DateRange,
                            unselectedIcon = Icons.Outlined.DateRange,
                        ),
                        BottomNavigationItem(
                            title = "Chat",
                            selectedIcon = Icons.Filled.Email,
                            unselectedIcon = Icons.Outlined.Email,
                        ),
                        BottomNavigationItem(
                            title = "Settings",
                            selectedIcon = Icons.Filled.Settings,
                            unselectedIcon = Icons.Outlined.Settings,
                        )
                    )
                    val isLeader = (projectModel.value!!.leader == currUID)
                    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
                    var topBarTitle by remember { mutableStateOf(items[0].title) }
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text(text = topBarTitle)},
                                navigationIcon = {
                                    IconButton(onClick = {
                                        finish()
                                    }) {
                                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                                    }
                                }
                            )
                        },
                        bottomBar = {
                            NavigationBar(
                                containerColor = Color.Transparent
                            ) {
                                items.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {
                                            topBarTitle = item.title
                                            selectedItemIndex = index
                                            navController.navigate(item.title)
                                        },
                                        label = {
                                            Text(text = item.title)
                                        },
                                        alwaysShowLabel = false,
                                        icon = {
                                            Icon(
                                                imageVector = if (index == selectedItemIndex) {
                                                    item.selectedIcon
                                                } else item.unselectedIcon,
                                                contentDescription = item.title
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    ) {
                            innerPadding ->
                        NavHost(navController = navController, startDestination = items[0].title, modifier = Modifier.padding(innerPadding)) {
                            composable(items[0].title) {
                                if (currUID != null) {
                                    TasksScreen(
                                        projectId = projectModel.value!!.id,
                                        uid = currUID,
                                        tasks = projectModel.value!!.tasks.map { it.toMutableMap() },
                                        isLeader = isLeader
                                    )
                                }
                            }
                            composable(items[1].title) {
                                FirebaseUtils.currentUserEmail()?.let { it1 ->
                                    ChatScreen(
                                        projectId = projectModel.value!!.id,
                                        username = it1,
                                        chat = projectModel.value!!.chat
                                    )
                                }
                            }
                            composable(items[2].title) {
                                if (currUID != null) {
                                    ProjectSettingsScreen(
                                        ids = projectModel.value!!.members,
                                        currUID = currUID,
                                        isLeader = isLeader,
                                        projectId = projectModel.value!!.id,
                                        projectName = projectModel.value!!.name)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
