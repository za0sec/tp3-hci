package com.example.app_grupo13.ui.screens

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
fun ResetPasswordScreen(
    navController: NavController,
    context: Context = LocalContext.current,
    viewModel: RecoverPasswordViewModel = viewModel(factory = RecoverPasswordViewModelFactory(context))
) {
    var email by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

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

            Spacer(modifier = Modifier.height(200.dp))
            
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

            if (error != null) {
                Text(
                    text = error!!,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

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
                    scope.launch {
                        if (viewModel.recoverPassword(email)) {
                            navController.currentBackStackEntry?.savedStateHandle?.set("email", email)
                            navController.navigate("reset_password_code")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7059AB)
                ),
                enabled = !isLoading && email.isNotEmpty()
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
            
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
