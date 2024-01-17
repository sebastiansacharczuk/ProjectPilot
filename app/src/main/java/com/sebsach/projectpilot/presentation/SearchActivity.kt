package com.sebsach.projectpilot.presentation


import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import com.sebsach.projectpilot.model.UserModel
import com.sebsach.projectpilot.ui.theme.ProjectPilotTheme
import com.sebsach.projectpilot.utils.FirebaseUtils


/**
 * @author Sebastian Sacharczuk
 * @github https://github.com/sebastiansacharczuk
 */

class SearchActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("Search: onCreate")
        setContent {
            ProjectPilotTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val focusManager = LocalFocusManager.current
                    var inputSearch by remember{ mutableStateOf("") }
                    val users = remember { mutableStateOf(listOf<UserModel>()) }
                    Column {
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            IconButton(
                                onClick = { finish() },
                                modifier = Modifier.padding(start = 6.dp, bottom = 10.dp)
                            ) { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Search") }

                            TextField(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 6.dp, end = 6.dp, bottom = 10.dp),
                                value = inputSearch,
                                onValueChange = {inputSearch = it},
                                label = { Text("Search") },
                                shape = CircleShape,
                                colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password)
                            )

                            IconButton(
                                onClick = {
                                    if (inputSearch.isEmpty()) {
                                        FirebaseUtils.allUsersCollectionReference()
                                            .get()
                                            .addOnSuccessListener { result ->
                                                users.value = result.documents.mapNotNull { it.toObject(UserModel::class.java) }
                                            }.addOnFailureListener { exception ->
                                                // Handle any errors here.
                                            }
                                    } else {
                                        val startAt = inputSearch
                                        val endAt = "$inputSearch\uf8ff"

                                        FirebaseUtils.allUsersCollectionReference()
                                            .orderBy("username")
                                            .startAt(startAt)
                                            .endAt(endAt)
                                            .get()
                                            .addOnSuccessListener { result ->
                                                users.value = result.documents.mapNotNull { it.toObject(UserModel::class.java) }
                                            }.addOnFailureListener { exception ->
                                                // Handle any errors here.
                                                println(exception.message)
                                            }
                                    }
                                },
                                modifier = Modifier.padding(end = 6.dp, bottom = 10.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                            }


                        }

                        LazyColumn(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            items(users.value) { user ->
                                Button(
                                    onClick = { /*TODO*/ },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFEBEBEB),
                                        contentColor = Color.Black
                                    )
                                ) {
                                    Text(text = user.username + if(user.uid == FirebaseUtils.currentUserId()) " (me)" else "")
                                }
                            }
                        }

                    }

                }
            }
        }
    }
}

