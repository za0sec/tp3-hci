package com.example.app_grupo13.ui.screens

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_grupo13.R
import com.example.app_grupo13.ui.components.NavBar
import com.example.app_grupo13.ui.viewmodels.ProfileViewModel
import com.example.app_grupo13.ui.viewmodels.ProfileViewModelFactory
import kotlinx.coroutines.launch

val MyAppIcons = Icons.Rounded



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    context: Context = LocalContext.current,
    viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(context))
) {
    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF17171F))
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Información del usuario",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF7059AB))
            }
        }

        error?.let { errorMsg ->
            Text(
                text = errorMsg,
                color = Color.Red,
                fontSize = 16.sp,
                modifier = Modifier.padding(16.dp)
            )
        }

        user?.let { currentUser ->
            val walletDetails by viewModel.walletDetails.collectAsState()
            
            var nombre by remember { mutableStateOf(currentUser.firstName) }
            var apellido by remember { mutableStateOf(currentUser.lastName) }
            var correo by remember { mutableStateOf(currentUser.email) }
            var alias by remember { mutableStateOf(walletDetails?.alias ?: "") }
            var cvu by remember { mutableStateOf(walletDetails?.cbu ?: "") }

            var tempAlias by remember { mutableStateOf(alias) }
            var editedAlias by remember { mutableStateOf(false) }

            // Campo Nombre
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    enabled = false,
                    singleLine = true,
                    label = { Text("Nombre", color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Nombre",
                            tint = Color(0xFF9C8AE0)
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color(0xFF9C8AE0),
                        unfocusedIndicatorColor = Color.Gray,
                        containerColor = Color(0xFF1C1C1C),
                        cursorColor = Color(0xFF9C8AE0),
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Apellido
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    enabled = false,
                    singleLine = true,
                    label = { Text("Apellido", color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Apellido",
                            tint = Color(0xFF9C8AE0)
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color(0xFF9C8AE0),
                        unfocusedIndicatorColor = Color.Gray,
                        containerColor = Color(0xFF1C1C1C),
                        cursorColor = Color(0xFF9C8AE0),
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Email
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = correo,
                    onValueChange = { correo = it },
                    enabled = false,
                    singleLine = true,
                    label = { Text("Correo electrónico", color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Correo electrónico",
                            tint = Color(0xFF9C8AE0)
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color(0xFF9C8AE0),
                        unfocusedIndicatorColor = Color.Gray,
                        containerColor = Color(0xFF1C1C1C),
                        cursorColor = Color(0xFF9C8AE0),
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo CVU
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = cvu,
                    enabled = false,
                    onValueChange = { },
                    singleLine = true,
                    label = { Text("CVU", color = Color.Gray) },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color(0xFF9C8AE0),
                        unfocusedIndicatorColor = Color.Gray,
                        containerColor = Color(0xFF1C1C1C),
                        cursorColor = Color(0xFF9C8AE0),
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Alias
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = tempAlias.toString(),
                    enabled = true,
                    onValueChange = { newValue ->
                        editedAlias = true
                        tempAlias = newValue.uppercase()
                    },
                    singleLine = true,
                    label = { Text("Alias (debe contener un punto)", color = Color.Gray) },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color(0xFF9C8AE0),
                        unfocusedIndicatorColor = Color.Gray,
                        containerColor = Color(0xFF1C1C1C),
                        cursorColor = Color(0xFF9C8AE0),
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val scope = rememberCoroutineScope()
            Button(
                enabled = editedAlias && tempAlias.contains("."),
                onClick = {
                    scope.launch {
                        if (viewModel.updateAlias(tempAlias)) {
                            editedAlias = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (tempAlias.contains(".")) Color(0xFF7059AB) else Color.Gray
                )
            ) {
                Text(
                    text = if (tempAlias.contains(".")) "Guardar" else "El alias debe contener un punto",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        NavBar(navController = navController)
    }
}
