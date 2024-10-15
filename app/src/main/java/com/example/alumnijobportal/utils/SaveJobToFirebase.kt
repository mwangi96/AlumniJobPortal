package com.example.alumnijobportal.utils

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

// JobData model for job posts
data class JobData(
    val id: String = "",  // Document ID
    val jobTitle: String? = null,
    val companyName: String? = null,
    val workplaceType: String? = null,
    val employmentType: String? = null,
    val currency: String? = null,
    val salaryType: String? = null,
    val minSalary: String? = null,
    val maxSalary: String? = null,
    val location: String? = null,
    val description: String? = null
)

class SaveJobToFirebase {

    // Function to save a job posting to Firebase Firestore
    fun saveJobToFirebase(
        jobTitle: String,
        companyName: String,
        workplaceType: String,
        employmentType: String,
        currency: String,
        salaryType: String,
        minSalary: String,
        maxSalary: String,
        location: String,
        jobDescription: String,
        onSuccess: () -> Unit,  // Callback for success case
        onFailure: (Exception) -> Unit  // Callback for failure case
    ) {
        // Generate a unique job ID
        val jobId = UUID.randomUUID().toString()

        // Create a JobData object
        val jobData = JobData(
            id = jobId,
            jobTitle = jobTitle,
            companyName = companyName,
            workplaceType = workplaceType,
            employmentType = employmentType,
            currency = currency,
            salaryType = salaryType,
            minSalary = minSalary,
            maxSalary = maxSalary,
            location = location,
            description = jobDescription
        )

        // Get Firestore instance
        val db = FirebaseFirestore.getInstance()

        // Add the job data to the "jobs" collection
        db.collection("jobs")
            .document(jobId) // Use the generated job ID as the document ID
            .set(jobData)  // Use the JobData model to store job data
            .addOnSuccessListener {
                Log.d(TAG, "Job posted successfully with ID: $jobId")
                onSuccess()  // Call the success callback
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error posting job", e)
                onFailure(e)  // Call the failure callback
            }
    }

    companion object {
        private const val TAG = "SaveJobToFirebase"
    }
}
