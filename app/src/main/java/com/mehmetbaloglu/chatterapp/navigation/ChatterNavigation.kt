package com.mehmetbaloglu.chatterapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
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

    }
}