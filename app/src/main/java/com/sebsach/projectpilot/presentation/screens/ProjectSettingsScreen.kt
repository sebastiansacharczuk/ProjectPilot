package com.sebsach.projectpilot.presentation.screens

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sebsach.projectpilot.models.UserModel
import com.sebsach.projectpilot.presentation.SearchActivity
import com.sebsach.projectpilot.utils.FirebaseUtils


/**
 * @author Sebastian Sacharczuk
 * github: https://github.com/sebastiansacharczuk
 */


@Composable
fun ProjectSettingsScreen(ids: List<String?>, isLeader: Boolean){
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            println(ids)
            var members by remember { mutableStateOf(listOf<UserModel>()) }

            LaunchedEffect(key1 = ids) {
                FirebaseUtils.usersById(ids) { users ->
                    members = users
                }
            }
            var expanded by remember { mutableStateOf(false) }

            Column (horizontalAlignment = Alignment.CenterHorizontally){

                Button(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector =
                        if(expanded)
                            Icons.Default.KeyboardArrowUp
                        else
                            Icons.Default.KeyboardArrowDown,
                        contentDescription = "Up/Down"
                    )
                    Text("Members")
                }

                if (expanded) {
                    LazyColumn(
                        Modifier.height(200.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item{
                            if (isLeader) {
                                Button(
                                    onClick = {
                                              context.startActivity(Intent(context, SearchActivity::class.java))
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                                ) {
                                    Text(text = "Add new member")
                                }
                            }
                        }
                        items(items = members) { member ->
                            Button(
                                onClick = {},
                                enabled = isLeader
                            ) {
                                Text(text = member.username)
                            }
                        }
                    }
                }
            }
        }
        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(text = "Leave Project")
        }
    }
}

