package com.example.alumnijobportal.nav

import LoginSignUpScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.alumnijobportal.screen.*
import com.example.alumnijobportal.screen.DashboardScreen.DashboardScreen
import com.example.alumnijobportal.utils.SharedViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
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
                userEmail = userEmail
            )
        }

    }
}

