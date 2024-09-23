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
//    data object DashboardScreen : Screens(route = "dashboard_screen")
    data object MainScreen : Screens(route = "main_screen")
    object DashboardScreen : Screens("dashboard_screen/{userName}/{userEmail}") // Include parameters in the route
}
