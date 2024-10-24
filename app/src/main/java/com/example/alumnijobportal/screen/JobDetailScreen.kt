package com.example.alumnijobportal.screen

import SharedViewModel
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.alumnijobportal.utils.JobData
import com.google.firebase.firestore.FirebaseFirestore


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailScreen(navController: NavController, userEmail: String, sharedViewModel: SharedViewModel) {
    // Get the job ID from the SharedViewModel
    val jobId = sharedViewModel.selectedJobId.value ?: return // Exit if no jobId
    var job by remember { mutableStateOf<JobData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var hasApplied by remember { mutableStateOf(false) }
    val db = FirebaseFirestore.getInstance()

    // Fetch job details and check if the user has applied
    LaunchedEffect(jobId) {
        // Fetch job details
        db.collection("jobs").document(jobId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    job = document.toObject(JobData::class.java)?.copy(id = document.id)
                } else {
                    errorMessage = "Job not found."
                }
                isLoading = false
            }
            .addOnFailureListener { exception ->
                errorMessage = "Failed to load job details: ${exception.message}"
                isLoading = false
            }

        // Check if the user has already applied for this job
        sharedViewModel.hasUserAppliedForJob(jobId, userEmail) { result ->
            hasApplied = result
        }
    }

    Scaffold(
        content = {
            when {
                isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Loading job details...")
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
                job != null -> {
                    job?.let { jobData ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Display job information
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    jobData.jobTitle?.let {
                                        Text(
                                            text = it,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }
                                    jobData.companyName?.let {
                                        Text(
                                            text = it,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    jobData.location?.let {
                                        Text(it)
                                    }
                                    jobData.minSalary?.let { minSal ->
                                        jobData.maxSalary?.let { maxSal ->
                                            jobData.currency?.let { currency ->
                                                jobData.salaryType?.let { salaryType ->
                                                    Text("$minSal - $maxSal $currency / $salaryType")
                                                }
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Posted 17 hours ago", style = MaterialTheme.typography.bodySmall)
                                }
                            }

                            // Job Description Section
                            jobData.description?.let {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text("Job Description", style = MaterialTheme.typography.titleMedium)
                                        Text(it, style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }

                            // Job Summary Section
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    jobData.employmentType?.let {
                                        Text("Employment: $it")
                                    }
                                    jobData.workplaceType?.let {
                                        Text("Workplace: $it")
                                    }
                                    jobData.location?.let {
                                        Text("Location: $it")
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Apply Button or "Already Applied" Text
                            if (hasApplied) {
                                // If user has already applied, show "Already Applied"
                                Button(
                                    onClick = { /* No action needed */ },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = false // Disable the button
                                ) {
                                    Text("Already Applied")
                                }
                            } else {
                                // Apply Button
                                Button(
                                    onClick = {
                                        // Navigate to the ApplyScreen, passing the jobId
                                        navController.navigate("apply")
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Apply")
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Back Button
                            Button(
                                onClick = { navController.popBackStack() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Back")
                            }
                        }
                    }
                }
            }
        }
    )
}
