package com.example.alumnijobportal.nav

sealed class Screens(val route: String) {
    data object LoadScreen : Screens(route = "loading")
    data object IntroScreen : Screens(route = "intro_screen")
    data object LoginSignUpScreen : Screens(route = "login_screen")
    data object SignUpScreen : Screens(route = "signup")
    data object LoginScreen : Screens(route = "login")

    // Remove deep link parameters from JobDetailScreen and ApplicantsScreen routes
    data object JobDetailScreen : Screens(route = "applicationDetail") // Removed {jobId}
    data object ApplicantsScreen : Screens(route = "applicants") // Removed {jobId}
//    data object ApplyScreen : Screens("apply")

    // Updated DashboardScreen route to not require parameters
    data object DashboardScreen : Screens(route = "dashboard_screen")

    // Define routes for bottom navigation screens
    data object HomeScreen : Screens(route ="home")
    data object PostedJobsScreen : Screens(route ="posted job") // Fixed route string
    data object JobPostScreen : Screens(route = "post job") // Fixed route string
    data object ApplicationScreen : Screens(route ="application")
    data object ChatScreen : Screens(route ="chat")
    data object ProfileScreen : Screens(route ="profile")

    // Admin-specific screens
    data object AdminBlogScreen : Screens(route = "admin_blog")
    data object ContactUsScreen : Screens(route = "admin_contact_us")
    data object AdminMeetingsScreen : Screens(route = "admin_meetings")
    data object AdminPostedJobsScreen : Screens(route = "admin_posted_jobs")

    // Alumni-specific screens
    data object BuildResumeScreen : Screens(route = "build_resume")
    data object BlogScreen : Screens(route = "blog")
    data object ProfileCompletionGuideScreen : Screens(route = "profile_completion_guide")
    data object SavedJobsScreen : Screens(route = "saved_jobs")
    data object MeetingsScreen : Screens(route = "meetings")
}
