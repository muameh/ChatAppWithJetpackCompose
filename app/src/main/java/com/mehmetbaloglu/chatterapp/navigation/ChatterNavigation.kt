package com.mehmetbaloglu.chatterapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.mehmetbaloglu.chatterapp.ui.screens.ChatScreen
import com.mehmetbaloglu.chatterapp.ui.screens.HomeScreen
import com.mehmetbaloglu.chatterapp.ui.screens.LogInScreen
import com.mehmetbaloglu.chatterapp.ui.screens.SignUpScreen

@Composable
fun ChatterNavigation(){
    val navController: NavHostController = rememberNavController()

    val currentUser = FirebaseAuth.getInstance().currentUser
    val startDestination = if (currentUser != null) ChatterScreens.HomeScreen.name else ChatterScreens.LogInScreen.name

    NavHost(navController = navController, startDestination = startDestination) {

        composable(route= ChatterScreens.LogInScreen.name){
            LogInScreen(navController)
        }

        composable(route= ChatterScreens.SignUpScreen.name){
            SignUpScreen(navController)
        }

        composable(route= ChatterScreens.HomeScreen.name){
            HomeScreen(navController = navController)
        }

        composable(
            route= ChatterScreens.ChatScreen.name + "/{channelID}",
            arguments = listOf(
                navArgument(name = "channelID", builder = {type = NavType.StringType} )
            )
        ){
            val channelID = it.arguments?.getString("channelID") ?: ""
            ChatScreen(navController = navController, channelID = channelID)
        }




    }
}