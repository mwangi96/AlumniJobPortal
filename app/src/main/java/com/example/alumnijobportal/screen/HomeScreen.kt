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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.alumnijobportal.utils.JobData
import com.google.firebase.firestore.FirebaseFirestore



@Composable
fun HomeScreen(navController: NavHostController, skills: List<String>, sharedViewModel: SharedViewModel) {
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
                    job?.let {
                        if (it.skills.any { skill -> skill in skills }) {
                            it.copy(id = document.id) // Ensure ID is included
                        } else null
                    }
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
                JobCard(job) {
                    // Safely navigate, using a default value or handling null
                    val email = sharedViewModel.userEmail.value ?: "default@example.com" // Fallback if null
                    navController.navigate("jobDetail/${job.id}/$email")
                }
            }
        }
    }
}
@Composable
fun JobCard(job: JobData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
//            Text(job.title, style = MaterialTheme.typography.titleMedium)
//            Text(job.description, style = MaterialTheme.typography.bodyMedium)
            // Add more job details as needed
        }
    }
}

