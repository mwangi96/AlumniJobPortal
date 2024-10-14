package com.example.alumnijobportal.nav

sealed class Screens(val route: String) {
    data object LoadScreen : Screens(route = "loading")
    data object IntroScreen : Screens(route = "intro_screen")
    data object LoginSignUpScreen : Screens(route = "login_screen")
    data object SignUpScreen : Screens(route = "signup")
    data object LoginScreen : Screens(route = "login")
    data object FacebookLoginScreen : Screens(route = "facebook_login")
    data object GoogleLoginScreen : Screens(route = "google_login")
    data object AppleLoginScreen : Screens(route = "apple_login")
//    data object ProfileScreen:Screens(route = "profile" )
    data object DashboardScreen : Screens("dashboard_screen/{userName}/{userEmail}/{userRole}")
    // Define new screens for the bottom navigation
//    data object HomeScreen : Screens("home")
//    data object PostedJobsScreen : Screens("posted_jobs")
//    data object BrowseJobsScreen : Screens("browse_jobs")
//    data object JobPostScreen : Screens("post_job")
//    data object MyApplicationsScreen : Screens("my_applications")
//    data object ChatScreen : Screens("chat")
//    data object ProfileScreen : Screens("profile")
}
