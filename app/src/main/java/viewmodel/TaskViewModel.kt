package com.example.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskCategory
import com.example.taskmanager.data.TaskRepository
import com.example.taskmanager.network.QuoteApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val quoteText: String = "",
    val quoteAuthor: String = "",
    val quoteLoading: Boolean = false
)

class TaskViewModel(
    private val repository: TaskRepository = TaskRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        loadTasks()
        loadQuote()
    }

    fun loadTasks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val tasks = repository.getTasks()
            _uiState.value = _uiState.value.copy(tasks = tasks, isLoading = false)
        }
    }

    fun addTask(title: String, category: TaskCategory) {
        viewModelScope.launch {
            repository.addTask(title, "", category)
            _uiState.value = _uiState.value.copy(tasks = repository.getTasks())
        }
    }

    private fun loadQuote() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(quoteLoading = true)
            try {
                val result = QuoteApi.service.getQuoteOfTheDay()
                val quote = result.firstOrNull()
                _uiState.value = _uiState.value.copy(
                    quoteText = quote?.q ?: "Stay productive!",
                    quoteAuthor = quote?.a ?: "",
                    quoteLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    quoteText = "Couldn't load quote — check your connection.",
                    quoteAuthor = "",
                    quoteLoading = false
                )
            }
        }
    }
    fun toggleDone(id: Int) {
        viewModelScope.launch {
            repository.toggleTaskDone(id)
            _uiState.value = _uiState.value.copy(tasks = repository.getTasks())
        }
    }
}