package com.example.app_grupo13.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_grupo13.R
import com.example.app_grupo13.ui.components.NavBar

@Composable
fun MovementsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            // Encabezado con logo, texto y botones
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo Plum",
                        modifier = Modifier.size(36.dp),
                        tint = Color.Unspecified // Deja el color original del ícono
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Espaciado entre el logo y el texto
                    Text(
                        "Movimientos",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row {
                    IconButton(onClick = { /* Acción del botón ordenar */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sort),
                            contentDescription = "Ordenar",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { /* Acción del botón filtrar */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filtro),
                            contentDescription = "Filtrar",
                            tint = Color.White
                        )
                    }
                }
            }

            // Lista de transacciones
            val transactions = listOf(
                Transaction("Jane Doe", -3000.00),
                Transaction("John Doe", 54000.00),
                Transaction("John Doe", 500.00),
                Transaction("Jane Doe", -83000.50),
                Transaction("John Doe", 1200.00),
                Transaction("John Doe", 1500.00)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(transactions) { transaction ->
                    TransactionCard(transaction)
                }
            }
        }
        NavBar(navController = navController)
    }
}


data class Transaction(
    val name: String,
    val amount: Double
)

@Composable
fun TransactionCard(transaction: Transaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F1F))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = if (transaction.amount < 0) R.drawable.arrow_down else R.drawable.arrow_up),
                contentDescription = if (transaction.amount < 0) "Gasto" else "Ingreso",
                tint = if (transaction.amount < 0) Color.Red else Color.Green
            )
            
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = transaction.name,
                    color = Color.White,
                    fontSize = 16.sp
                )
                Text(
                    text = if (transaction.amount < 0) 
                        "-$${String.format("%.2f", -transaction.amount)}" 
                    else 
                        "+$${String.format("%.2f", transaction.amount)}",
                    color = if (transaction.amount < 0) Color.Red else Color.Green,
                    fontSize = 14.sp
                )
            }
        }
    }
}