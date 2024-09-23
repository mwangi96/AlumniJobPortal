package com.example.alumnijobportal.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.alumnijobportal.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


lateinit var auth: FirebaseAuth

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    // Initialize Firebase Auth
    auth = FirebaseAuth.getInstance()

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
                    text = "Login",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(56.dp)),
                    painter = painterResource(R.drawable.cropped_ist_logo),
                    contentDescription = "Login"
                )

                Spacer(modifier = Modifier.height(16.dp))

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

                // Forgot Password
                TextButton(onClick = {
                    if (email.isNotEmpty()) {
                        forgotPassword(email, context) // Trigger forgot password function
                    } else {
                        Toast.makeText(context, "Please enter your email first", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text(text = "Forgot Password?", color = MaterialTheme.colorScheme.primary)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Handle login logic (this could be a form where the user enters email and password)
                Button(
                    onClick = {
                        if (password == confirmPassword) {
                            signIn(email, password, { user ->
                                successMessage = "login successfully"
                                // Navigate to DashboardScreen on successful sign in
                                val userName = user?.displayName ?: "Unknown User"
                                val userEmail = user?.email ?: "Unknown Email"
                                navController.navigate("dashboard_screen/$userName/$userEmail")
                            }, { error ->
                                errorMessage = error
                            })
                        } else {
                            errorMessage = "Passwords do not match"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display success or error message
                errorMessage?.let {
                    Text(text = it, color = Color.Red)
                }

                successMessage?.let {
                    Text(text = it, color = Color.Green)
                }
            }
        }
    }
}

private fun forgotPassword(email: String, context: Context) {
    auth.sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Password reset email sent", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
}

private fun signIn(email: String, password: String, onSuccess: (FirebaseUser?) -> Unit, onFailure: (String) -> Unit) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null && user.isEmailVerified) {
                    onSuccess(user)
                } else {
                    auth.signOut()
                    onFailure("Please verify your email first")
                }
            } else {
                onFailure(task.exception?.message ?: "Sign in failed")
            }
        }
}
