package com.example.alumnijobportal.screen

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
fun JobDetailScreen(navController: NavController, jobId: String, userEmail: String) {
    var job by remember { mutableStateOf<JobData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val db = FirebaseFirestore.getInstance()

    // Fetch job details from Firebase
    LaunchedEffect(jobId) {
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
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Job Details") })
        },
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
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text("Title: ${jobData.title}", style = MaterialTheme.typography.titleLarge)
                            Text("Company: ${jobData.companyName}", style = MaterialTheme.typography.titleMedium)
                            Text("Location: ${jobData.location}")
                            Text("Workplace Type: ${jobData.workplaceType}")
                            Text("Employment Type: ${jobData.employmentType}")
                            Text("Currency: ${jobData.currency}")
                            Text("Salary Type: ${jobData.salaryType}")
                            Text("Min Salary: ${jobData.minSalary}")
                            Text("Max Salary: ${jobData.maxSalary}")
                            Text("Description: ${jobData.description}")

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(onClick = {
                                // Apply for the job logic here
                                val application = hashMapOf(
                                    "jobId" to jobId,
                                    "userEmail" to userEmail
                                )
                                db.collection("applications").add(application)
                                    .addOnSuccessListener {
                                        Log.d("JobDetailScreen", "Application successful!")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("JobDetailScreen", "Error applying for job", e)
                                    }
                            }) {
                                Text("Apply")
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(onClick = { navController.popBackStack() }) {
                                Text("Back")
                            }
                        }
                    }
                }
            }
        }
    )
}

