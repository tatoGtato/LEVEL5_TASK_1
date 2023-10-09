package com.example.level5_task_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.level5_task_1.ui.theme.LEVEL5_TASK_1Theme
import com.example.level5_task_1.ui.theme.screens.CreateProfileScreen
import com.example.level5_task_1.ui.theme.screens.ProfileScreen
import com.example.level5_task_1.ui.theme.screens.ProfileScreens

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProfileApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileApp() {
    val navController = rememberNavController()
    Scaffold { innerPadding ->
        ProfileNavHost(navController, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun ProfileNavHost(navController: NavHostController, modifier: Modifier = Modifier) {

    val viewModel: ProfileViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = ProfileScreens.CreateProfileScreen.route,
        modifier = modifier
    ) {
        composable(ProfileScreens.CreateProfileScreen.route) {
            CreateProfileScreen(navController = navController, viewModel)
        }
        composable(ProfileScreens.ProfileScreen.route) {
            ProfileScreen(viewModel)
        }
    }
}