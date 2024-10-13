package com.example.alumnijobportal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.alumnijobportal.nav.NavGraph
import com.example.alumnijobportal.screen.DashboardScreen.MainScreen
import com.example.alumnijobportal.ui.theme.AlumniJobPortalTheme
import com.example.alumnijobportal.utils.SharedViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavHostController
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        setContent {
            AlumniJobPortalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navController = rememberNavController()

                    // Calling the NavGraph that contains the composables with screens
                    NavGraph(navController = navController, sharedViewModel = sharedViewModel)

                    // Using placeholder values
                    MainScreen(
                        userRole = "admin", // or "alumni"
                        userEmail = "user@example.com", // Placeholder email
                        userName = "User Name" // Placeholder name
                    )
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun AlumniJobPortalScreenPreview() {
        AlumniJobPortalTheme {
            MainScreen(userRole = "admin", userEmail = "user@example.com", userName = "User Name")
        }
    }
}
