package com.mehmetbaloglu.chatterapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.mehmetbaloglu.chatterapp.data.models.LogInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _signUpState = MutableStateFlow<LogInState>(LogInState.Nothing)
    val signUpState: StateFlow<LogInState> = _signUpState.asStateFlow()

    private val _signUpMessage = MutableStateFlow<String?>(null)
    val signUpMessage: StateFlow<String?> = _signUpMessage.asStateFlow()


    fun signUpWithEmail(userName: String, email: String, password: String) {
        _signUpState.value = LogInState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.user.let { user ->
                        user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(userName).build())
                            ?.addOnCompleteListener { _signUpState.value = LogInState.Success }
                        _signUpMessage.value = task.result.toString()
                    }
                }
            }.addOnFailureListener { error ->
                _signUpState.value = LogInState.Error
                _signUpMessage.value = error.localizedMessage
                Log.d("signUpError", error.toString())
            }
    }

}