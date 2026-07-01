package com.example.taskmanager.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.ui.components.TaskItem
import com.example.taskmanager.viewmodel.TaskUiState
import androidx.compose.foundation.layout.Arrangement

@Composable
fun HomeScreen(
    uiState: TaskUiState,
    onAddTaskClick: () -> Unit,
    onToggleTask: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text("My Tasks", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(12.dp))

        // Quote card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (uiState.quoteLoading) {
                    Text("Loading quote...")
                } else {
                    Text(
                        text = "\"${uiState.quoteText}\"",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (uiState.quoteAuthor.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "— ${uiState.quoteAuthor}",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.tasks, key = { it.id }) { task ->
                TaskItem(
                    task = task,
                    onToggle = { onToggleTask(task.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAddTaskClick) { Text("Add Task") }
    }
}
@Composable
fun HomeScreen(
    uiState: TaskUiState,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onAddTaskClick: () -> Unit,
    onToggleTask: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()   // add fillMaxSize here
            .padding(24.dp)
    ) {
        // title row stays the same
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text("My Tasks", style = MaterialTheme.typography.headlineLarge)
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text("Dark", style = MaterialTheme.typography.labelSmall)
                Switch(checked = isDarkMode, onCheckedChange = { onToggleDarkMode(it) })
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // give LazyColumn a weight so it fills remaining space
        LazyColumn(
            modifier = Modifier.weight(1f),  // add weight(1f) here
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.tasks, key = { it.id }) { task ->
                TaskItem(
                    task = task,
                    onToggle = { onToggleTask(task.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAddTaskClick, modifier = Modifier.fillMaxWidth()) {
            Text("Add Task")
        }
    }
}