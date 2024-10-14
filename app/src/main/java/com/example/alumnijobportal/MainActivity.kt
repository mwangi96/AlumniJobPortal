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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.alumnijobportal.nav.NavGraph
import com.example.alumnijobportal.ui.theme.AlumniJobPortalTheme
import com.example.alumnijobportal.utils.SharedViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
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
                    val navController = rememberNavController()

                    NavGraph(
                        navController = navController,
                        sharedViewModel = sharedViewModel
                    )
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun AlumniJobPortalScreenPreview() {
        AlumniJobPortalTheme {
            val navController = rememberNavController()
            NavGraph(
                navController = navController,
                sharedViewModel = SharedViewModel()
            )
        }
    }
}
