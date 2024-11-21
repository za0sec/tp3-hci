package com.example.app_grupo13.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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

@Composable
fun ProfileScreen(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF17171F))
            .padding(16.dp)
    ){
        Text(
            text = "Información del usuario",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        var nombre by remember {
            mutableStateOf("Octavio") }
        var apellido by remember {
            mutableStateOf("") }
        var correo by remember {
            mutableStateOf("") }
        var alias by remember {
            mutableStateOf("") }
        var cvu by remember {
            mutableStateOf("1234567") }

        Text(
            text = "Nombre",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            BasicTextField(
                value = nombre,
                onValueChange = { nombre = it },
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(color = Color.White),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                            .background(Color.Transparent)
                            .padding(8.dp)
                    ) {
                        innerTextField()
                    }
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {},
                modifier = Modifier.height(38.dp).wrapContentSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Editar",
                    tint = Color(0xFF4A347F)
                )
            }
        }

        //agregar aca apellido, correo, alias


        Text(
            text = "CVU: $cvu",
            color = Color.White,
            modifier = Modifier.padding(top = 16.dp)
        )

    }
}