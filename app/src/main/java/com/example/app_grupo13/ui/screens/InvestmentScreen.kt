package com.example.app_grupo13.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestmentScreen(navController: NavController) {
    var availableBalance by remember { mutableStateOf(10000.00) } // Dinero disponible
    var totalInvested by remember { mutableStateOf(0.00) } // Total invertido
    var totalReturn by remember { mutableStateOf(0.00) } // Retorno total
    var inputAmount by remember { mutableStateOf("") }
    var selectedInvestmentType by remember { mutableStateOf("Crypto") } // Tipo de inversión seleccionado
    var showTypeDialog by remember { mutableStateOf(false) } // Mostrar diálogo para seleccionar tipo
    var showConfirmDialog by remember { mutableStateOf(false) } // Mostrar diálogo de confirmación

    // Lista de inversiones activas
    val investments = remember { mutableStateListOf<Investment>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF17171F))
            .padding(16.dp)
    ) {
        // Encabezado
        Text(
            text = "Inversiones Activas",
            color = Color.White,
            fontSize = 28.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar balance y retorno total
        BalanceCard(availableBalance = availableBalance, totalInvested = totalInvested, totalReturn = totalReturn)

        Spacer(modifier = Modifier.height(16.dp))

        // Sección para nueva inversión
        TextField(
            value = inputAmount,
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^[0-9]*\\.?[0-9]*\$"))) inputAmount = it
            },
            label = { Text(text = "Monto a invertir", color = Color.Gray) },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFF1C1C1E),
                focusedIndicatorColor = Color(0xFF7059AB)
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { showTypeDialog = true }, // Mostrar diálogo de selección de tipo
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7059AB)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Seleccionar Tipo e Invertir", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de inversiones activas
        Text(
            text = "Inversiones Activas",
            color = Color.Gray,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(investments) { investment ->
                InvestmentCard(
                    investment = investment,
                    onWithdraw = {
                        investments.remove(investment)
                        availableBalance += investment.amount
                        totalInvested -= investment.amount
                        totalReturn += investment.dailyReturn * 30 // Simulación de retorno mensual
                    }
                )
            }
        }
    }

    // Diálogo para seleccionar el tipo de inversión
    if (showTypeDialog) {
        AlertDialog(
            onDismissRequest = { showTypeDialog = false },
            title = { Text(text = "Seleccionar Tipo de Inversión") },
            text = {
                Column {
                    InvestmentTypeOption("Crypto", selectedInvestmentType) { selectedInvestmentType = it }
                    InvestmentTypeOption("Acciones", selectedInvestmentType) { selectedInvestmentType = it }
                    InvestmentTypeOption("Bonos", selectedInvestmentType) { selectedInvestmentType = it }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showTypeDialog = false
                        showConfirmDialog = true // Mostrar diálogo de confirmación
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(onClick = { showTypeDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo de confirmación antes de invertir
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text(text = "Confirmar Inversión") },
            text = {
                Text(
                    text = "¿Estás seguro de invertir $${inputAmount} en $selectedInvestmentType?",
                    color = Color.Gray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = inputAmount.toDoubleOrNull()
                        if (amount != null && amount <= availableBalance) {
                            investments.add(
                                Investment(
                                    type = "Inversión en $selectedInvestmentType",
                                    amount = amount,
                                    dailyReturn = amount * 0.001
                                )
                            )
                            availableBalance -= amount
                            totalInvested += amount
                            inputAmount = ""
                        }
                        showConfirmDialog = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(onClick = { showConfirmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun InvestmentTypeOption(
    type: String,
    selectedType: String,
    onSelect: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(type) }
            .padding(8.dp)
    ) {
        RadioButton(
            selected = type == selectedType,
            onClick = { onSelect(type) },
            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF7059AB))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = type, color = Color.Gray, fontSize = 16.sp)
    }
}

@Composable
fun BalanceCard(availableBalance: Double, totalInvested: Double, totalReturn: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            BalanceRow(label = "Balance Disponible", amount = availableBalance)
            Spacer(modifier = Modifier.height(8.dp))
            BalanceRow(label = "Total Invertido", amount = totalInvested)
            Spacer(modifier = Modifier.height(8.dp))
            BalanceRow(label = "Retorno Total", amount = totalReturn)
        }
    }
}

@Composable
fun InvestmentCard(investment: Investment, onWithdraw: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2E)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = investment.type, color = Color.White, fontSize = 16.sp)
                Text(
                    text = "Monto: ${NumberFormat.getCurrencyInstance(Locale("es", "AR")).format(investment.amount)}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Rendimiento Diario: ${NumberFormat.getCurrencyInstance(Locale("es", "AR")).format(investment.dailyReturn)}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            Button(
                onClick = onWithdraw,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = "Rescatar", color = Color.White)
            }
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

data class Investment(
    val type: String,
    val amount: Double,
    val dailyReturn: Double
)
