package com.example.alumnijobportal.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

data class ApplicationData(
    val jobId: String = "",
    val jobTitle: String = "",
    val companyName: String = "",
    val status: String = "" // "Withdrawn", "Application Sent", etc.
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationScreen(navController: NavHostController, userEmail: String) {
    var applications by remember { mutableStateOf<List<ApplicationData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val db = FirebaseFirestore.getInstance()

    // Fetch applications from Firestore
    LaunchedEffect(userEmail) {
        db.collection("applications")
            .whereEqualTo("userEmail", userEmail)
            .get()
            .addOnSuccessListener { result ->
                applications = result.mapNotNull { doc ->
                    doc.toObject(ApplicationData::class.java)
                }
                isLoading = false
            }
            .addOnFailureListener { exception ->
                Log.e("ApplicationScreen", "Error fetching applications", exception)
                errorMessage = "Failed to load applications: ${exception.message}"
                isLoading = false
            }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Application") }) },
        content = { paddingValues ->
            when {
                isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Loading applications...")
                    }
                }
                errorMessage != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
                    }
                }
                applications.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(applications) { application ->
                            ApplicationItem(application, onClick = {
                                // Navigate to application detail screen without deep linking
                                navController.navigate("applicationDetail") {
                                    popUpTo("applicationDetail") { inclusive = true }
                                }
                                // Optionally, pass additional data via a ViewModel or shared state
                            })
                        }
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No applications found.")
                    }
                }
            }
        }
    )
}

@Composable
fun ApplicationItem(application: ApplicationData, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(text = application.jobTitle, style = MaterialTheme.typography.titleMedium)
        Text(text = application.companyName, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val statusColor = when (application.status) {
                "Withdrawn" -> Color.Red
                "Application Sent" -> Color.Blue
                else -> Color.Gray
            }
            Text(
                text = application.status,
                color = statusColor,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
