package com.example.lecom.ui.screen.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lecom.ui.screen.compose.DetailsScreen
import com.example.lecom.ui.screen.compose.HomeScreen
import com.example.lecom.ui.screen.compose.SettingsScreen
import com.example.lecom.ui.theme.LeComTheme
import com.example.lecom.utils.DesignSystem

/**
 * Main Activity - Entry point of the application
 * This is the first page that loads when the app starts
 */
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Apply edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        DesignSystem.initialize(this)
        
        setContent {
            LeComTheme {
                MainScreen()
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        DesignSystem.updateTheme(this)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route ?: "home"
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "lECom",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = when (currentDestination) {
                                "home" -> "Home"
                                "details" -> "Details"
                                "settings" -> "Settings"
                                else -> ""
                            },
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Text("🏠") },
                    label = { Text("Home") },
                    selected = currentDestination == "home",
                    onClick = {
                        if (currentDestination != "home") {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Text("📋") },
                    label = { Text("Details") },
                    selected = currentDestination == "details",
                    onClick = {
                        if (currentDestination != "details") {
                            navController.navigate("details")
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Text("⚙️") },
                    label = { Text("Settings") },
                    selected = currentDestination == "settings",
                    onClick = {
                        if (currentDestination != "settings") {
                            navController.navigate("settings")
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") {
                HomeScreen(
                    onNavigateToDetails = {
                        navController.navigate("details")
                    }
                )
            }
            composable("details") {
                DetailsScreen(
                    onNavigateToSettings = {
                        navController.navigate("settings")
                    }
                )
            }
            composable("settings") {
                SettingsScreen(
                    onNavigateToHome = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
