package com.example.alumnijobportal.utils

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

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
        skills: List<String>,
        screeningQuestions: List<String>,
        onSuccess: () -> Unit,  // Callback for success case
        onFailure: (Exception) -> Unit  // Callback for failure case
    ) {
        // Create a map for the job data
        val jobData = mapOf(
            "jobTitle" to jobTitle,
            "companyName" to companyName,
            "workplaceType" to workplaceType,
            "employmentType" to employmentType,
            "currency" to currency,
            "salaryType" to salaryType,
            "minSalary" to minSalary,
            "maxSalary" to maxSalary,
            "location" to location,
            "jobDescription" to jobDescription,
            "skills" to skills,
            "screeningQuestions" to screeningQuestions
        )

        // Get Firestore instance
        val db = FirebaseFirestore.getInstance()

        // Add the job data to the "jobs" collection
        db.collection("jobs")
            .add(jobData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                // Call the success callback
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                // Call the failure callback
                onFailure(e)
            }
    }

    companion object {
        private const val TAG = "SaveJobToFirebase"
    }
}
