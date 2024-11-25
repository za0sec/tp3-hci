package com.example.app_grupo13.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_grupo13.data.model.Card
import com.example.app_grupo13.ui.viewmodels.CardsViewModel
import com.example.app_grupo13.ui.viewmodels.CardsViewModelFactory
import com.example.app_grupo13.ui.viewmodels.PaymentViewModel
import com.example.app_grupo13.ui.viewmodels.PaymentViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayWithCardScreen(
    navController: NavController,
    cardsViewModel: CardsViewModel = viewModel(factory = CardsViewModelFactory(LocalContext.current)),
    paymentViewModel: PaymentViewModel = viewModel(factory = PaymentViewModelFactory(LocalContext.current))
) {
    var selectedCard by remember { mutableStateOf<Card?>(null) }
    var amount by remember { mutableStateOf("") }
    var receiverEmail by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf(false) }
    
    val cards by cardsViewModel.cards.collectAsState()
    val isLoading by paymentViewModel.isLoading.collectAsState()
    val error by paymentViewModel.error.collectAsState()
    val success by paymentViewModel.success.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        cardsViewModel.loadCards()
    }

    LaunchedEffect(success) {
        if (success == true) {
            // Pago exitoso, navegar de vuelta
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF17171F))
            .padding(16.dp)
    ) {
        // Header
        IconButton(
            onClick = { navController.popBackStack() }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver",
                tint = Color.White
            )
        }

        Text(
            text = "Pagar con Tarjeta",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Selección de tarjeta
        Text(
            text = "Selecciona una tarjeta",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth()
        ) {
            items(cards) { card ->
                CardSelectionItem(
                    card = card,
                    isSelected = card == selectedCard,
                    onClick = { selectedCard = card }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campos de entrada
        OutlinedTextField(
            value = amount,
            onValueChange = { 
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                    amount = it
                }
            },
            label = { Text("Monto") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = receiverEmail,
            onValueChange = { receiverEmail = it },
            label = { Text("Email del destinatario") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de pago
        Button(
            onClick = { showConfirmDialog = true },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedCard != null && amount.isNotEmpty() && receiverEmail.isNotEmpty() && !isLoading
        ) {
            Text("Realizar Pago")
        }

        // Mostrar errores
        error?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }

    // Diálogo de confirmación
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirmar Pago") },
            text = { 
                Text("¿Deseas realizar el pago de $${amount} a ${receiverEmail}?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            amount.toDoubleOrNull()?.let { amountDouble ->
                                paymentViewModel.makePayment(amountDouble, receiverEmail)
                            }
                            showConfirmDialog = false
                        }
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun CardSelectionItem(
    card: Card,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF7059AB) else Color(0xFF2A2A2A)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "**** **** **** ${card.number.takeLast(4)}",
                    color = Color.White,
                    fontSize = 16.sp
                )
                Text(
                    text = card.fullName,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
            Text(
                text = card.expirationDate,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }
    }
} 