package com.example.alumnijobportal.nav

import ApplyScreen
import LoginSignUpScreen
import SharedViewModel
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.alumnijobportal.screen.*
import com.example.alumnijobportal.screen.DashboardScreen.DashboardScreen


@Composable
fun NavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    // Extract userEmail safely, providing a default if it's null
    val userEmail = sharedViewModel.userEmail.value ?: ""

    NavHost(
        navController = navController,
        startDestination = Screens.LoadScreen.route // Set LoadScreen as the start screen
    ) {
        // Load screen
        composable(route = Screens.LoadScreen.route) {
            LoadScreen(navController = navController)
        }

        // Intro screen
        composable(route = Screens.IntroScreen.route) {
            IntroScreen(navController = navController)
        }

        // Login/SignUp screen
        composable(route = Screens.LoginSignUpScreen.route) {
            LoginSignUpScreen(navController = navController)
        }

        // SignUp screen
        composable(route = Screens.SignUpScreen.route) {
            SignUpScreen(navController = navController)
        }

        // Login screen
        // Login screen
        composable(route = Screens.LoginScreen.route) {
            LoginScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        // JobPostScreen route
        composable(Screens.JobPostScreen.route) {
            JobPostScreen(navController = navController,  sharedViewModel = sharedViewModel)
        }

        // HomeScreen route
        composable(route = Screens.HomeScreen.route) {
            HomeScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        // ApplicantsScreen route with sharedViewModel
        composable(route = Screens.ApplicantsScreen.route) {
            ApplicantsScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        // Applications screen
        composable(route = Screens.ApplicationScreen.route) {
            ApplicationScreen(navController = navController, userEmail = userEmail,  sharedViewModel = sharedViewModel)
        }

        // JobDetailScreen now retrieves jobId from SharedViewModel
        composable(route = Screens.JobDetailScreen.route) {
            JobDetailScreen(navController = navController, userEmail = userEmail, sharedViewModel = sharedViewModel)
        }

        // Dashboard screen with proper parameters
        composable(route = Screens.DashboardScreen.route) {
            DashboardScreen(
                navController = navController,
                userRole = sharedViewModel.userRole.value ?: "defaultRole", // Provide a default
                userName = sharedViewModel.userName.value ?: "defaultName", // Provide a default
                userEmail = userEmail,
                sharedViewModel = sharedViewModel // Pass the sharedViewModel here
            )
        }
    }
}
