package com.example.alumnijobportal.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.alumnijobportal.utils.JobData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

// Data class for job postings
data class JobPosting(
    val id: String = "",
    val jobTitle: String = "",
    val companyName: String = "",
    val location: String = "",
    val jobDescription: String = "",
    val minSalary: String = "",
    val maxSalary: String = "",
    val salaryType: String = "", // Add this field
    val employmentType: String = "",
    val applicantCount: Int = 0 // Add applicant count
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
                    document.toObject(JobData::class.java).copy(id = document.id)
                }.map { jobData ->
                    JobPosting(
                        id = jobData.id,
                        jobTitle = jobData.jobTitle ?: "",  // Ensure jobTitle is passed correctly
                        companyName = jobData.companyName ?: "",
                        location = jobData.location ?: "",
                        jobDescription = jobData.description ?: "",
                        minSalary = jobData.minSalary ?: "",
                        maxSalary = jobData.maxSalary ?: "",
                        salaryType = jobData.salaryType ?: "",
                        employmentType = jobData.employmentType ?: ""
                    )
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
                    JobCardInPostedScreen(job = job, navController = navController)
                }
            }
            else -> {
                Text("No jobs posted yet.")
            }
        }
    }
}

@Composable
fun JobCardInPostedScreen(job: JobPosting, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                // Navigate to ApplicantsScreen with the job ID using the standard navigation route
                navController.navigate("applicants/${job.id}")
            },
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

            // Display dynamic applicant count
            Text("Applicants: ${job.applicantCount}", style = MaterialTheme.typography.bodySmall)

            // Divider
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
