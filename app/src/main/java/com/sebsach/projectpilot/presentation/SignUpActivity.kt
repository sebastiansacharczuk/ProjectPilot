package com.sebsach.projectpilot.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sebsach.projectpilot.R
import com.sebsach.projectpilot.utils.AndroidUtils
import com.sebsach.projectpilot.utils.FirebaseUtils
import com.sebsach.projectpilot.models.UserModel
import com.sebsach.projectpilot.ui.theme.ProjectPilotTheme
/**
 * @author Sebastian Sacharczuk
 * @github https://github.com/sebastiansacharczuk
 */

class SignUpActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("SignUp: onCreate")

        setContent {
            ProjectPilotTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    auth = FirebaseAuth.getInstance()

                    var inputEmail by remember { (mutableStateOf("")) }
                    var inputPassword by remember { (mutableStateOf("")) }
                    var inputConfirmPassword by remember { (mutableStateOf("")) }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        ){
                        Icon(painter = painterResource(id = R.drawable.baseline_groups_160), contentDescription = "baseline_groups_160")
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                            value = inputEmail,
                            onValueChange = { inputEmail = it },
                            label = { Text("email") },
                            singleLine = true,
                            shape = CircleShape,
                            colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)

                        )
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                            value = inputPassword,
                            onValueChange = {inputPassword = it},
                            label = { Text("password") },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            shape = CircleShape,
                            colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password)

                        )
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                            value = inputConfirmPassword,
                            onValueChange = { inputConfirmPassword = it },
                            label = { Text("confirm password") },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            shape = CircleShape,
                            colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password)

                        )
                        Button(onClick = {
                            if(inputPassword != inputConfirmPassword){
                                Toast.makeText(this@SignUpActivity, "Password fields does not match", Toast.LENGTH_SHORT).show()
                            }
                            else if(inputEmail.isNotEmpty()
                                && inputPassword.isNotEmpty()
                                && inputConfirmPassword.isNotEmpty()
                            ){
                                signUpUser(inputEmail, inputConfirmPassword, auth)
                            } else {
                                Toast.makeText(this@SignUpActivity, "Fill all fields", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Text(text = "Create an account")
                        }
                        Button(onClick = {
                            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                        }) {
                            Text(text = "Back to signing in")
                        }

                    }
                    Box(contentAlignment = Alignment.BottomCenter
                    ){
                        Text(text = "Developed by Sebastian Sacharczuk", fontSize = 12.sp)
                    }
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        println("SignUp: onStart")

    }
    override fun onPause() {
        super.onPause()
        println("SignUp: onPause")

    }
    override fun onDestroy() {
        super.onDestroy()
        println("SignUp: onDestory")
    }

    private fun signUpUser(inputEmail: String, password: String, auth: FirebaseAuth) {
        auth.createUserWithEmailAndPassword(inputEmail, password)
            .addOnCompleteListener(this@SignUpActivity) { task ->
                if (task.isSuccessful) {
                    setUsername(inputEmail) { success ->
                        if (success) {
                            AndroidUtils.makeToast(this@SignUpActivity, "Signup Successfully")
                            //startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@SignUpActivity, "Failed to set username", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@SignUpActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun setUsername(username: String, callback: (Boolean) -> Unit) {
        FirebaseUtils.currentUserId()
            ?.let { UserModel(username, it) }?.let {
                FirebaseUtils.currentUserId()?.let { it1 ->
                    FirebaseFirestore.getInstance().collection("users").document(it1).set(it)
                        .addOnCompleteListener(this@SignUpActivity) { task ->
                            callback(task.isSuccessful)
                        }
                }
            }
    }
}

