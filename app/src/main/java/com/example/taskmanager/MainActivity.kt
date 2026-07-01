package com.example.taskmanager
import com.example.taskmanager.notifications.TaskNotifier
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanager.data.SettingsRepository
import com.example.taskmanager.viewmodel.SettingsViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.ui.Screen
import com.example.taskmanager.ui.screens.AddTaskScreen
import com.example.taskmanager.ui.screens.HomeScreen
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanager.viewmodel.TaskViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.taskmanager.ui.theme.TaskManagerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Create the notification channel (required on Android 8+)
        TaskNotifier.createNotificationChannel(this)

// 2. Request runtime permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 0
            )
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {val settingsRepository = SettingsRepository(applicationContext)

            setContent {
                val taskViewModel: TaskViewModel = viewModel()
                val settingsViewModel: SettingsViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                            return SettingsViewModel(settingsRepository) as T
                        }
                    }
                )

                val uiState by taskViewModel.uiState.collectAsState()
                val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
                val navController = rememberNavController()

                TaskManagerAppTheme(darkTheme = isDarkMode) {
                    NavHost(navController = navController, startDestination = Screen.Home.route) {
                        composable(Screen.Home.route) {
                            HomeScreen(
                                uiState = uiState,
                                isDarkMode = isDarkMode,
                                onToggleDarkMode = { settingsViewModel.toggleDarkMode(it) },
                                onAddTaskClick = { navController.navigate(Screen.AddTask.route) },
                                onToggleTask = { taskViewModel.toggleDone(it) }
                            )
                        }
                        composable(Screen.AddTask.route) {
                            val context = androidx.compose.ui.platform.LocalContext.current
                            AddTaskScreen(
                                onSave = { title, category ->
                                    taskViewModel.addTask(title, category)
                                    TaskNotifier.notifyTaskAdded(context, title)  // add this line
                                    navController.popBackStack()
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TaskManagerAppTheme {
        Greeting("Android")
    }
}