package com.example.level5_task_1.ui.theme.screens

sealed class ProfileScreens(val route: String) {
    object CreateProfileScreen: ProfileScreens("create_profile_screen")
    object ProfileScreen: ProfileScreens("profile_screen")
}