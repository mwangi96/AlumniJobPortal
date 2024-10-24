package com.example.alumnijobportal.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.text.style.TextAlign
import com.example.alumnijobportal.R
import com.example.alumnijobportal.ui.theme.AlumniJobPortalTheme

@Composable
fun IntroScreen(navController: NavHostController? = null) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), // Using theme background
    ) {
        // Background Image that fills the entire screen
        Image(
            painter = painterResource(id = R.drawable.intro_image1), // Replace with your image resource
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop  // Maintains image aspect ratio while filling screen
        )

        // Content overlay (Column) with gradient background for better readability
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background.copy(alpha = 0.8f)),
                        startY = 600f  // Adjust to control where the gradient starts
                    )
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            // Title Text
            Text(
                text = "Find the best jobs around you, fast.",
                fontSize = 28.sp,  // Increased font size for title
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground, // Adapting text color for readability in both themes
                modifier = Modifier.padding(bottom = 12.dp)  // Adjusted padding
            )

            // Body Text
            Text(
                text = "Build your profile, explore and apply to your favorite jobs and get contacted by employers on the go.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground, // Using onBackground color for better readability
                modifier = Modifier.padding(bottom = 32.dp),  // Adjusted padding for spacing
                textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(5.dp))

            // Next Button
            Button(
                onClick = {
                    // Handle navigation to the next screen
                    navController?.navigate("login_screen")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp),  // Button size
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),  // Button color from theme
                shape = RoundedCornerShape(24.dp)  // Rounded corners
            ) {
                Text(text = "Next", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)  // Button text color from theme
            }

            Spacer(modifier = Modifier.height(50.dp))  // Adjusted bottom spacing
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IntroScreenPreview() {
    AlumniJobPortalTheme {
        IntroScreen()
    }
}
