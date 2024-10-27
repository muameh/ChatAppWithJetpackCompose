package com.mehmetbaloglu.chatterapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mehmetbaloglu.chatterapp.data.models.Channel
import com.mehmetbaloglu.chatterapp.ui.viewmodels.HomeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val channels = homeViewModel.channels.collectAsState()
    val channelMessages = homeViewModel.channelAddedMessage.collectAsState()

    val addChannelDialog = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF6ac1f0))
                    .clickable { addChannelDialog.value = true }
            ) {
                Text(
                    text = "Add Channel",
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        },
        containerColor = Color.Black,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column {
                Text(
                    text = "Channels",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Black),
                    modifier = Modifier.padding(16.dp)
                )
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {Text("Search...")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.Red),
                    singleLine = true,
                    colors = TextFieldDefaults.colors().copy(
                        focusedTextColor = Color.Red,
                        focusedIndicatorColor = Color.Red,
                        unfocusedTextColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        focusedContainerColor = Color.White,
                    ),
                    textStyle = TextStyle(color = Color.Black),
                    trailingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = "search icon")}
                )
                LazyColumn() {
                    item {

                    }
                    items(channels.value) { channel ->
                        ChannelCard(navController, channel)
                    }
                }
            }
        }
    }

    if (addChannelDialog.value) {
        ModalBottomSheet(
            onDismissRequest = { addChannelDialog.value = false },
            sheetState = sheetState
        ) {
            AddChannelDialog {
                homeViewModel.addChannelForRealTime(it)
                addChannelDialog.value = false
            }
        }
    }
}

@Composable
fun ChannelCard(
    navController: NavController,
    channelName: Channel,
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { navController.navigate("ChatScreen/${channelName.id}") }
    ) {
        Text(
            text = channelName.name,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Bold
        )
    }
}




@Composable
fun AddChannelDialog(onAddChannel: (String) -> Unit) {
    val channelName = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add Channel")
        Spacer(modifier = Modifier.padding(16.dp))
        TextField(
            value = channelName.value,
            onValueChange = { channelName.value = it },
            label = { Text(text = "Channel Name") },
            singleLine = true
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            onClick = { onAddChannel(channelName.value) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Add")
        }
    }
}