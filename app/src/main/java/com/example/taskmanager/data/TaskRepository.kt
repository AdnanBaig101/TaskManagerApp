package com.example.taskmanager.data

import kotlinx.coroutines.delay

class TaskRepository {
    private val tasks = mutableListOf(
        Task(1, "Finish Compose assignment", category = TaskCategory.STUDY),
        Task(2, "Gym session", category = TaskCategory.HEALTH)
    )
    private var nextId = tasks.size + 1

    suspend fun getTasks(): List<Task> {
        delay(300); return tasks.toList() }
    suspend fun addTask(title: String, description: String, category: TaskCategory): Task {
        val newTask = Task(nextId++, title, description, category)
        tasks.add(newTask)
        return newTask
    }
    suspend fun toggleTaskDone(id: Int) {
        val i = tasks.indexOfFirst { it.id == id }
        if (i != -1) tasks[i] = tasks[i].copy(isDone = !tasks[i].isDone)
    }
    suspend fun deleteTask(id: Int) { tasks.removeAll { it.id == id } }
    fun findById(id: Int): Task? = tasks.find { it.id == id }
}