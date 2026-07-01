package com.example.taskmanager.ui

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddTask : Screen("add_task")
}