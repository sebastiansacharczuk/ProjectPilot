package com.sebsach.projectpilot.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.sebsach.projectpilot.R
import com.sebsach.projectpilot.utils.AndroidUtils
import com.sebsach.projectpilot.ui.theme.ProjectPilotTheme
import com.sebsach.projectpilot.utils.FirebaseUtils

/**
 * @author Sebastian Sacharczuk
 * @github https://github.com/sebastiansacharczuk
 */
class SignInActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            if(FirebaseUtils.isLoggedIn()){
                startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                finish()
            }

        }


        println("SignIn: onCreate")
        setContent {
            ProjectPilotTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    auth = FirebaseAuth.getInstance()

                    val focusManager = LocalFocusManager.current
                    val context = LocalContext.current
                    var inputNickname by remember { (mutableStateOf("")) }
                    var inputPassword by remember { (mutableStateOf("")) }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){
                        Icon(painter = painterResource(id = R.drawable.baseline_groups_160), contentDescription = "baseline_groups_160")
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                            value = inputNickname,
                            onValueChange = { inputNickname = it },
                            label = { Text("nickname") },
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
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password)

                        )
                        Button(onClick = {
                            if(inputNickname.isEmpty() || inputPassword.isEmpty()){
                                AndroidUtils.makeToast(this@SignInActivity, "Enter email and password")
                            }
                            else{
                                signInUser(inputNickname, inputPassword)
                            }
                        }) {
                            Text(text = "Sign In")
                        }
                        Button(onClick = {
                            startActivity(Intent(context, SignUpActivity::class.java))
                        }) {
                            Text(text = "Don't have an account? Sign Up")
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
        println("SignIn: onStart")

    }

    override fun onPause() {
        super.onPause()
        println("SignIn: onPause")

    }
    override fun onDestroy() {
        super.onDestroy()
        println("SignIn: onDestroy")
    }

    private fun signInUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this@SignInActivity){ task ->
                if(task.isSuccessful){
                    //Toast.makeText(this@SignInActivity, "SignIn Successfully", Toast.LENGTH_SHORT).show()
                    AndroidUtils.makeToast(this@SignInActivity, "SignIn Successfully")
                    startActivity(
                        Intent(this@SignInActivity, MainActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                } else {
                    //Toast.makeText(this@SignInActivity, "SignIn Failed", Toast.LENGTH_SHORT).show()
                    AndroidUtils.makeToast(this@SignInActivity, "SignIn Failed")
                }
            }

    }

}
