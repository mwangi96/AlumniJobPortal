package com.example.alumnijobportal.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.alumnijobportal.R

@Composable
fun LoginSignUpScreen(navController: NavHostController? = null) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.cropped_ist_logo), // Replace with your logo resource
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = "Welcome to Ist Jobs!",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Facebook Button
            Button(
                onClick = {
                    navController?.let {
                        it.navigate("facebook_login")
                    } // Navigate only if navController is not null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2))
            ) {
                Text(text = "Continue with Facebook", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Google Button
            Button(
                onClick = {
                    navController?.let {
                        it.navigate("google_login")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDB4437))
            ) {
                Text(text = "Continue with Google", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Apple Button
            Button(
                onClick = {
                    navController?.let {
                        it.navigate("apple_login")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000000))
            ) {
                Text(text = "Continue with Apple", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider with "or"
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    color = Color.Gray,
                    thickness = 1.dp
                )
                Text("or")
                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    color = Color.Gray,
                    thickness = 1.dp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Continue Button (Login Screen)
            Button(
                onClick = {
                    navController?.let {
                        it.navigate("login")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Use your app's primary color
            ) {
                Text(text = "Continue", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign-Up Link
            val annotatedText = buildAnnotatedString {
                append("Don't have an account? ")
                withStyle(style = SpanStyle(color = Color.Blue)) {
                    append("Sign Up")
                }
            }
            ClickableText(
                text = annotatedText,
                onClick = {
                    navController?.let {
                        it.navigate("signup")
                    }
                }
            )
        }
    }
}
