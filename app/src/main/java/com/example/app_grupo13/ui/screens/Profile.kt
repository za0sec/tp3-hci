package com.example.app_grupo13.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_grupo13.R
import com.example.app_grupo13.ui.components.NavBar

val MyAppIcons = Icons.Rounded



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF17171F))
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
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
        var nombre by remember {
            mutableStateOf("Octavio")
        }
        var apellido by remember {
            mutableStateOf("Zacagnino")
        }
        var correo by remember {
            mutableStateOf("ozacagnino@itba.edu.ar")
        }
        var alias by remember {
            mutableStateOf("ozacagnino.plum")
        }
        var cvu by remember {
            mutableStateOf("123456789")
        }


        var tempAlias by remember { mutableStateOf(alias) }


        var editedAlias by remember { mutableStateOf(false) }


        var isNombreEditable by remember { mutableStateOf(false) } // Estado de edición para Apellido

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
                    cursorColor = Color(0xFF9C8AE0)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { isNombreEditable = !isNombreEditable },
                modifier = Modifier.height(38.dp).wrapContentSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Editar",
                    tint = Color(0xFF4A347F)
                )
            }
        }

        // Espacio entre Nombre y Apellido
        Spacer(modifier = Modifier.height(16.dp))




        // Campo Apellido con icono
        var isApellidoEditable by remember { mutableStateOf(false) } // Estado de edición para Apellido

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
                    cursorColor = Color(0xFF9C8AE0)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    isApellidoEditable = !isApellidoEditable
                }, // Cambia el estado de edición
                modifier = Modifier.height(38.dp).wrapContentSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Editar",
                    tint = Color(0xFF4A347F)
                )
            }
        }



        Spacer(modifier = Modifier.height(16.dp))

        var isCorreoEditable by remember { mutableStateOf(false) } // Estado de edición para Apellido

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
                    cursorColor = Color(0xFF9C8AE0)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { isCorreoEditable = !isCorreoEditable }, // Cambia el estado de edición
                modifier = Modifier.height(38.dp).wrapContentSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Editar",
                    tint = Color(0xFF4A347F)
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = cvu,
                enabled = false,
                onValueChange = { cvu = it },
                singleLine = true,
                label = { Text("CVU", color = Color.Gray) },
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
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = tempAlias,
                enabled = true,
                onValueChange = {
                    editedAlias = true
                    tempAlias = it
                },
                singleLine = true,
                label = { Text("Alias", color = Color.Gray) },
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
        }

       Spacer(modifier = Modifier.height(16.dp))

        Button(
            enabled = editedAlias, // El botón depende de `editedAlias`
            onClick = {
                // Acción para guardar
                println("Alias guardado: $alias")
                alias = tempAlias
                editedAlias = false // Desactivar botón después de guardar
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7059AB)
            ),

            ) {
            Text(
                text = "Guardar",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }


    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        NavBar(navController = navController)
    }
}

