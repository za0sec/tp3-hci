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
import com.example.app_grupo13.ui.screens.ResetPasswordScreen
import com.example.app_grupo13.ui.screens.LoginScreen
import com.example.app_grupo13.ui.screens.DashboardScreen
import com.example.app_grupo13.ui.screens.MovementsScreen
import com.example.app_grupo13.ui.screens.ProfileScreen
import com.example.app_grupo13.ui.screens.VerifyScreen
import com.example.app_grupo13.ui.theme.PlumTheme
import com.example.app_grupo13.ui.viewmodels.UserViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.app_grupo13.data.network.RemoteDataSource
import com.example.app_grupo13.data.repository.UserRepository
import com.example.app_grupo13.ui.viewmodels.UserViewModelFactory
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_grupo13.ui.screens.Dashboard
import com.example.app_grupo13.ui.viewmodels.DashboardViewModelFactory
import com.example.app_grupo13.ui.viewmodels.LoginViewModelFactory
import com.example.app_grupo13.ui.viewmodels.VerifyViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val remoteDataSource = RemoteDataSource(this)
        val userRepository = UserRepository(remoteDataSource)
        val userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(this)
        )[UserViewModel::class.java]

        setContent {
            PlumTheme {
                AppNavigation(userViewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(userViewModel: UserViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") { WelcomeScreen(navController) }
        composable("register") { RegisterScreen(navController, userViewModel) }
        composable("login") { 
            LoginScreen(
                navController = navController,
                viewModel = viewModel(
                    factory = LoginViewModelFactory(LocalContext.current)
                )
            ) 
        }
        composable("reset_password") { ResetPasswordScreen(navController) }
        composable("dashboard") { 
            Dashboard(
                navController = navController,
                viewModel = viewModel(
                    factory = DashboardViewModelFactory(LocalContext.current)
                )
            ) 
        }
        composable("movements") { MovementsScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable(
            route = "verify/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            VerifyScreen(
                navController = navController,
                email = backStackEntry.arguments?.getString("email"),
                viewModel = viewModel(
                    factory = VerifyViewModelFactory(LocalContext.current)
                )
            )
        }
    }
}
