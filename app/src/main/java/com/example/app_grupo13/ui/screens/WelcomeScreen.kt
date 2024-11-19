package com.example.app_grupo13.ui.screens

import PrimaryButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app_grupo13.ui.theme.DarkBackground
import com.example.app_grupo13.ui.theme.HintText
import com.example.app_grupo13.ui.theme.LightText


@Composable
fun WelcomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Plum",
            style = MaterialTheme.typography.headlineMedium,
            color = LightText,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "La forma más fácil de gestionar tu billetera",
            style = MaterialTheme.typography.bodyLarge,
            color = HintText,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        PrimaryButton(text = "Registrarse") {
            navController.navigate("register")
        }
    }
}