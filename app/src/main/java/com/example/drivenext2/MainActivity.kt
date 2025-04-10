package com.example.drivenext2

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import com.example.drivenext2.ui.theme.DriveNext2Theme
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.isSystemInDarkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Подключаем ViewModel
            val settingsViewModel = viewModel<SettingsViewModel>()
            val currentTheme by settingsViewModel.currentTheme.collectAsState()

            DriveNext2Theme(
                darkTheme = when (currentTheme) {
                    SettingsViewModel.ThemeMode.SYSTEM -> isSystemInDarkTheme()
                    SettingsViewModel.ThemeMode.DARK -> true
                    SettingsViewModel.ThemeMode.LIGHT -> false
                }
            ) {
                val navController = rememberNavController()
                var hasInternet by remember { mutableStateOf(isNetworkAvailable(this)) }
                var showSplash by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(2000)
                    showSplash = false
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (showSplash) {
                        WelcomeScreen(isSplash = true)
                    } else if (!hasInternet) {
                        NoInternetScreen {
                            hasInternet = isNetworkAvailable(this@MainActivity)
                        }
                    } else {
                        NavHost(
                            navController = navController,
                            startDestination = "onboarding"
                        ) {
                            composable("onboarding") {
                                OnboardingScreen {
                                    navController.navigate("registration")
                                }
                            }
                            composable("registration") {
                                RegistrationScreen(navController)
                            }
                            composable("login") {
                                LoginScreen(navController)
                            }
                            composable("sign_up") {
                                SignUpScreen(navController)
                            }
                            composable("sign_up_second") {
                                SignUpSecondScreen(navController)
                            }
                            composable("sign_up_final") {
                                SignUpFinalScreen(navController)
                            }
                            composable("registration_success") {
                                SuccessScreen(navController)
                            }
                            composable("main_app") {
                                MainAppScreen(navController)
                            }
                            composable("settings") {
                                SettingsScreen(navController)
                            }
                            composable("profile") {
                                ProfileScreen(navController = navController, viewModel = viewModel())
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

    return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}