package com.example.app_grupo13.ui.screens

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_grupo13.R
import com.example.app_grupo13.ui.components.NavBar
import com.example.app_grupo13.ui.theme.PurplePrimary
import kotlin.coroutines.coroutineContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositScreen(navController: NavController) {
    val context = LocalContext.current // Obtén el contexto dentro de un composable

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF17171F))
            .padding(16.dp)
    ) {
        // Caja donde irá la flecha de regreso
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Depositar",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        var amount by remember { mutableStateOf("") }
        // Cuánto deseas depositar y textArea
        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Ingresar monto", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_deposit),
                    contentDescription = "Person Icon",
                    tint = Color(0xFF9C8AE0)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        var errorMessage by remember { mutableStateOf("") }

        // Mensaje de error
        Text(
            text = errorMessage,
            color = Color.Red,
            fontSize = 17.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de depositar
        Button(
            onClick = {
                errorMessage = validateMoneyError(amount, context, navController)

            },
            colors = ButtonDefaults.buttonColors(Color(0xFF9C8AE0)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "Depositar",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

// Chequea que sean todos valores numéricos y verifica si el monto está vacío
fun validateMoneyError(amount: String, context: Context, navController : NavController): String {
    if (amount.isEmpty()) {
        return "Por favor ingrese un monto"
    }
    if (!amount.matches(Regex("^[0-9]*$"))) {
        return "Por favor ingrese un monto válido"
    }
    depositMoney(amount.toInt(), context, navController)
    return ""
}

// Simula la acción de depositar dinero y abre un diálogo de confirmación
fun depositMoney(amount: Int, context: Context, navController: NavController) {
    println("Depósito de $amount realizado")
    val builder = AlertDialog.Builder(context)
    builder.setMessage("Se depositará un monto de \$$amount.")
        .setTitle("Confimar depósito")
        .setPositiveButton("Confimar") { dialog, _ -> dialog.dismiss()
            navController.popBackStack()
        }
        .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }

    val dialog: AlertDialog = builder.create()
    dialog.show()

}
