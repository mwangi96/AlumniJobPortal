package com.example.alumnijobportal.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

// Data class for applicants
data class Applicant(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val jobId: String = "",
    val location: String = "N/A", // Add location field
    val applicationStatus: String = "Application Received" // Add status field
)

@Composable
fun ApplicantsScreen(jobId: String?, navController: NavHostController) {
    var applicants by remember { mutableStateOf<List<Applicant>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Firebase Firestore instance
    val db = FirebaseFirestore.getInstance()

    // Fetch applicants from Firestore
    LaunchedEffect(jobId) {
        if (jobId != null) {
            db.collection("applications")
                .whereEqualTo("jobId", jobId)
                .get()
                .addOnSuccessListener { result ->
                    applicants = result.mapNotNull { document ->
                        document.toObject(Applicant::class.java).copy(id = document.id)
                    }
                    isLoading = false
                }
                .addOnFailureListener { exception ->
                    errorMessage = "Failed to load applicants: ${exception.message}"
                    isLoading = false
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Search bar
        OutlinedTextField(
            value = "", // You can implement search functionality here
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search for a Person") }
        )

        // Applicants header
        Text(
            text = "Job Applicants",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            errorMessage != null -> {
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            }
            applicants.isNotEmpty() -> {
                LazyColumn {
                    items(applicants) { applicant ->
                        ApplicantCard(applicant)
                    }
                }
            }
            else -> {
                Text("No applicants yet.")
            }
        }
    }
}

@Composable
fun ApplicantCard(applicant: Applicant) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Increased spacing for clarity
        ) {
            // Applicant name with icon
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person, // Example icon
                    contentDescription = "Applicant Icon"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(applicant.name, style = MaterialTheme.typography.titleMedium)
            }

            // Application status button
            OutlinedButton(onClick = { /* Handle button click */ }) {
                Text(applicant.applicationStatus)
            }

            // Location
            Text("Location: ${applicant.location}", style = MaterialTheme.typography.bodyMedium)

            // Message button
            Button(
                onClick = { /* Handle messaging */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Message")
            }
        }
    }
}
