package com.example.app_grupo13.ui.screens

import CustomTextField
import PrimaryButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.app_grupo13.ui.theme.DarkBackground
import com.example.app_grupo13.ui.theme.LightText

@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crea tu cuenta", style = MaterialTheme.typography.headlineLarge, color = LightText)
        CustomTextField("Nombre completo", name, { name = it }, Icons.Default.Person)
        CustomTextField("Email", email, { email = it }, Icons.Default.Email)
        CustomTextField("Contraseña", password, { password = it }, Icons.Default.Lock, isPassword = true)
        CustomTextField("Confirmar contraseña", confirmPassword, { confirmPassword = it }, Icons.Default.Lock, isPassword = true)
        PrimaryButton("Continuar") {
            // Lógica de registro
        }
    }
}
