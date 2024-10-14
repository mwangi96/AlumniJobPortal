package com.example.alumnijobportal.utils

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "", // Add more fields as necessary
    val profilePictureUrl: String = "" // Example of additional field
)
