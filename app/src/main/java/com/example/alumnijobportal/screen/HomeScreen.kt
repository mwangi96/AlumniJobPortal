package com.example.alumnijobportal.screen

import SharedViewModel
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
import androidx.navigation.NavHostController
import com.example.alumnijobportal.utils.JobData
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun HomeScreen(navController: NavHostController, sharedViewModel: SharedViewModel) {
    val db = FirebaseFirestore.getInstance()
    var jobPosting by remember { mutableStateOf<List<JobData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        db.collection("jobs")
            .get()
            .addOnSuccessListener { result ->
                jobPosting = result.mapNotNull { document ->
                    val job = document.toObject(JobData::class.java)
                    job?.copy(id = document.id)
                }
                isLoading = false
            }
            .addOnFailureListener { exception ->
                Log.w("HomeScreen", "Error getting documents: ", exception)
                errorMessage = "Failed to load job postings."
                isLoading = false
            }
    }

    if (isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Text("Loading job postings...")
        }
    } else if (errorMessage != null) {
        Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(jobPosting) { job ->
                // Convert JobData to JobPosting before passing
                val jobPosting = JobPosting(
                    id = job.id,
                    jobTitle = job.jobTitle ?: "",
                    companyName = job.companyName ?: "",
                    location = job.location ?: "",
                    jobDescription = job.description ?: "",
                    minSalary = job.minSalary ?: "",
                    maxSalary = job.maxSalary ?: "",
                    salaryType = job.salaryType ?: "",
                    employmentType = job.employmentType ?: ""
                )
                JobCardInHomeScreen(job = jobPosting) {
                    // Use standard navigation without deep linking
                    // Set the selected job ID before navigating
                    sharedViewModel.setSelectedJobId(jobPosting.id)
                    val email = sharedViewModel.userEmail.value ?: "default@example.com"
                    navController.navigate("jobDetail/${jobPosting.id}/$email") // Keeping this for direct navigation
                }
            }
        }
    }
}

@Composable
fun JobCardInHomeScreen(
    job: JobPosting,
    onClick: () -> Unit // Keep the lambda parameter for handling click
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }, // Use the onClick lambda here
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium // Add rounded corners
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Job Title and Company Name
            Text(job.jobTitle, style = MaterialTheme.typography.titleMedium, color = Color.White)
            Text(job.companyName, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Job Details
            Text(job.location, style = MaterialTheme.typography.bodyMedium)
            Text("${job.minSalary} - ${job.maxSalary} / ${job.salaryType}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)

            // Employment Type styled like a button
            Surface(
                modifier = Modifier
                    .padding(top = 4.dp) // Add some space above
                    .padding(horizontal = 8.dp), // Add horizontal padding
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f), // Background color
                contentColor = MaterialTheme.colorScheme.onSecondary // Text color
            ) {
                Text(
                    job.employmentType,
                    modifier = Modifier.padding(8.dp), // Padding inside the button
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
