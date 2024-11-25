package com.example.app_grupo13.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_grupo13.R
import com.example.app_grupo13.data.model.Payment
import com.example.app_grupo13.ui.components.NavBar
import com.example.app_grupo13.ui.viewmodels.PaymentViewModel
import com.example.app_grupo13.ui.viewmodels.PaymentViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MovementsScreen(
    navController: NavController,
    viewModel: PaymentViewModel = viewModel(factory = PaymentViewModelFactory(LocalContext.current))
) {
    val payments by viewModel.payments.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var currentPage by remember { mutableStateOf(1) }
    var canLoadMore by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.loadPayments(page = currentPage)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF17171F))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = Color.White
                )
            }
            Text(
                text = stringResource(R.string.movements),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Content
        Box(modifier = Modifier.weight(1f)) {
            when {
                error != null -> {
                    Text(
                        text = error ?: stringResource(R.string.unknown_error),
                        color = Color.Red,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                payments.isEmpty() && !isLoading -> {
                    Text(
                        text = stringResource(R.string.no_movements),
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(payments) { payment ->
                            PaymentItem(payment = payment)
                        }

                        item {
                            if (isLoading) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = Color(0xFF7059AB))
                                }
                            }
                        }

                        // Detectar cuando llegamos al final para cargar más
                        item {
                            if (!isLoading) {
                                LaunchedEffect(Unit) {
                                    viewModel.loadMorePayments()
                                }
                            }
                        }
                    }
                }
            }
        }

        NavBar(navController = navController)
    }
}

@Composable
fun PaymentItem(payment: Payment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEDEDED))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Icono y detalles
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(
                        id = when (payment.type) {
                            "CARD" -> R.drawable.ic_cards
                            "BALANCE" -> R.drawable.ic_transfer
                            else -> R.drawable.ic_movements
                        }
                    ),
                    contentDescription = null,
                    tint = when (payment.type) {
                        "CARD" -> Color(0xFF66344A)
                        "BALANCE" -> Color(0xFFE08453)
                        else -> Color(0xFF4A347F)
                    },
                    modifier = Modifier.size(40.dp)
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        text = when (payment.type) {
                            "CARD" -> stringResource(R.string.pay_with_card)
                            "BALANCE" -> stringResource(R.string.transfer)
                            else -> stringResource(R.string.movements)
                        },
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    payment.createdAt?.let { date ->
                        Text(
                            text = formatDate(date),
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Monto
            payment.amount?.let { amount ->
                Text(
                    text = "$${String.format("%.2f", amount)}",
                    color = if (amount < 0) Color.Red else Color(0xFF34A853),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

private fun formatDate(dateString: String): String {
    try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date!!)
    } catch (e: Exception) {
        return dateString
    }
} 