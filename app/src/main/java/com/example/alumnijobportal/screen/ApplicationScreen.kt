package com.example.alumnijobportal.screen

import SharedViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

data class ApplicationData(
    val jobId: String = "",
    val jobTitle: String = "",
    val companyName: String = "",
    val status: String = "" // "Withdrawn", "Application Sent", etc.
)
@Composable
fun ApplicationScreen(
    sharedViewModel: SharedViewModel,
    navController: NavHostController,
    userEmail: String
) {
    // Observe the applications LiveData
    val applications by sharedViewModel.applications.observeAsState(emptyList())

    // Fetch the applications when the userEmail changes or screen loads
    LaunchedEffect(userEmail) {
        sharedViewModel.fetchApplications(userEmail)
    }

    Scaffold(content = { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp), // Apply both scaffold padding and extra padding
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(applications) { application ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = application.jobTitle,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = application.companyName,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = application.status,
                            style = MaterialTheme.typography.bodySmall,
                            color = when (application.status) {
                                "Withdrawn" -> Color.Red
                                "Application Sent" -> Color.Blue
                                else -> Color.Gray
                            }
                        )
                    }
                }
            }
        }
    })
}
