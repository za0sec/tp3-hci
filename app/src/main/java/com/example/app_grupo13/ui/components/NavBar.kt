package com.example.app_grupo13.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.res.stringResource
import com.example.app_grupo13.R

@Composable
fun NavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color(0xFF202020)
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.List, contentDescription = stringResource(R.string.movements)) },
            label = { Text(stringResource(R.string.movements)) },
            selected = currentRoute == "movements",
            onClick = { navController.navigate("movements") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF8A4FFF),
                selectedTextColor = Color(0xFF8A4FFF),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = stringResource(R.string.home)) },
            label = { Text(stringResource(R.string.home)) },
            selected = currentRoute == "dashboard",
            onClick = { navController.navigate("dashboard") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF8A4FFF),
                selectedTextColor = Color(0xFF8A4FFF),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = stringResource(R.string.profile)) },
            label = { Text(stringResource(R.string.profile)) },
            selected = currentRoute == "profile",
            onClick = { navController.navigate("profile") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF8A4FFF),
                selectedTextColor = Color(0xFF8A4FFF),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
    }
}