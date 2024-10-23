package com.mehmetbaloglu.chatterapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.mehmetbaloglu.chatterapp.data.models.LogInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor() : ViewModel() {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _logInState = MutableStateFlow<LogInState>(LogInState.Nothing)
    val logInState: StateFlow<LogInState> = _logInState.asStateFlow()

    private val _logInMessage = MutableStateFlow<String?>(null)
    val logInMessage: StateFlow<String?> = _logInMessage.asStateFlow()


    fun LogIn(email: String, password: String) {
        _logInState.value = LogInState.Loading
        // Simulate a network request
        auth.signInWithEmailAndPassword(email, password)
            .addOnFailureListener { error ->
                _logInState.value = LogInState.Error
                _logInMessage.value = error.localizedMessage
            }
            .addOnSuccessListener {
                _logInState.value = LogInState.Success
                _logInMessage.value = "Welcome, ${it.user?.displayName}"
            }
    }


}

