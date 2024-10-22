package com.mehmetbaloglu.chatterapp.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mehmetbaloglu.chatterapp.R
import com.mehmetbaloglu.chatterapp.data.models.LogInState
import com.mehmetbaloglu.chatterapp.navigation.ChatterScreens
import com.mehmetbaloglu.chatterapp.ui.viewmodels.LogInViewModel

@Composable
fun LogInScreen(navController: NavController = rememberNavController()) {
    val logInViewModel: LogInViewModel = hiltViewModel()
    val context = LocalContext.current

    val logInState = logInViewModel.logInState.collectAsState()
    val logInMessage = logInViewModel.logInMessage.collectAsState()

    var emailState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }


    LaunchedEffect(key1 = logInState.value) {
        when (logInState.value) {
            LogInState.Success -> {
                navController.navigate(ChatterScreens.HomeScreen.name)
            }
            LogInState.Error -> {
                Toast.makeText(context, logInMessage.value, Toast.LENGTH_SHORT).show()
            }
            LogInState.Loading -> {}
            LogInState.Nothing -> {}
        }
    }

    Scaffold { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(top = 0.dp, start = 32.dp, end = 32.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chat_logo),
                    contentDescription = "app logo",
                    modifier = Modifier.size(150.dp),
                )
                OutlinedTextField(
                    value = emailState,
                    onValueChange = { emailState = it },
                    label = { Text(text = "Email") },
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF6ac1f0))
                )
                OutlinedTextField(
                    value = passwordState,
                    onValueChange = { passwordState = it },
                    label = { Text(text = "Password") },
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF6ac1f0))
                )

                if (logInState.value == LogInState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                } else {
                    Button(
                        onClick = {
                            logInViewModel.LogIn(emailState, passwordState)
                            Log.d("login", "email: $emailState, password: $passwordState")
                        },
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6ac1f0)),
                        enabled = emailState.isNotEmpty() &&
                                passwordState.isNotEmpty() &&
                                (logInState.value == LogInState.Nothing || logInState.value == LogInState.Error)
                    ) {
                        Text(text = "Log In")
                    }
                    TextButton(onClick = { navController.navigate(ChatterScreens.SignUpScreen.name) }) {
                        Text(text = "Don't have an account? Sign Up")
                    }
                }

            }

        }
    }

}