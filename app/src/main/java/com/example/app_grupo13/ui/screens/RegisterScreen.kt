package com.example.app_grupo13.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_grupo13.ui.theme.DarkBackground
import com.example.app_grupo13.ui.theme.LightText
import com.example.app_grupo13.ui.viewmodels.UserViewModel
import androidx.compose.ui.res.stringResource
import com.example.app_grupo13.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, userViewModel: UserViewModel) { // TODO confirmar contrasena ojitos
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val registrationResult = userViewModel.registrationResult

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = DarkBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top // Cambiado para acomodar la flecha al principio
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

            Spacer(modifier = Modifier.height(50.dp)) // Espaciado entre la flecha y el título

            Text(
                text = stringResource(R.string.signup_title),
                color = LightText,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Campo de Nombre
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                singleLine = true,
                label = { Text(stringResource(R.string.first_name), color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(R.string.first_name),
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

            // Campo de Apellido
            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                singleLine = true,
                label = { Text(stringResource(R.string.last_name), color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(R.string.last_name),
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

            // Campo de Email
            TextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                label = { Text(stringResource(R.string.email), color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
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
                visualTransformation = PasswordVisualTransformation(),
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
            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Confirmar Contraseña
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                singleLine = true,
                label = { Text(stringResource(R.string.confirm_password), color = Color.Gray) },
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = stringResource(R.string.confirm_password),
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

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (password == confirmPassword) {
                        userViewModel.registerUser(firstName, lastName, email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7059AB)
                )
            ) {
                Text(
                    text = stringResource(R.string.continue_button),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Manejo del resultado
            when (registrationResult.value) {
                true -> navController.navigate("verify/${email}") {
                    popUpTo("register") { inclusive = true }
                }
                false -> Text(
                    text = stringResource(R.string.registration_error),
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
                null -> {}
            }
        }
    }
}
