package com.example.app_grupo13.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF121212) // Fondo oscuro
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top // Centrar contenido verticalmente
        ) {
            // Flecha de regreso
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.wrapContentSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back Arrow",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(200.dp)) // Espaciado entre la flecha y el título
            // Título
            Text(
                text = "Recuperar contraseña",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Campo de Email
            TextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                label = { Text("Email", color = Color.Gray) },
                placeholder = { Text("Ingresa tu correo electrónico", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.MailOutline,
                        contentDescription = "Email Icon",
                        tint = Color(0xFF9C8AE0)
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color(0xFF9C8AE0),
                    unfocusedIndicatorColor = Color.Gray,
                    containerColor = Color(0xFF1C1C1C),
                    cursorColor = Color(0xFF9C8AE0)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Texto explicativo
            Text(
                text = "Ingresa tu correo electrónico para enviar un enlace de restablecimiento",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de Continuar
            Button(
                onClick = {
                    // Acción para enviar el enlace
                    navController.navigate("reset_password_code")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7059AB)
                ),

            ) {
                Text(
                    text = "Continuar",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
