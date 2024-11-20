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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.app_grupo13.R
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
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxSize(0.4f) // Makes the logo take up 40% of the screen
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

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text(
                text = "¿Ya tienes una cuenta? ",
                style = MaterialTheme.typography.bodyMedium,
                color = HintText
            )
            Text(
                text = "Inicia sesión",
                style = MaterialTheme.typography.bodyMedium,
                color = LightText,
                modifier = Modifier.clickable { navController.navigate("login") }
            )
        }
    }
}