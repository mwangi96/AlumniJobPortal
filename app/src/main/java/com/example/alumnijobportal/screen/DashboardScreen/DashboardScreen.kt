package com.example.alumnijobportal.screen

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavHostController,
    userName: String?,
    userEmail: String?
) {
    // Use userName and userEmail in your UI
    Scaffold(
        topBar = { TopAppBar(title = { Text("Dashboard") }) },
        content = { innerPadding -> // Accept the innerPadding parameter
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding) // Apply the innerPadding here
                    .padding(16.dp), // Add any additional padding you want
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Welcome, $userName!")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Email: $userEmail")
            }
        }
    )
}
