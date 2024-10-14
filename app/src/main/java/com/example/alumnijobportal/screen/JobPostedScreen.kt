package com.example.alumnijobportal.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

// Data class for job postings
data class JobPosting(
    val id: String = "",
    val jobTitle: String = "",
    val companyName: String = "",
    val location: String = "",
    val jobDescription: String = ""
)

@Composable
fun JobsPostedScreen(navController: NavHostController) {
    var jobPostings by remember { mutableStateOf<List<JobPosting>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Firebase Firestore instance
    val db = FirebaseFirestore.getInstance()

    // Fetch jobs from Firestore
    LaunchedEffect(Unit) {
        db.collection("jobs")
            .get()
            .addOnSuccessListener { result ->
                val jobs = result.mapNotNull { document ->
                    document.toObject(JobPosting::class.java).copy(id = document.id)
                }
                jobPostings = jobs
                isLoading = false
            }
            .addOnFailureListener { exception ->
                errorMessage = "Failed to load jobs: ${exception.message}"
                isLoading = false
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Posted Jobs", style = MaterialTheme.typography.titleLarge)

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            errorMessage != null -> {
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            }
            jobPostings.isNotEmpty() -> {
                // List of job postings with click handling
                jobPostings.forEach { job ->
                    JobCard(job = job, navController = navController)
                }
            }
            else -> {
                Text("No jobs posted yet.")
            }
        }
    }
}

@Composable
fun JobCard(job: JobPosting, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                // Navigate to JobDetailScreen with the job ID
                navController.navigate("jobDetail/${job.id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(job.jobTitle, style = MaterialTheme.typography.titleMedium)
            Text("Company: ${job.companyName}")
            Text("Location: ${job.location}")
            Text("Description: ${job.jobDescription}")
        }
    }
}
