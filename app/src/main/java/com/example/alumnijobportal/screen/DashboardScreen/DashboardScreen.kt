package com.example.alumnijobportal.screen.DashboardScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.alumnijobportal.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

// Define a sealed class for NavIcon
sealed class NavIcon {
    data class Vector(val icon: ImageVector) : NavIcon()
    data class Drawable(val icon: Painter) : NavIcon()
}

// Define a BottomNavItem data class
data class BottomNavItem(val label: String, val icon: NavIcon, val route: String)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavHostController,
    userRole: String,
    userName: String,
    userEmail: String
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    var isDrawerExpanded by remember { mutableStateOf(false) }

    // Mutable state to hold the title of the top bar
    var title by remember { mutableStateOf("Dashboard") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            if (userRole == "admin") {
                AdminDrawer(isExpanded = isDrawerExpanded) { isDrawerExpanded = !isDrawerExpanded }
            } else {
                AlumniDrawer(isExpanded = isDrawerExpanded) { isDrawerExpanded = !isDrawerExpanded }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(title) },  // Use the title state here
                    actions = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                                isDrawerExpanded = !isDrawerExpanded // Toggle the drawer state
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                )
            },
            bottomBar = {
                CustomNavigationBar(userRole, navController) { selectedTitle ->
                    title = selectedTitle  // Update the title based on selected item
                }
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                Spacer(modifier = Modifier.height(16.dp)) // Space at the top

                Text(text = "This is the main content area.")

                Spacer(modifier = Modifier.weight(1f)) // Fills available space

                Spacer(modifier = Modifier.height(16.dp)) // Space at the bottom
            }
        }
    }
}

@Composable
fun CustomNavigationBar(userType: String, navController: NavHostController, onItemSelected: (String) -> Unit) {
    val items = if (userType == "admin") {
        listOf(
            BottomNavItem("Posted Job", NavIcon.Drawable(painterResource(R.drawable.ic_work)), "postedJob"),
            BottomNavItem("Post Job", NavIcon.Vector(Icons.Default.Add), "postJob"),
            BottomNavItem("Chat", NavIcon.Drawable(painterResource(R.drawable.ic_chat)), "chat"),
            BottomNavItem("Profile", NavIcon.Vector(Icons.Default.Person), "profile")
        )
    } else {
        listOf(
            BottomNavItem("Home", NavIcon.Vector(Icons.Default.Home), "home"),
            BottomNavItem("Application", NavIcon.Drawable(painterResource(R.drawable.ic_applications)), "application"),
            BottomNavItem("Chat", NavIcon.Drawable(painterResource(R.drawable.ic_chat)), "chat"),
            BottomNavItem("Profile", NavIcon.Vector(Icons.Default.Person), "profile")
        )
    }

    val currentRoute = navController.currentDestination?.route

    BottomNavigation(
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    when (item.icon) {
                        is NavIcon.Vector -> Icon(item.icon.icon, contentDescription = null)
                        is NavIcon.Drawable -> Image(item.icon.icon, contentDescription = null)
                    }
                },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                    onItemSelected(item.label) // Pass the label to update the title
                }
            )
        }
    }
}

@Composable
fun AdminDrawer(isExpanded: Boolean, onToggle: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 16.dp) // Add padding to the right
    ) {
        if (isExpanded) {
            // Use a Column to position the content below the menu icon
            Column(
                horizontalAlignment = Alignment.End, // Align items to the end (right)
                modifier = Modifier
                    .align(Alignment.TopEnd) // Position at the top right corner
                    .padding(top = 60.dp) // Adjust top padding to position below the menu icon
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Blog", modifier = Modifier.padding(start = 16.dp))
                Text("Contact Us", modifier = Modifier.padding(start = 16.dp))
                Text("Meetings", modifier = Modifier.padding(start = 16.dp))
                Text("Logout", modifier = Modifier
                    .clickable {
                        FirebaseAuth.getInstance().signOut()
                    }
                    .padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.weight(1f)) // Space to push content up if needed
            }
        }
    }
}

@Composable
fun AlumniDrawer(isExpanded: Boolean, onToggle: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 16.dp) // Add padding to the right
    ) {
        if (isExpanded) {
            // Use a Column to position the content below the menu icon
            Column(
                horizontalAlignment = Alignment.End, // Align items to the end (right)
                modifier = Modifier
                    .align(Alignment.TopEnd) // Position at the top right corner
                    .padding(top = 60.dp) // Adjust top padding to position below the menu icon
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Build Resume", modifier = Modifier.padding(start = 16.dp))
                Text("Blog", modifier = Modifier.padding(start = 16.dp))
                Text("Profile Completion Guide", modifier = Modifier.padding(start = 16.dp))
                Text("Saved Jobs", modifier = Modifier.padding(start = 16.dp))
                Text("Meetings", modifier = Modifier.padding(start = 16.dp))
                Text("Logout", modifier = Modifier
                    .clickable {
                        FirebaseAuth.getInstance().signOut()
                    }
                    .padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp)) // Add spacing at the bottom
            }
        }
    }
}
