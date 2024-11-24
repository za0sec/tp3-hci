package com.example.app_grupo13.ui.screens

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_grupo13.R
import com.example.app_grupo13.data.model.Card
import com.example.app_grupo13.data.model.CardType
import com.example.app_grupo13.ui.viewmodels.CardsViewModel
import com.example.app_grupo13.ui.viewmodels.CardsViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsScreen(
    navController: NavController,
    context: Context = LocalContext.current,
    viewModel: CardsViewModel = viewModel(factory = CardsViewModelFactory(context))
) {
    var showDialog by remember { mutableStateOf(false) }
    val cards by viewModel.cards.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadCards()
    }

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

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF7059AB))
            }
        }

        error?.let { errorMsg ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMsg,
                    color = Color.Red,
                    fontSize = 16.sp
                )
            }
        }

        // Lista de tarjetas
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(cards) { card ->
                CardItem(
                    card = card,
                    onDelete = { 
                        scope.launch {
                            viewModel.deleteCard(card.id!!)
                        }
                    }
                )
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
        AddCardDialog(
            onDismiss = { showDialog = false }
        ) { cardData ->
            scope.launch {
                val newCard = Card(
                    number = cardData.cardNumber.replace(" ", ""),
                    fullName = cardData.cardHolder,
                    expirationDate = cardData.expiryDate,
                    cvv = cardData.cvv,
                    type = CardType.CREDIT
                )
                if (viewModel.addCard(newCard)) {
                    showDialog = false
                }
            }
        }
    }
}

@Composable
fun CardItem(card: Card, onDelete: () -> Unit) {
    var isFlipped by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(isFlipped) {
        rotation.animateTo(
            targetValue = if (isFlipped) 180f else 0f,
            animationSpec = tween(durationMillis = 400)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp)
            .clickable { isFlipped = !isFlipped }
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 12f * density
            }
    ) {
        if (rotation.value <= 90f) {
            FrontCardContent(card = card, onDelete = onDelete)
        } else {
            BackCardContent(card = card)
        }
    }
}

@Composable
fun FrontCardContent(card: Card, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF8E24AA), Color(0xFFC980D5))
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = card.type.name,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "Eliminar tarjeta",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onDelete() }
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = card.number.chunked(4).joinToString(" "),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = card.fullName,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Text(
                        text = card.expirationDate,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun BackCardContent(card: Card) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { rotationY = 180f },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF8E24AA), Color(0xFFC980D5))
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color.Black.copy(alpha = 0.3f))
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                card.cvv?.let { cvv ->
                    Text(
                        text = "CVV: $cvv",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardDialog(
    onDismiss: () -> Unit,
    onAddCard: (CardData) -> Unit
) {
    var cardNumber by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Tarjeta") },
        text = {
            Column {
                TextField(
                    value = cardNumber,
                    onValueChange = { 
                        if (it.length <= 19) { // 16 dígitos + 3 espacios
                            cardNumber = formatCardNumber(it)
                        }
                    },
                    label = { Text("Número de Tarjeta") },
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                TextField(
                    value = cardHolder,
                    onValueChange = { cardHolder = it },
                    label = { Text("Titular de la Tarjeta") },
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row {
                    TextField(
                        value = expiryDate,
                        onValueChange = { 
                            if (it.length <= 5) {
                                expiryDate = formatExpiryDate(it)
                            }
                        },
                        label = { Text("MM/YY") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    TextField(
                        value = cvv,
                        onValueChange = { 
                            if (it.length <= 3) {
                                cvv = it.filter { char -> char.isDigit() }
                            }
                        },
                        label = { Text("CVV") },
                        modifier = Modifier.width(100.dp),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isValidCard(cardNumber, cardHolder, expiryDate, cvv)) {
                        onAddCard(CardData(cardNumber, cardHolder, expiryDate, cvv))
                    }
                }
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

private fun formatCardNumber(input: String): String {
    val digitsOnly = input.filter { it.isDigit() }
    return digitsOnly.chunked(4).joinToString(" ")
}

private fun formatExpiryDate(input: String): String {
    val digitsOnly = input.filter { it.isDigit() }
    return when {
        digitsOnly.length >= 2 -> "${digitsOnly.take(2)}/${digitsOnly.drop(2)}"
        else -> digitsOnly
    }
}

private fun isValidCard(
    cardNumber: String,
    cardHolder: String,
    expiryDate: String,
    cvv: String
): Boolean {
    val digitsOnly = cardNumber.filter { it.isDigit() }
    return digitsOnly.length == 16 &&
            cardHolder.isNotBlank() &&
            expiryDate.length == 5 &&
            cvv.length == 3
}

data class CardData(
    val cardNumber: String,
    val cardHolder: String,
    val expiryDate: String,
    val cvv: String
)
