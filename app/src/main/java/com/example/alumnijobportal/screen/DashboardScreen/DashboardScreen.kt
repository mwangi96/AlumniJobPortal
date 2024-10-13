package com.example.alumnijobportal.screen.DashboardScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alumnijobportal.R
import com.google.firebase.auth.FirebaseAuth

// Define a sealed class for NavIcon
sealed class NavIcon {
    data class Vector(val icon: ImageVector) : NavIcon()
    data class Drawable(val icon: Painter) : NavIcon()
}

// Define a BottomNavItem data class
data class BottomNavItem(val label: String, val icon: NavIcon, val route: String)


@Composable
fun DashboardScreen(
    navController: NavHostController,
    userRole: String,
    userName: String,
    userEmail: String
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Welcome, $userName!", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Email: $userEmail", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Role: $userRole", style = MaterialTheme.typography.bodyMedium)

        // Add more UI elements as needed based on the user role
        if (userRole == "admin") {
            // Admin-specific UI
            Text("You have admin privileges.")
        } else {
            // Alumni-specific UI
            Text("You are logged in as an alumni.")
        }
    }
}

@Composable
fun MainScreen(userRole: String, userName: String, userEmail: String) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            if (userRole == "admin") {
                AdminDrawer()
            } else {
                AlumniDrawer()
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                CustomNavigationBar(userRole, navController)
            }
        ) { paddingValues ->
            NavHost(
                navController,
                startDestination = "dashboard/${userName}/${userEmail}/${userRole}",
                modifier = Modifier.padding(paddingValues)
            ) {
                // Define your composable routes here
                composable("dashboard/{userName}/{userEmail}/{userRole}") { backStackEntry ->
                    val userName = backStackEntry.arguments?.getString("userName") ?: ""
                    val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
                    val userRole = backStackEntry.arguments?.getString("userRole") ?: ""
                    DashboardScreen(
                        navController = navController,
                        userRole = userRole,
                        userName = userName,
                        userEmail = userEmail
                    )
                }
                composable("postedJob") { PostedJobScreen() }
                composable("postJob") { JobsPostScreen() }
                composable("chat") { ChatScreen() }
                composable("profile") { ProfileScreen() }
                composable("home") { HomeScreen() }
                composable("application") { ApplicationScreen() }
            }
        }
    }
}

    @Composable
    fun CustomNavigationBar(userType: String, navController: NavHostController) {
        val items = if (userType == "admin") {
            listOf(
                BottomNavItem(
                    "Posted Job",
                    NavIcon.Drawable(painterResource(R.drawable.ic_work)),
                    "postedJob"
                ),
                BottomNavItem("Post Job", NavIcon.Vector(Icons.Default.Add), "postJob"),
                BottomNavItem(
                    "Chat",
                    NavIcon.Drawable(painterResource(R.drawable.ic_chat)),
                    "chat"
                ),
                BottomNavItem("Profile", NavIcon.Vector(Icons.Default.Person), "profile")
            )
        } else {
            listOf(
                BottomNavItem("Home", NavIcon.Vector(Icons.Default.Home), "home"),
                BottomNavItem(
                    "Application",
                    NavIcon.Drawable(painterResource(R.drawable.ic_applications)),
                    "application"
                ),
                BottomNavItem(
                    "Chat",
                    NavIcon.Drawable(painterResource(R.drawable.ic_chat)),
                    "chat"
                ),
                BottomNavItem("Profile", NavIcon.Vector(Icons.Default.Person), "profile")
            )
        }

        val currentRoute = navController.currentDestination?.route // Get the current route

        BottomNavigation {
            items.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        when (item.icon) {
                            is NavIcon.Vector -> Icon(item.icon.icon, contentDescription = null)
                            is NavIcon.Drawable -> Image(
                                item.icon.icon,
                                contentDescription = null
                            ) // Use Image for drawable icons
                        }
                    },
                    label = { Text(item.label) },
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }


    @Composable
    fun AdminDrawer() {
        Column {
            Text("Blog")
            Text("Contact Us")
            Text("Meetings")
            Text("Logout", modifier = Modifier.clickable {
                FirebaseAuth.getInstance().signOut()
            })
        }
    }

    @Composable
    fun AlumniDrawer() {
        Column {
            Text("Build Resume")
            Text("Blog")
            Text("Profile Completion Guide")
            Text("Saved Jobs")
            Text("Meetings")
            Text("Logout", modifier = Modifier.clickable {
                FirebaseAuth.getInstance().signOut()
            })
        }
    }

    @Composable
    fun PostedJobScreen() {
        Text("Posted Job Screen")
    }

    @Composable
    fun JobsPostScreen() {
        Text("Post Job Screen")
    }

    @Composable
    fun ChatScreen() {
        Text("Chat Screen")
    }

    @Composable
    fun ProfileScreen() {
        Text("Profile Screen")
    }

    @Composable
    fun HomeScreen() {
        Text("Home Screen")
    }

    @Composable
    fun ApplicationScreen() {
        Text("Application Screen")
    }
