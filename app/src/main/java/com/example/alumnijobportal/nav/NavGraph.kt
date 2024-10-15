package com.example.alumnijobportal.nav

import LoginSignUpScreen
import SharedViewModel
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.alumnijobportal.screen.*
import com.example.alumnijobportal.screen.DashboardScreen.DashboardScreen


@Composable
fun NavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
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


        // Facebook Login screen
        composable(route = Screens.FacebookLoginScreen.route) {
            FacebookLoginScreen(navController = navController)
        }

        // Google Login screen
        composable(route = Screens.GoogleLoginScreen.route) {
            GoogleLoginScreen(navController = navController)
        }

        // Apple Login screen
        composable(route = Screens.AppleLoginScreen.route) {
            AppleLoginScreen(navController = navController)
        }

        // JobPostScreen route
        composable(Screens.JobPostScreen.route) {
            JobPostScreen(navController = navController)
        }

        // Home screen without skills list
        composable(route = Screens.HomeScreen.route) {
            HomeScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        // ApplicantsScreen route without parameters
        composable(route = Screens.ApplicantsScreen.route) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId")
            ApplicantsScreen(jobId = jobId, navController = navController)
        }

        // Applications screen
        composable(route = Screens.ApplicationScreen.route) {
            ApplicationScreen(navController = navController, userEmail = userEmail)
        }

        // JobDetailScreen route using jobId and email
        composable(route = Screens.JobDetailScreen.route) {
            val jobId = sharedViewModel.selectedJobId.value ?: "" // Provide a default if null
            JobDetailScreen(navController = navController, jobId = jobId, userEmail = userEmail)
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
