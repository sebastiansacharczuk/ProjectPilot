package com.sebsach.projectpilot.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sebsach.projectpilot.presentation.screens.JoinRequestsScreen
import com.sebsach.projectpilot.presentation.screens.ProjectListScreen
import com.sebsach.projectpilot.presentation.screens.SettingsScreen
import com.sebsach.projectpilot.ui.theme.ProjectPilotTheme
import com.sebsach.projectpilot.utils.FirebaseUtils
import kotlinx.coroutines.launch

/**
 * @author Sebastian Sacharczuk
 * @github https://github.com/sebastiansacharczuk
 */
data class NavigationItem(
    val name: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("MainActivity: onCreate")
        setContent {
            ProjectPilotTheme {
                val UID = FirebaseUtils.currentUserId()
                val navController = rememberNavController()
                val items = listOf(
                    NavigationItem(
                        name = "Projects",
                        selectedIcon = ImageVector.vectorResource(com.sebsach.projectpilot.R.drawable.baseline_groups_24),
                        unselectedIcon = ImageVector.vectorResource(com.sebsach.projectpilot.R.drawable.outline_groups_24),
                        route = "Projects"
                    ),
                    NavigationItem(
                        name = "Join Requests",
                        selectedIcon = ImageVector.vectorResource(com.sebsach.projectpilot.R.drawable.baseline_person_add_24),
                        unselectedIcon = ImageVector.vectorResource(com.sebsach.projectpilot.R.drawable.outline_person_add_24),
                        route = "JoinRequests"
                    ),
                    NavigationItem(
                        name = "Settings",
                        selectedIcon = Icons.Filled.Settings,
                        unselectedIcon = Icons.Outlined.Settings,
                        route = "Settings"
                    ),
                )

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    var selectedItemIndex by rememberSaveable {
                        mutableIntStateOf(0)
                    }
                    ModalNavigationDrawer(
                        drawerContent = {
                            ModalDrawerSheet {
                                Spacer(modifier = Modifier.height(16.dp))
                                items.forEachIndexed { index, item ->
                                    NavigationDrawerItem(
                                        label = {
                                            Text(text = item.name)
                                        },
                                        selected = index == selectedItemIndex,
                                        onClick = {
                                            navController.navigate(item.route)
                                            selectedItemIndex = index
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (index == selectedItemIndex) {
                                                                item.selectedIcon
                                                } else item.unselectedIcon,
                                                contentDescription = item.name
                                            )
                                        },
                                        modifier = Modifier
                                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                                    )
                                }
                            }
                        },
                        drawerState = drawerState
                    ) {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = {},
                                    navigationIcon = {
                                        IconButton(onClick = {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Menu,
                                                contentDescription = "Menu"
                                            )
                                        }
                                    }
                                )
                            }
                        ) {
                               innerPadding ->
                            NavHost(navController = navController, startDestination = "Projects", modifier = Modifier.padding(innerPadding)) {
                                composable("Projects") {
                                    if (UID != null) {
                                        ProjectListScreen(UID)
                                    }
                                }
                                composable("JoinRequests") {
                                    JoinRequestsScreen()
                                }
                                composable("Settings") {
                                    SettingsScreen()
                                }
                            }
                        }
                    } // ModalNavigationDrawer
                } // Surface
            }
        }
    }
}
