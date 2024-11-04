package com.mehmetbaloglu.chatterapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.RectangleShape
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
    val logoutMessage = homeViewModel.logoutMessage.collectAsState()

    val addChannelDialog = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF45ab34))
                    .clickable { addChannelDialog.value = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    tint = Color.White,
                    contentDescription = "plus icon",
                    modifier = Modifier.padding(18.dp)
                )

            }
        },
        containerColor = Color.Black,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Channels",
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Black),
                        modifier = Modifier.padding(16.dp)
                    )
                    Button(
                        onClick = {
                            homeViewModel.logOut()
                            logoutMessage.value?.let {
                                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            }
                            // Log out sonrası LoginScreen'e yönlendirme
                            navController.navigate("LogInScreen") {
                                // Tüm geçmişi temizle
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                launchSingleTop = true // Bu, LoginScreen'i yeniden oluşturmamak için kullanılır
                            }
                        },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray.copy(alpha = 0.3f),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Log Out")
                    }
                }
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Search...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(16.dp)) // Kenarları yuvarlat
                        .background(Color.White) // Arka plan rengi beyaz yap
                        .border(
                            width = 2.dp,
                            color = Color(0xFF45ab34),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "search icon"
                        )
                    }
                )
                LazyColumn() {
                    item {
                    }
                    items(channels.value) { channel ->
                        ChannelCard(
                            onClick = { navController.navigate("ChatScreen/${channel.id}") },
                            channelName = channel,
                            onDeleteClick = { homeViewModel.deleteChannel(channel.id) }
                        )
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
    onClick: (String) -> Unit = {},
    channelName: Channel,
    onDeleteClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.DarkGray)
            .clickable { onClick(channelName.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier =
            Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape)
                .background(Color(0xFF45ab34))
        ) {
            Text(
                text = channelName.name.get(0).toString().uppercase(),
                color = Color.White,
                style = TextStyle(fontSize = 35.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = channelName.name,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "delete icon",
                modifier = Modifier
                    .padding(19.dp)
                    .size(30.dp)
                    .clickable { onDeleteClick() },
                tint = Color.Red
            )
        }


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