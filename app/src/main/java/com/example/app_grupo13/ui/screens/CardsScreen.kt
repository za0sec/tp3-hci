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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
                    contentDescription = stringResource(R.string.back),
                    tint = Color.White
                )
            }
        }

        // Encabezado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.cards),
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
                    text = stringResource(R.string.unknown_error),
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
                contentDescription = stringResource(R.string.add_card)
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
    var showDeleteDialog by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(isFlipped) {
        rotation.animateTo(
            targetValue = if (isFlipped) 180f else 0f,
            animationSpec = tween(durationMillis = 400)
        )
    }

    // Diálogo de confirmación para eliminar
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.confirm_delete)) },
            text = { Text(stringResource(R.string.confirm_delete_card)) },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
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
            FrontCardContent(
                card = card,
                onDelete = { showDeleteDialog = true }
            )
        } else {
            BackCardContent(card = card)
        }
    }
}

@Composable
fun FrontCardContent(card: Card, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
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
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = card.type.name,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = stringResource(R.string.delete_card),
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(onClick = onDelete)
                    )
                }
                
                Text(
                    text = card.number.chunked(4).joinToString(" "),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.card_holder),
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = card.fullName.uppercase(),
                            color = Color.White,
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = stringResource(R.string.expires),
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
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
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.cvv),
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = cvv,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
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

    // Estados de error
    var cardNumberError by remember { mutableStateOf<String?>(null) }
    var cardHolderError by remember { mutableStateOf<String?>(null) }
    var expiryDateError by remember { mutableStateOf<String?>(null) }
    var cvvError by remember { mutableStateOf<String?>(null) }

    // Validaciones en tiempo real
    if (cardNumber.isNotEmpty()) {
        validateCardNumber(cardNumber) { error -> cardNumberError = error }
    }

    if (cardHolder.isNotEmpty()) {
        validateCardHolder(cardHolder) { error -> cardHolderError = error }
    }

    if (expiryDate.isNotEmpty()) {
        validateExpiryDate(expiryDate) { error -> expiryDateError = error }
    }

    if (cvv.isNotEmpty()) {
        validateCvv(cvv) { error -> cvvError = error }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_card)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { 
                        if (it.length <= 19) {
                            cardNumber = formatCardNumber(it)
                        }
                    },
                    label = { Text(stringResource(R.string.card_number)) },
                    isError = cardNumberError != null,
                    supportingText = { cardNumberError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = cardHolder,
                    onValueChange = { cardHolder = it },
                    label = { Text(stringResource(R.string.card_holder)) },
                    isError = cardHolderError != null,
                    supportingText = { cardHolderError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = expiryDate,
                        onValueChange = { 
                            if (it.length <= 5) {
                                expiryDate = formatExpiryDate(it)
                            }
                        },
                        label = { Text(stringResource(R.string.expiry_date)) },
                        modifier = Modifier.weight(1f),
                        isError = expiryDateError != null,
                        supportingText = { expiryDateError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { 
                            if (it.length <= 3) {
                                cvv = it.filter { char -> char.isDigit() }
                            }
                        },
                        label = { Text(stringResource(R.string.cvv)) },
                        modifier = Modifier.width(100.dp),
                        isError = cvvError != null,
                        supportingText = { cvvError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (cardNumberError == null && cardHolderError == null && 
                        expiryDateError == null && cvvError == null) {
                        onAddCard(CardData(cardNumber, cardHolder, expiryDate, cvv))
                    }
                },
                enabled = cardNumber.isNotEmpty() && cardHolder.isNotEmpty() && 
                         expiryDate.isNotEmpty() && cvv.isNotEmpty() &&
                         cardNumberError == null && cardHolderError == null && 
                         expiryDateError == null && cvvError == null
            ) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
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

// Funciones de validación actualizadas
@Composable
private fun validateCardNumber(number: String, onError: (String?) -> Unit) {
    onError(when {
        number.isEmpty() -> stringResource(R.string.card_number_required)
        number.filter { it.isDigit() }.length != 16 -> stringResource(R.string.card_number_invalid)
        else -> null
    })
}

@Composable
private fun validateCardHolder(holder: String, onError: (String?) -> Unit) {
    onError(when {
        holder.isEmpty() -> stringResource(R.string.card_holder_required)
        holder.length < 3 -> stringResource(R.string.card_holder_too_short)
        else -> null
    })
}

@Composable
private fun validateExpiryDate(date: String, onError: (String?) -> Unit) {
    onError(when {
        date.isEmpty() -> stringResource(R.string.expiry_date_required)
        date.length != 5 -> stringResource(R.string.expiry_date_invalid_format)
        else -> {
            val parts = date.split("/")
            if (parts.size != 2) {
                stringResource(R.string.expiry_date_invalid_format)
            } else {
                val month = parts[0].toIntOrNull()
                val year = parts[1].toIntOrNull()
                when {
                    month == null || year == null -> stringResource(R.string.expiry_date_invalid_format)
                    month !in 1..12 -> stringResource(R.string.expiry_date_invalid_month)
                    else -> null
                }
            }
        }
    })
}

@Composable
private fun validateCvv(cvv: String, onError: (String?) -> Unit) {
    onError(when {
        cvv.isEmpty() -> stringResource(R.string.cvv_required)
        cvv.length != 3 -> stringResource(R.string.cvv_invalid_length)
        !cvv.all { it.isDigit() } -> stringResource(R.string.cvv_invalid_format)
        else -> null
    })
}
