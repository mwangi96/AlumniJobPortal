package com.example.alumnijobportal.nav

sealed class Screens(val route: String) {
    object LoadScreen : Screens(route = "loading")
    object IntroScreen : Screens(route = "intro_screen")
    object LoginSignUpScreen : Screens(route = "login_screen")
    object SignUpScreen : Screens(route = "signup")
    object LoginScreen : Screens(route = "login")
    object FacebookLoginScreen : Screens(route = "facebook_login")
    object GoogleLoginScreen : Screens(route = "google_login")
    object AppleLoginScreen : Screens(route = "apple_login")

    // Remove deep link parameters from JobDetailScreen and ApplicantsScreen routes
    object JobDetailScreen : Screens(route = "applicationDetail") // Removed {jobId}
    object ApplicantsScreen : Screens(route = "applicants") // Removed {jobId}

    // Updated DashboardScreen route to not require parameters
    object DashboardScreen : Screens(route = "dashboard_screen")

    // Define routes for bottom navigation screens
    object HomeScreen : Screens(route ="home")
    object PostedJobsScreen : Screens(route ="posted_jobs") // Fixed route string
    object JobPostScreen : Screens(route = "post_job") // Fixed route string
    object ApplicationScreen : Screens(route ="application")
    object ChatScreen : Screens(route ="chat")
    object ProfileScreen : Screens(route ="profile")

    // Admin-specific screens
    object AdminBlogScreen : Screens(route = "admin_blog")
    object ContactUsScreen : Screens(route = "admin_contact_us")
    object AdminMeetingsScreen : Screens(route = "admin_meetings")
    object AdminPostedJobsScreen : Screens(route = "admin_posted_jobs")

    // Alumni-specific screens
    object BuildResumeScreen : Screens(route = "build_resume")
    object BlogScreen : Screens(route = "blog")
    object ProfileCompletionGuideScreen : Screens(route = "profile_completion_guide")
    object SavedJobsScreen : Screens(route = "saved_jobs")
    object MeetingsScreen : Screens(route = "meetings")
}
