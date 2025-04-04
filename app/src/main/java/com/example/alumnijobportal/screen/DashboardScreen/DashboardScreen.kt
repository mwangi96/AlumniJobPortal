package com.example.alumnijobportal.screen.DashboardScreen

import ApplyScreen
import SharedViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alumnijobportal.nav.Screens
import com.example.alumnijobportal.screen.ApplicantsScreen
import com.example.alumnijobportal.screen.JobPostScreen
import com.example.alumnijobportal.screen.JobsPostedScreen
import com.example.alumnijobportal.screen.ChatScreen
import com.example.alumnijobportal.screen.ProfileScreen
import com.example.alumnijobportal.screen.HomeScreen
import com.example.alumnijobportal.screen.ApplicationScreen
import com.example.alumnijobportal.screen.JobDetailScreen

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
    userEmail: String,
    sharedViewModel: SharedViewModel // Pass the sharedViewModel as a parameter
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var isDrawerExpanded by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("Dashboard") }
    val internalNavController = rememberNavController()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            if (userRole == "admin") {
                AdminDrawer(
                    isExpanded = isDrawerExpanded,
                    onToggle = { isDrawerExpanded = !isDrawerExpanded },
                    navController = navController // Pass the navController here
                )
            } else {
                AlumniDrawer(
                    isExpanded = isDrawerExpanded,
                    onToggle = { isDrawerExpanded = !isDrawerExpanded },
                    navController = navController // Pass the navController here
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(title) },
                    actions = {
                        // Show menu icon only for specific screens
                        if ((userRole == "admin" && internalNavController.currentDestination?.route == Screens.PostedJobsScreen.route) ||
                            (userRole == "alumni" && internalNavController.currentDestination?.route == Screens.HomeScreen.route)) {
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    drawerState.open()
                                    isDrawerExpanded = true // Ensure the drawer is expanded
                                }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    },
                )
            },

            bottomBar = {
                CustomNavigationBar(userRole, internalNavController) { selectedTitle ->
                    title = selectedTitle
                }
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                Spacer(modifier = Modifier.height(10.dp))

                NavHost(
                    navController = internalNavController,
                    startDestination = if (userRole == "admin") Screens.PostedJobsScreen.route else Screens.HomeScreen.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(Screens.PostedJobsScreen.route) {
                        title = "Posted Jobs" // Set title for this screen
                        JobsPostedScreen(navController = internalNavController, sharedViewModel = sharedViewModel) // Pass sharedViewModel here
                    }
                    composable(Screens.JobPostScreen.route) {
                        title = "Post a new Job" // Set title for this screen
                        JobPostScreen(navController = internalNavController, sharedViewModel = sharedViewModel) // Pass sharedViewModel here
                    }
                    composable(Screens.ChatScreen.route) {
                        title = "Messages" // Set title for this screen
                        ChatScreen(navController = internalNavController, sharedViewModel = sharedViewModel) // Pass sharedViewModel here
                    }
                    composable(Screens.ProfileScreen.route) {
                        title = "Profile" // Set title for this screen
                        ProfileScreen(navController = internalNavController, sharedViewModel = sharedViewModel) // Pass sharedViewModel here
                    }
                    composable(Screens.HomeScreen.route) {
                        title = "Available Jobs" // Set title for this screen
                        HomeScreen(navController = internalNavController, sharedViewModel = sharedViewModel) // Already passing sharedViewModel correctly
                    }
                    composable(Screens.ApplicationScreen.route) {
                        title = "Applications" // Set title for this screen
                        ApplicationScreen(navController = internalNavController, userEmail = userEmail, sharedViewModel = sharedViewModel) // Pass sharedViewModel here
                    }
                    composable("jobDetail") {
                        title = "Job Details"
                        JobDetailScreen(
                            navController = internalNavController,
                            userEmail = userEmail,
                            sharedViewModel = sharedViewModel // Use SharedViewModel to retrieve jobId
                        )
                    }
                    composable("applicants") {
                        title = "Applicants" // Set title for applicants screen
                        ApplicantsScreen(navController = internalNavController, sharedViewModel = sharedViewModel) // Pass sharedViewModel to ApplicantsScreen
                    }
                    composable("apply") { // Remove jobId from the route
                        ApplyScreen(
                            navController = internalNavController,
                            sharedViewModel = sharedViewModel,
                            userEmail = userEmail // pass the email here if needed
                        )
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun CustomNavigationBar(userType: String, navController: NavHostController, onItemSelected: (String) -> Unit) {
    val items = if (userType == "admin") {
        listOf(
            BottomNavItem("Posted Job", NavIcon.Drawable(painterResource(R.drawable.ic_work)), Screens.PostedJobsScreen.route),
            BottomNavItem("Post Job", NavIcon.Vector(Icons.Default.Add), Screens.JobPostScreen.route),
            BottomNavItem("Chat", NavIcon.Drawable(painterResource(R.drawable.ic_chat)), Screens.ChatScreen.route),
            BottomNavItem("Profile", NavIcon.Vector(Icons.Default.Person), Screens.ProfileScreen.route)
        )
    } else {
        listOf(
            BottomNavItem("Home", NavIcon.Vector(Icons.Default.Home), Screens.HomeScreen.route),
            BottomNavItem("Applied", NavIcon.Drawable(painterResource(R.drawable.ic_applications)), Screens.ApplicationScreen.route),
            BottomNavItem("Chat", NavIcon.Drawable(painterResource(R.drawable.ic_chat)), Screens.ChatScreen.route),
            BottomNavItem("Profile", NavIcon.Vector(Icons.Default.Person), Screens.ProfileScreen.route)
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
                    onItemSelected(item.label)
                }
            )
        }
    }
}

@Composable
fun AdminDrawer(isExpanded: Boolean, onToggle: () -> Unit, navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 16.dp)
    ) {
        if (isExpanded) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 60.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Blog", modifier = Modifier.clickable { navController.navigate(Screens.BlogScreen.route) })
                Spacer(modifier = Modifier.height(8.dp))
                Text("Contact Us", modifier = Modifier.clickable { navController.navigate(Screens.ContactUsScreen.route) })
                Spacer(modifier = Modifier.height(8.dp))
                Text("Meetings", modifier = Modifier.clickable { navController.navigate(Screens.MeetingsScreen.route) })
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Logout",
                    modifier = Modifier.clickable {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate(Screens.LoginScreen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true // Clears the back stack
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun AlumniDrawer(isExpanded: Boolean, onToggle: () -> Unit, navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 16.dp)
    ) {
        if (isExpanded) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 60.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Build Resume", modifier = Modifier.clickable { navController.navigate(Screens.BuildResumeScreen.route) })
                Spacer(modifier = Modifier.height(8.dp))
                Text("Blog", modifier = Modifier.clickable { navController.navigate(Screens.BlogScreen.route) })
                Spacer(modifier = Modifier.height(8.dp))
                Text("Profile Completion Guide", modifier = Modifier.clickable { navController.navigate(Screens.ProfileCompletionGuideScreen.route) })
                Spacer(modifier = Modifier.height(8.dp))
                Text("Contact Us", modifier = Modifier.clickable { navController.navigate(Screens.ContactUsScreen.route) })
                Spacer(modifier = Modifier.height(8.dp))
                Text("Saved Jobs", modifier = Modifier.clickable { navController.navigate(Screens.SavedJobsScreen.route) })
                Spacer(modifier = Modifier.height(8.dp))
                Text("Meetings", modifier = Modifier.clickable { navController.navigate(Screens.MeetingsScreen.route) })
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Logout",
                    modifier = Modifier.clickable {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate(Screens.LoginScreen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true // Clears the back stack
                            }
                        }
                    }
                )
            }
        }
    }
}
