package com.example.app_grupo13.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.app_grupo13.ui.screens.WelcomeScreen
import com.example.app_grupo13.ui.screens.RegisterScreen
import com.example.app_grupo13.ui.screens.LoginScreen
import com.example.app_grupo13.ui.screens.DashboardScreen
import com.example.app_grupo13.ui.screens.MovementsScreen
import com.example.app_grupo13.ui.screens.ProfileScreen
import com.example.app_grupo13.ui.screens.ResetNewPasswordScreen
import com.example.app_grupo13.ui.screens.ResetPasswordCodeScreen
import com.example.app_grupo13.ui.screens.ResetPasswordScreen
import com.example.app_grupo13.ui.theme.PlumTheme
import com.example.app_grupo13.ui.screens.DepositScreen
import com.example.app_grupo13.ui.screens.TransferScreen
import com.example.app_grupo13.ui.screens.PayScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlumTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "welcome") {
        composable("welcome") { WelcomeScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
        composable("movements") { MovementsScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("reset_password") { ResetPasswordScreen(navController) }
        composable("reset_password_code"){ResetPasswordCodeScreen(navController)}
        composable("reset_password_new") { ResetNewPasswordScreen(navController) }
        composable("deposit") { DepositScreen(navController) }
        composable("transfer") { TransferScreen(navController) }
        composable("pay") { PayScreen(navController) }
    }
}

