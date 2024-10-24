package com.example.alumnijobportal.screen

import SharedViewModel
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.alumnijobportal.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.alumnijobportal.utils.UserProfile

@Composable
fun ProfileScreen(navController: NavHostController, sharedViewModel: SharedViewModel) {
    // Firebase instances
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    // State for user profile
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newPhoneNumber by remember { mutableStateOf("") }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    var deletionSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val user = auth.currentUser
        if (user != null) {
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        userProfile = UserProfile(
                            uid = user.uid,
                            name = document.getString("username") ?: "",
                            email = document.getString("email") ?: "",
                            phoneNumber = document.getString("phoneNumber") ?: "",
                            profilePictureUrl = document.getString("profilePictureUrl") ?: ""
                        )
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
            isLoading = false
        }
    }

    // Display loading indicator or profile information
    if (isLoading) {
        CircularProgressIndicator()
    } else {
        userProfile?.let { profile ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Profile picture


                Text("Name: ${profile.name}", style = MaterialTheme.typography.bodyLarge)
                Text("Email: ${profile.email}", style = MaterialTheme.typography.bodyLarge)
                Text("Phone: ${profile.phoneNumber}", style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { showUpdateDialog = true }, modifier = Modifier.fillMaxWidth()) {
                    Text("Update", color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { showDeleteConfirmationDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete Account", color = Color.White)
                }
            }

            // Update Dialog
            if (showUpdateDialog) {
                AlertDialog(
                    onDismissRequest = { showUpdateDialog = false },
                    title = { Text("Update Profile") },
                    text = {
                        Column {
                            TextField(
                                value = newName,
                                onValueChange = { newName = it },
                                label = { Text("New Name") }
                            )
                            TextField(
                                value = newPhoneNumber,
                                onValueChange = { newPhoneNumber = it },
                                label = { Text("New Phone Number") }
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            userProfile?.let { updatedProfile ->
                                val updates = hashMapOf(
                                    "username" to newName,
                                    "phoneNumber" to newPhoneNumber
                                )
                                db.collection("users").document(updatedProfile.uid).update(updates as Map<String, Any>)
                                    .addOnSuccessListener {
                                        Log.d("ProfileScreen", "Profile updated successfully.")
                                        userProfile = updatedProfile.copy(name = newName, phoneNumber = newPhoneNumber)
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.w("ProfileScreen", "Error updating profile: ", exception)
                                    }
                            }
                            showUpdateDialog = false
                        }) {
                            Text("Update")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showUpdateDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // Delete Account Confirmation Dialog
            if (showDeleteConfirmationDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirmationDialog = false },
                    title = { Text("Confirm Deletion") },
                    text = { Text("Are you sure you want to delete your account? This action cannot be undone.") },
                    confirmButton = {
                        TextButton(onClick = {
                            auth.currentUser?.delete()?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    db.collection("users").document(profile.uid).delete()
                                        .addOnSuccessListener {
                                            Log.d("ProfileScreen", "Account deleted successfully.")
                                            deletionSuccess = true
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.w("ProfileScreen", "Error deleting Firestore document: ", exception)
                                        }
                                } else {
                                    Log.w("ProfileScreen", "Error deleting account.")
                                }
                            }
                            showDeleteConfirmationDialog = false
                        }) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteConfirmationDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // Optionally show a message upon successful deletion
            if (deletionSuccess) {
                Text("Your account has been successfully deleted.", style = MaterialTheme.typography.bodyLarge)
            }
        } ?: run {
            Text("User profile not found", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
