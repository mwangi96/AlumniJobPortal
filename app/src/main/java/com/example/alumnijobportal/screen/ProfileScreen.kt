package com.example.alumnijobportal.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.alumnijobportal.utils.UserProfile

@Composable
fun ProfileScreen(navController: NavHostController) {
    // Firebase instances
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    // State for user profile
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch user profile data
    LaunchedEffect(Unit) {
        val user = auth.currentUser
        if (user != null) {
            // Retrieve user data from Firestore
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        userProfile = document.toObject(UserProfile::class.java)
                    } else {
                        Log.d("ProfileScreen", "No such document")
                    }
                    isLoading = false
                }
                .addOnFailureListener { exception ->
                    Log.w("ProfileScreen", "Error getting documents: ", exception)
                    isLoading = false
                }
        } else {
            // User is not logged in
            isLoading = false
        }
    }

    // Display loading indicator or profile information
    if (isLoading) {
        CircularProgressIndicator()
    } else {
        userProfile?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text("Profile", style = MaterialTheme.typography.titleLarge)
                Text("Name: ${it.name}")
                Text("Email: ${it.email}")
                Text("Phone: ${it.phoneNumber}") // Displaying phone number
                // Display other user information as needed
            }
        } ?: run {
            Text("User profile not found")
        }
    }
}
