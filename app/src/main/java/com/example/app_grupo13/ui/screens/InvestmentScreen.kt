package com.example.app_grupo13.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestmentScreen(navController: NavController) {
    var availableBalance by remember { mutableStateOf(10000.00) } // Dinero disponible
    var investedAmount by remember { mutableStateOf(0.00) } // Monto invertido
    var inputAmount by remember { mutableStateOf("") }
    var isInvesting by remember { mutableStateOf(true) }
    val dailyYield = 0.001 // Rendimiento diario (0.1%)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF17171F))
            .padding(16.dp)
    ) {
        // Encabezado
        Text(
            text = if (isInvesting) "Invertir Dinero" else "Rescatar Dinero",
            color = Color.White,
            fontSize = 28.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar balance
        BalanceInfo(availableBalance = availableBalance, investedAmount = investedAmount, dailyYield = dailyYield)

        Spacer(modifier = Modifier.height(16.dp))

        // Input de monto
        TextField(
            value = inputAmount,
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^[0-9]*\\.?[0-9]*\$"))) inputAmount = it
            },
            label = { Text(text = "Monto", color = Color.Gray) },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFF1C1C1E),
                focusedIndicatorColor = Color(0xFF7059AB)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botones de acción
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    val amount = inputAmount.toDoubleOrNull()
                    if (amount != null && amount <= availableBalance) {
                        availableBalance -= amount
                        investedAmount += amount
                        inputAmount = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7059AB)),
                enabled = isInvesting && inputAmount.toDoubleOrNull() ?: 0.0 <= availableBalance,
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text(text = "Invertir", color = Color.White)
            }
            Button(
                onClick = {
                    val amount = inputAmount.toDoubleOrNull()
                    if (amount != null && amount <= investedAmount) {
                        availableBalance += amount
                        investedAmount -= amount
                        inputAmount = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7059AB)),
                enabled = !isInvesting && inputAmount.toDoubleOrNull() ?: 0.0 <= investedAmount,
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text(text = "Rescatar", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cambiar entre invertir y rescatar
        Text(
            text = if (isInvesting) "¿Quieres rescatar dinero?" else "¿Quieres invertir dinero?",
            color = Color.Gray,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { isInvesting = !isInvesting }
                .padding(8.dp)
        )
    }
}

@Composable
fun BalanceInfo(availableBalance: Double, investedAmount: Double, dailyYield: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            BalanceRow(label = "Dinero disponible", amount = availableBalance)
            Spacer(modifier = Modifier.height(8.dp))
            BalanceRow(label = "Monto invertido", amount = investedAmount)
            Spacer(modifier = Modifier.height(8.dp))
            BalanceRow(label = "Rendimiento diario", amount = investedAmount * dailyYield)
        }
    }
}

@Composable
fun BalanceRow(label: String, amount: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 16.sp)
        Text(
            text = NumberFormat.getCurrencyInstance(Locale("es", "AR")).format(amount),
            color = Color.White,
            fontSize = 16.sp
        )
    }
}