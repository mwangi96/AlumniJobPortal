package com.example.alumnijobportal.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.alumnijobportal.R
import kotlinx.coroutines.delay

@Composable
fun LoadScreen(navController: NavController) {
    // Launches the effect that waits for 5 seconds before navigating
    LaunchedEffect(key1 = true) {
        delay(5000) // 5 seconds delay
        navController.navigate("intro_screen") {
            popUpTo("loading") { inclusive = true }
        }
    }

    // State for animation - starts at bottom of the screen
    var logoOffsetY by remember { mutableStateOf(500.dp) }

    // Trigger the animation after a delay
    LaunchedEffect(Unit) {
        // Animation to bring the logo to the center of the screen
        logoOffsetY = 0.dp
    }

    // Main content layout with white background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)  // Set white background for the screen
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Animate the logo's position from bottom to center
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .offset(y = animateDpAsState(
                    targetValue = logoOffsetY,
                    animationSpec = tween(durationMillis = 2000)
                ).value) // Animate the offset
        ) {
            // Circle Image (App Logo)
            Image(
                painter = painterResource(id = R.drawable.cropped_ist_logo),
                contentDescription = "App Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )

            // Circular Progress Indicator (Loading effect)
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 200.dp)
                    .size(30.dp), // Size of the progress indicator
                color = Color.Gray, // Progress indicator color for contrast
                strokeWidth = 3.dp // Thickness of the progress indicator
            )
        }
    }
}
