package com.example.alumnijobportal.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.alumnijobportal.nav.Screens

@Composable
fun AppleLoginScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Apple Login",
                style = MaterialTheme.typography.headlineMedium, // MaterialTheme from Material3
                fontWeight = FontWeight.Bold
            )

            // Add a button to navigate to the dashboard
            Button(onClick = { navController.navigate(Screens.DashboardScreen.route) }) {
                Text(text = "Continue to Dashboard")
            }
        }
    }
}
