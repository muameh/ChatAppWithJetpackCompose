package com.mehmetbaloglu.chatterapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mehmetbaloglu.chatterapp.R
import com.mehmetbaloglu.chatterapp.data.models.Message
import com.mehmetbaloglu.chatterapp.ui.viewmodels.ChatViewModel

@Composable
fun ChatScreen(navController: NavController, channelID: String) {
    val chatViewModel = hiltViewModel<ChatViewModel>()
    val messages = chatViewModel.messages.collectAsState()

    LaunchedEffect(key1 = true) { chatViewModel.listenForMessages(channelID = channelID) }


    Scaffold(
        containerColor = Color.Black
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ChatMessage(
                messages = messages.value,
                onSendMessage = { message ->
                    chatViewModel.sendMessage(channelID, message)
                }
            )
        }

    }
}

@Composable
fun ChatMessage(messages: List<Message>, onSendMessage: (String) -> Unit) {
    val hideKeyboardController = LocalSoftwareKeyboardController.current
    val msg = remember { mutableStateOf("") }


    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(messages) { message ->
                ChatBuble(message)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(8.dp)
                .background(Color.LightGray),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = msg.value,
                onValueChange = { msg.value = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text(text = "Type a Message") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        hideKeyboardController?.hide()
                    }
                )
            )
            IconButton(
                onClick = {
                    onSendMessage(msg.value)
                    msg.value = ""
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "send icon"
                )
            }

        }
    }
}


@Composable
fun ChatBuble(message: Message) {
    val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
    val bubleColor = if (isCurrentUser) Color(0xFF006837) else Color(0xFF2196F3)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        val alignment = if (!isCurrentUser) Alignment.CenterStart else Alignment.CenterEnd

        Row(
            modifier = Modifier
                .padding(4.dp)
                .align(alignment),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isCurrentUser){
                Image(
                    painter = painterResource(id = R.drawable.person_with_glasses),
                    contentDescription = "userImage",
                    modifier = Modifier.size(40.dp).clip(CircleShape),
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = message.message.trim(),
                color = Color.White,
                modifier =
                Modifier
                    .background(color = bubleColor, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp)

                )

        }

    }
}
