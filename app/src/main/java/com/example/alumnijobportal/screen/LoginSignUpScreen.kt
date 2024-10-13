import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.alumnijobportal.R
import com.example.alumnijobportal.ui.theme.AlumniJobPortalTheme

@Composable
fun LoginSignUpScreen(navController: NavHostController? = null) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), // Using theme background
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

            Spacer(modifier = Modifier.height(15.dp))

            // Title
            Text(
                text = "Welcome to Ist Jobs!",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Facebook Button (OutlinedButton)
            OutlinedButton(
                onClick = {
                    navController?.navigate("facebook_login")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Continue with Facebook")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Google Button (OutlinedButton)
            OutlinedButton(
                onClick = {
                    navController?.navigate("google_login")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Continue with Google")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Apple Button (OutlinedButton)
            OutlinedButton(
                onClick = {
                    navController?.navigate("apple_login")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Continue with Apple")
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

            // Continue Button (OutlinedButton)
            OutlinedButton(
                onClick = {
                    navController?.navigate("login")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Continue")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign-Up Link
            val annotatedText = buildAnnotatedString {
                append("Don't have an account? ")
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append("Sign Up")
                }
            }
            ClickableText(
                text = annotatedText,
                onClick = {
                    navController?.navigate("signup")
                }
            )
        }
    }
}
