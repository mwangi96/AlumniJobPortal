package com.example.alumnijobportal.nav

import LoginSignUpScreen
import SharedViewModel
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.alumnijobportal.screen.*
import com.example.alumnijobportal.screen.DashboardScreen.DashboardScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel // Pass the ViewModel
) {

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
        composable(route = Screens.LoginScreen.route) {
            LoginScreen(navController = navController)
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

        // Home screen with skills list
        composable(route = Screens.HomeScreen.route) {
            // Replace with the actual skills list or a variable
            val skills = listOf("Skill1", "Skill2", "Skill3") // Example skills
            HomeScreen(navController = navController, skills = skills, sharedViewModel = sharedViewModel)
        }



        // Dashboard screen
        composable(
            route = Screens.DashboardScreen.route + "/{userName}/{userEmail}/{userRole}",
            arguments = listOf(
                navArgument("userName") { type = NavType.StringType },
                navArgument("userEmail") { type = NavType.StringType },
                navArgument("userRole") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: "defaultUser"
            val userEmail = backStackEntry.arguments?.getString("userEmail") ?: "defaultEmail"
            val userRole = backStackEntry.arguments?.getString("userRole") ?: "alumni"

            DashboardScreen(
                navController = navController,
                userRole = userRole,
                userName = userName,
                userEmail = userEmail,
                sharedViewModel = sharedViewModel // Pass the ViewModel here
            )
        }
    }
}
