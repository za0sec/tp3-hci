package com.example.app_grupo13.ui.screens

import CustomTextField
import PrimaryButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun ResetPasswordScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Restablecer contraseña",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        CustomTextField(
            value = password,
            onValueChange = { password = it },
            label = "Nueva contraseña",
            leadingIcon = Icons.Default.Lock,
            isPassword = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        CustomTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirmar contraseña",
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = Icons.Default.Lock,
            isPassword = true
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        PrimaryButton(
            onClick = {
                // TODO: Implement password reset logic
                navController.navigate("login")
            },
            text = "Continuar"
        )
    }
}

