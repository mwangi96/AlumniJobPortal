package com.example.alumnijobportal.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.alumnijobportal.R
import com.example.alumnijobportal.nav.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest

@Composable
fun SignUpScreen(navController: NavController) {

    // Initialize Firebase Auth
    auth = FirebaseAuth.getInstance()

    var username by remember { mutableStateOf("") } // New username state
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") } // New Confirm Password state
    var passwordVisible by remember { mutableStateOf(false) } // For password visibility
    var confirmPasswordVisible by remember { mutableStateOf(false) } // For confirm password visibility
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sign Up",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    modifier = Modifier
                        .size(100.dp) // Adjust the size as needed
                        .clip(RoundedCornerShape(56.dp)),
                    painter = painterResource(R.drawable.cropped_ist_logo),
                    contentDescription = "Sign Up"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Username TextField
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email TextField
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password TextField
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisible = !passwordVisible
                        }) {
                            val icon = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                            Icon(painter = painterResource(id = icon), contentDescription = null)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))


                // Confirm Password TextField
                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            confirmPasswordVisible = !confirmPasswordVisible
                        }) {
                            val icon = if (confirmPasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                            Icon(painter = painterResource(id = icon), contentDescription = null)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        signUp(username, email, password, { user ->
                            successMessage = "sign up successfully"
                            // Navigate to LoginScreen on successful sign up
                            navController.navigate(Screens.LoginScreen.route)
                        }, { error ->
                            errorMessage = error
                        })
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign Up")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display error or success message
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                successMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

private fun signUp(
    username: String, // Added username parameter
    email: String,
    password: String,
    onSuccess: (FirebaseUser?) -> Unit,
    onFailure: (String) -> Unit
) {
    // Use createUserWithEmailAndPassword for signing up new users
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser

                // Check if the user was created successfully
                user?.let {
                    // Update the user profile with the username (display name)
                    val profileUpdates = userProfileChangeRequest {
                        displayName = username // Set the display name to the username
                    }

                    user.updateProfile(profileUpdates)
                        .addOnCompleteListener { profileUpdateTask ->
                            if (profileUpdateTask.isSuccessful) {
                                // Send verification email
                                user.sendEmailVerification()
                                    .addOnCompleteListener { emailVerificationTask ->
                                        if (emailVerificationTask.isSuccessful) {
                                            onSuccess(it) // Continue with the signup process
                                        } else {
                                            onFailure("Failed to send verification email: ${emailVerificationTask.exception?.message}")
                                        }
                                    }
                            } else {
                                onFailure("Failed to update profile: ${profileUpdateTask.exception?.message}")
                            }
                        }
                }
            } else {
                onFailure(task.exception?.message ?: "Sign up failed")
            }
        }
}

