package com.example.app_grupo13.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_grupo13.R
import java.time.LocalDate
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.TextRange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsScreen(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    val cardList = remember { mutableStateListOf<CardData>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF17171F))
    ) {
        // Encabezado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Tarjetas",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Lista de tarjetas
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(cardList) { card ->
                CardItem(card = card, onDelete = { cardList.remove(card) })
            }
        }

        // Botón flotante para agregar tarjeta
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp),
            containerColor = Color(0xFF7059AB),
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar tarjeta"
            )
        }
    }

    // Diálogo para agregar tarjeta
    if (showDialog) {
        AddCardDialog(onDismiss = { showDialog = false }) { newCard ->
            cardList.add(newCard)
            showDialog = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardDialog(onDismiss: () -> Unit, onAddCard: (CardData) -> Unit) {
    var cardNumber by remember { mutableStateOf(TextFieldValue("")) }
    var cardNumberError by remember { mutableStateOf(false) }
    var cardHolder by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf(TextFieldValue("")) }
    var expiryDateError by remember { mutableStateOf("") }
    var securityCode by remember { mutableStateOf("") }
    var isExpired by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = {
                    if (cardNumber.text.isNotEmpty() && !cardNumberError &&
                        expiryDate.text.length == 5 && expiryDateError.isEmpty() && !isExpired &&
                        cardHolder.isNotEmpty() && securityCode.isNotEmpty()
                    ) {
                        onAddCard(CardData(cardNumber.text, cardHolder, expiryDate.text, securityCode))
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7059AB)),
            ) {
                Text(text = "Guardar tarjeta", color = Color.White)
            }
        },
        title = { Text(text = "Agregar tarjeta", color = Color.White) },
        text = {
            Column {
                // Campo del número de tarjeta
                TextField(
                    value = cardNumber,
                    onValueChange = { input ->
                        val rawInput = input.text.replace(" ", "")
                        if (rawInput.length <= 16) { // Límite de 16 dígitos
                            cardNumberError = !rawInput.all { it.isDigit() }
                            if (!cardNumberError) {
                                val formatted = rawInput.chunked(4).joinToString(" ")
                                val cursorPosition = input.selection.start
                                val newCursorPosition = cursorPosition + (formatted.length - input.text.length)
                                cardNumber = TextFieldValue(formatted, TextRange(newCursorPosition))
                            } else {
                                cardNumber = input
                            }
                        }
                    },
                    label = { Text("Número de tarjeta", color = Color.Gray) },
                    isError = cardNumberError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (cardNumberError) {
                    Text(
                        text = "Ingrese un número de tarjeta válido",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Titular de la cuenta
                TextField(
                    value = cardHolder,
                    onValueChange = { cardHolder = it },
                    label = { Text("Titular de la cuenta", color = Color.Gray) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Fecha de vencimiento
                TextField(
                    value = expiryDate,
                    onValueChange = { input ->
                        val rawInput = input.text.replace("/", "")
                        if (rawInput.length <= 4) { // Límite de 4 caracteres para MM/AA
                            val formatted = when {
                                rawInput.length > 2 -> rawInput.substring(0, 2) + "/" + rawInput.substring(2)
                                else -> rawInput
                            }
                            expiryDate = TextFieldValue(formatted, TextRange(formatted.length))

                            // Validación del mes
                            val month = if (rawInput.length >= 2) rawInput.substring(0, 2).toIntOrNull() else null
                            val year = if (rawInput.length == 4) rawInput.substring(2, 4).toIntOrNull() else null

                            expiryDateError = when {
                                month == null || month !in 1..12 -> if (rawInput.length >= 2) "Mes inválido" else ""
                                else -> ""
                            }

                            // Validación de vencimiento (solo si el formato está completo)
                            if (month != null && year != null && rawInput.length == 4) {
                                val fullYear = 2000 + year
                                isExpired = fullYear < LocalDate.now().year ||
                                        (fullYear == LocalDate.now().year && month < LocalDate.now().monthValue)
                            } else {
                                isExpired = false
                            }
                        }
                    },
                    label = { Text("Fecha de vencimiento mm/aa", color = Color.Gray) },
                    isError = expiryDateError.isNotEmpty() || isExpired,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (expiryDateError.isNotEmpty()) {
                    Text(
                        text = expiryDateError,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                if (isExpired) {
                    Text(
                        text = "La tarjeta está vencida",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Código de seguridad
                TextField(
                    value = securityCode,
                    onValueChange = {
                        if (it.length <= 3) securityCode = it
                    },
                    label = { Text("Código de seguridad", color = Color.Gray) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

@Composable
fun CardItem(card: CardData, onDelete: () -> Unit) {
    var isFlipped by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(isFlipped) {
        val targetRotation = if (isFlipped) 180f else 0f
        rotation.animateTo(targetRotation, animationSpec = tween(durationMillis = 600))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(8.dp)
            .clickable { isFlipped = !isFlipped }
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 12f * density
            },
        contentAlignment = Alignment.Center
    ) {
        if (rotation.value <= 90f) {
            FrontCardContent(card = card, onDelete = onDelete)
        } else {
            BackCardContent(card = card)
        }
    }
}

@Composable
fun FrontCardContent(card: CardData, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF8E24AA), Color(0xFFC980D5))
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo Plum",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                    Row {
                        Text(
                            text = card.expiryDate,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = "Eliminar tarjeta",
                            tint = Color.Red,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { onDelete() } // Acción para eliminar tarjeta
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = card.cardNumber, color = Color.White, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = card.cardHolder, color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun BackCardContent(card: CardData) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                rotationY = 180f // Invertimos el contenido del reverso
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF8E24AA), Color(0xFFC980D5))
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Código de seguridad (CVV): ${card.cvv}",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

data class CardData(
    val cardNumber: String,
    val cardHolder: String,
    val expiryDate: String,
    val cvv: String
)
