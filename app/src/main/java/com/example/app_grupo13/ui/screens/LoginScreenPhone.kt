package com.example.app_grupo13.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import com.example.app_grupo13.ui.viewmodels.LoginViewModel
import com.example.app_grupo13.ui.viewmodels.LoginViewModelFactory
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import android.util.Log
import androidx.compose.material.icons.filled.ArrowBack
import com.example.app_grupo13.R
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(LocalContext.current)
    )
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordHidden by remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF121212) // Fondo oscuro
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top // Cambiado para acomodar la flecha de regreso al inicio
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
                        contentDescription = stringResource(R.string.back_arrow),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(200.dp)) // Espaciado entre la flecha y el título

            // Título
            Text(
                text = stringResource(R.string.login_title),
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Campo de Email
            TextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                label = { Text(stringResource(R.string.email), color = Color.Gray) },
                placeholder = { Text(stringResource(R.string.enter_email), color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.MailOutline,
                        contentDescription = stringResource(R.string.email_icon),
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

            // Campo de Contraseña
            TextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                label = { Text(stringResource(R.string.password), color = Color.Gray) },
                placeholder = { Text(stringResource(R.string.enter_password), color = Color.Gray) },
                visualTransformation =
                if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = stringResource(R.string.password_icon),
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
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.forgot_password),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = stringResource(R.string.recover_password),
                    color = Color(0xFF9C8AE0),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("reset_password") }
                )
            }


            Spacer(modifier = Modifier.height(120.dp))

            // Botón de Continuar
            Button(
                onClick = {
                    Log.d("LoginScreen", "Login button clicked with email: $email")
                    viewModel.loginUser(email, password)
                },
                enabled = !viewModel.isLoading.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7059AB)
                ),
            ) {
                if (viewModel.isLoading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        text = stringResource(R.string.continue_button),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Observa el resultado del inicio de sesión
    LaunchedEffect(viewModel.loginResult.value) {
        Log.d("LoginScreen", "LoginResult changed: ${viewModel.loginResult.value}")
        viewModel.loginResult.value?.let { success ->
            if (success) {
                Log.d("LoginScreen", "Login successful, navigating to dashboard")
                navController.navigate("dashboard") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                Log.d("LoginScreen", "Login failed")
            }
        }
    }
}
