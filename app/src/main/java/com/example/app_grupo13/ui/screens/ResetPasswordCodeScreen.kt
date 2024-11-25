package com.example.app_grupo13.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_grupo13.ui.viewmodels.RecoverPasswordViewModel
import com.example.app_grupo13.ui.viewmodels.RecoverPasswordViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordCodeScreen(
    navController: NavController,
    context: Context = LocalContext.current,
    email: String? = navController.previousBackStackEntry?.savedStateHandle?.get<String>("email"),
    viewModel: RecoverPasswordViewModel = viewModel(factory = RecoverPasswordViewModelFactory(context))
) {
    var code by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        // Establecer el email en el ViewModel cuando la pantalla se carga
        email?.let { viewModel.setEmail(it) }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF121212)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
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

            Spacer(modifier = Modifier.height(100.dp))

            // Título
            Text(
                text = "Restablecer Contraseña",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo para el Código
            TextField(
                value = code,
                onValueChange = { code = it },
                singleLine = true,
                label = { Text("Código", color = Color.Gray) },
                placeholder = { Text("Ingresa el código recibido", color = Color.Gray) },
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

            // Campo de Nueva Contraseña
            TextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                singleLine = true,
                label = { Text("Nueva Contraseña", color = Color.Gray) },
                placeholder = { Text("Ingresa tu nueva contraseña", color = Color.Gray) },
                visualTransformation = PasswordVisualTransformation(),
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

            // Campo de Confirmar Contraseña
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                singleLine = true,
                label = { Text("Confirmar Contraseña", color = Color.Gray) },
                placeholder = { Text("Confirma tu nueva contraseña", color = Color.Gray) },
                visualTransformation = PasswordVisualTransformation(),
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

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            error?.let { errorMsg ->
                Text(
                    text = errorMsg,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de Continuar
            Button(
                onClick = {
                    if (newPassword != confirmPassword) {
                        errorMessage = "Las contraseñas no coinciden"
                        return@Button
                    }
                    if (newPassword.length < 8) {
                        errorMessage = "La contraseña debe tener al menos 8 caracteres"
                        return@Button
                    }
                    if (code.isEmpty()) {
                        errorMessage = "Debes ingresar el código"
                        return@Button
                    }
                    
                    scope.launch {
                        if (viewModel.resetPassword(code, newPassword)) {
                            navController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7059AB)
                ),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Continuar",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Texto explicativo
            Text(
                text = "Revisa tu correo y escribe el código que te enviamos junto con tu nueva contraseña.",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
