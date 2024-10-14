package com.example.alumnijobportal.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationScreen(navController: NavController) {
    var availableStartDate by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("How would you like to apply?") })
        },
        content = { paddingValues -> // Use the paddingValues parameter
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Apply content padding here
                    .padding(16.dp), // Additional padding around the content
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Upload your Resume", style = MaterialTheme.typography.titleLarge)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        // Logic for uploading a resume
                    }) {
                        Text("Upload")
                    }
                    Button(onClick = {
                        // Logic for creating a new CV
                    }) {
                        Text("Create A New CV")
                    }
                }

                Text("Q1: When are you available to start?")
                TextField(
                    value = availableStartDate,
                    onValueChange = { availableStartDate = it },
                    placeholder = { Text("Enter your availability") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    // Logic to submit the application
                }) {
                    Text("Submit Application")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { navController.popBackStack() }) {
                    Text("Back")
                }
            }
        }
    )
}
