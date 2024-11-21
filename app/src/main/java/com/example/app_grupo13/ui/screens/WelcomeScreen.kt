package com.example.app_grupo13.ui.screens

import PrimaryButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.app_grupo13.R
import com.example.app_grupo13.ui.theme.DarkBackground
import com.example.app_grupo13.ui.theme.HintText
import com.example.app_grupo13.ui.theme.LightText


@Composable
fun WelcomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1C1C1C), // Negro
                        Color(0xFF4A347F)  // Violeta oscuro
                    )
                )
            ),

        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(120.dp)) // Espaciado superior

        // Logo y tarjetas
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(170.dp) // Tamaño del logo
            )

            // Tarjetas
            Image(
                painter = painterResource(id = R.drawable.ic_ccards),
                contentDescription = "Tarjetas",
                modifier = Modifier
                    .size(200.dp) // Tamaño de las tarjetas

            )
        }

        Spacer(modifier = Modifier.height(60.dp)) // Espaciado entre tarjetas y slogan

        // Slogan alineado con el botón
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp), // Alineación con el botón
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "La forma más",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "fácil de gestionar",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "tu billetera",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFFAA629), // "tu billetera" en amarillo
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp)) // Espaciado entre el slogan y el botón

        Button(
            onClick = { navController.navigate("register") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7059AB)
            ),

        ) {
            Text(
                text = "Registrarse",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Texto de iniciar sesión
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¿Ya tienes una cuenta? ",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Text(
                text = "Inicia sesión aquí",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF9C8AE0), // Color violeta claro
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { navController.navigate("login") }
            )
        }
    }
}
