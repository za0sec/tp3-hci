package com.example.app_grupo13.ui.screens


import android.app.AlertDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_grupo13.R
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextFieldDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(navController: NavController) {
    var amount by remember { mutableStateOf("") }
    var aliasOrCvu by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF17171F))
            .padding(16.dp)
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Transferir",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Campo para ingresar el monto
        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Ingresar monto", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_deposit),
                    contentDescription = "Deposit Icon",
                    tint = Color(0xFF9C8AE0),
                    modifier = Modifier.size(24.dp)
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFF1C1C1E),
                focusedIndicatorColor = Color(0xFF9C8AE0)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Campo para ingresar el alias o CVU
        TextField(
            value = aliasOrCvu,
            onValueChange = { aliasOrCvu = it },
            label = { Text("Ingresar alias o CVU", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Person Icon",
                    tint = Color(0xFF9C8AE0),
                    modifier = Modifier.size(24.dp)
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFF1C1C1E),
                focusedIndicatorColor = Color(0xFF9C8AE0)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Mensaje de error
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Botón de transferir
        Button(
            onClick = {
                val validationResult = validateTransferInput(amount, aliasOrCvu)
                if (validationResult.isEmpty()) {
                    showConfirmDialog = true
                } else {
                    errorMessage = validationResult
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C8AE0)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "Transferir", color = Color.White, fontSize = 16.sp)
        }
    }

    // Diálogo de confirmación
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text(text = "Confirmar Transferencia", color = Color.White) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "¿Estás seguro de transferir $$amount a $aliasOrCvu?",
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_transfer), // Cambia el ícono al de transferencias
                        contentDescription = null,
                        tint = Color(0xFF9C8AE0),
                        modifier = Modifier.size(48.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        transferMoney(amount.toInt(), aliasOrCvu, navController)
                        showConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7059AB))
                ) {
                    Text("Confirmar", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showConfirmDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Cancelar", color = Color.White)
                }
            },
            containerColor = Color(0xFF2C2C2E),
            shape = RoundedCornerShape(16.dp)
        )
    }
}

// Valida el monto ingresado y el alias o CVU
fun validateTransferInput(amount: String, aliasOrCvu: String): String {
    if (amount.isEmpty() || aliasOrCvu.isEmpty()) return "Por favor complete ambos campos para continuar."
    if (!amount.matches(Regex("^[0-9]*$"))) return "Por favor ingrese un monto válido"
    return ""
}

// Simula la acción de transferir dinero
fun transferMoney(amount: Int, aliasOrCvu: String, navController: NavController) {
    println("Transferencia de $$amount a $aliasOrCvu realizada exitosamente.")
    navController.popBackStack()
}
