package com.example.taskmanager.data

data class Task(
    val id: Int,
    val title: String,
    val description: String = "",
    val category: TaskCategory = TaskCategory.PERSONAL,
    val isDone: Boolean = false
)

enum class TaskCategory(val label: String) {
    WORK("Work"), PERSONAL("Personal"), STUDY("Study"), HEALTH("Health")
}