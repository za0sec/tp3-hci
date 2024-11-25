package com.example.app_grupo13.ui.screens

import android.content.Context
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_grupo13.R
import com.example.app_grupo13.ui.viewmodels.InvestmentViewModel
import com.example.app_grupo13.ui.viewmodels.InvestmentViewModelFactory
import java.text.NumberFormat
import java.util.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestmentScreen(
    navController: NavController,
    context: Context = LocalContext.current,
    viewModel: InvestmentViewModel = viewModel(factory = InvestmentViewModelFactory(context))
) {
    val currentInvestment by viewModel.currentInvestment.collectAsState()
    val dailyReturns by viewModel.dailyReturns.collectAsState()
    val dailyInterest by viewModel.dailyInterest.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    var inputAmount by remember { mutableStateOf("") }
    var showInvestDialog by remember { mutableStateOf(false) }
    var showDivestDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.reloadData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF17171F))
            .padding(16.dp)
    ) {
        // Encabezado
        Text(
            text = stringResource(R.string.investments),
            color = Color.White,
            fontSize = 28.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        
        Spacer(modifier = Modifier.height(30.dp))

        // Mostrar balance y retorno total
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.current_investment, currentInvestment?.investment ?: 0),
                    color = Color.White,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.daily_rate, dailyInterest?.interest ?: 0),
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para nueva inversión
        TextField(
            value = inputAmount,
            onValueChange = { 
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                    inputAmount = it
                }
            },
            label = { Text(stringResource(R.string.investment_amount), color = Color.Gray) },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFF1C1C1E),
                focusedIndicatorColor = Color(0xFF7059AB)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de Invertir y Desinvertir
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { showInvestDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7059AB)),
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.invest), color = Color.White)
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Button(
                onClick = { showDivestDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7059AB)),
                modifier = Modifier.weight(1f),
                enabled = currentInvestment?.investment ?: 0.0 > 0
            ) {
                Text(stringResource(R.string.divest), color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de retornos diarios
        Text(
            text = stringResource(R.string.daily_returns),
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        LazyColumn {
            items(dailyReturns) { dailyReturn ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.return_amount, dailyReturn.returnGiven),
                            color = Color.White
                        )
                        Text(
                            text = stringResource(R.string.previous_balance, dailyReturn.balanceBefore),
                            color = Color.Gray
                        )
                        Text(
                            text = stringResource(R.string.new_balance, dailyReturn.balanceAfter),
                            color = Color.Gray
                        )
                        Text(
                            text = stringResource(R.string.date, dailyReturn.createdAt),
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }

    // Diálogo de inversión
    if (showInvestDialog) {
        AlertDialog(
            onDismissRequest = { showInvestDialog = false },
            title = { Text(stringResource(R.string.confirm_investment)) },
            text = { Text(stringResource(R.string.confirm_investment_amount, inputAmount)) },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            inputAmount.toDoubleOrNull()?.let { amount ->
                                if (viewModel.invest(amount)) {
                                    inputAmount = ""
                                    showInvestDialog = false
                                }
                            }
                        }
                    }
                ) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                Button(onClick = { showInvestDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    // Diálogo de desinversión
    if (showDivestDialog) {
        AlertDialog(
            onDismissRequest = { showDivestDialog = false },
            title = { Text(stringResource(R.string.confirm_divestment)) },
            text = { Text(stringResource(R.string.confirm_divestment_amount, inputAmount)) },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            inputAmount.toDoubleOrNull()?.let { amount ->
                                if (viewModel.divest(amount)) {
                                    inputAmount = ""
                                    showDivestDialog = false
                                }
                            }
                        }
                    }
                ) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                Button(onClick = { showDivestDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
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
        // Icono correspondiente
        val icon = when (type) {
            stringResource(R.string.crypto) -> R.drawable.ic_bitcoin
            stringResource(R.string.stocks) -> R.drawable.ic_chart
            stringResource(R.string.bonds) -> R.drawable.ic_bank
            else -> null
        }
        icon?.let {
            Icon(
                painter = painterResource(id = it),
                contentDescription = type,
                tint = Color.White,
                modifier = Modifier.size(24.dp).padding(end = 8.dp)
            )
        }
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
            BalanceRow(label = stringResource(R.string.available_balance), amount = availableBalance)
            Spacer(modifier = Modifier.height(8.dp))
            BalanceRow(label = stringResource(R.string.total_invested), amount = totalInvested)
            Spacer(modifier = Modifier.height(8.dp))
            BalanceRow(label = stringResource(R.string.total_return), amount = totalReturn)
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
            Row(verticalAlignment = Alignment.CenterVertically) {

                val icon = when (investment.type) {
                    stringResource(R.string.crypto_investment) -> R.drawable.ic_bitcoin
                    stringResource(R.string.stocks_investment) -> R.drawable.ic_chart
                    stringResource(R.string.bonds_investment) -> R.drawable.ic_bank
                    else -> null
                }
                icon?.let {
                    Icon(
                        painter = painterResource(id = it),
                        contentDescription = investment.type,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp).padding(end = 8.dp)
                    )
                }
                Column {
                    Text(text = investment.type, color = Color.White, fontSize = 16.sp)
                    Text(
                        text = stringResource(R.string.amount_format, NumberFormat.getCurrencyInstance(Locale("es", "AR")).format(investment.amount)),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Text(
                        text = stringResource(R.string.daily_return_format, NumberFormat.getCurrencyInstance(Locale("es", "AR")).format(investment.dailyReturn)),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
            Button(
                onClick = onWithdraw,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = stringResource(R.string.withdraw), color = Color.White)
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
