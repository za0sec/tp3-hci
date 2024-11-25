package com.example.app_grupo13.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_grupo13.ui.viewmodels.PaymentViewModel
import com.example.app_grupo13.ui.viewmodels.PaymentViewModelFactory
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource
import com.example.app_grupo13.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayWithBalanceScreen(
    navController: NavController,
    viewModel: PaymentViewModel = viewModel(factory = PaymentViewModelFactory(LocalContext.current))
) {
    var amount by remember { mutableStateOf("") }
    var receiverEmail by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf(false) }
    
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val success by viewModel.success.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(success) {
        if (success == true) {
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF17171F))
            .padding(16.dp)
    ) {
        IconButton(
            onClick = { navController.popBackStack() }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back_arrow),
                tint = Color.White
            )
        }

        Text(
            text = stringResource(R.string.pay_with_balance),
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        OutlinedTextField(
            value = amount,
            onValueChange = { 
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                    amount = it
                }
            },
            label = { Text(stringResource(R.string.amount)) },
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
            label = { Text(stringResource(R.string.receiver_email)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showConfirmDialog = true },
            modifier = Modifier.fillMaxWidth(),
            enabled = amount.isNotEmpty() && receiverEmail.isNotEmpty() && !isLoading
        ) {
            Text(stringResource(R.string.make_payment))
        }

        error?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text(stringResource(R.string.confirm_payment)) },
            text = { 
                Text(stringResource(R.string.payment_confirmation, amount, receiverEmail))
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            amount.toDoubleOrNull()?.let { amountDouble ->
                                viewModel.makePayment(amountDouble, receiverEmail)
                            }
                            showConfirmDialog = false
                        }
                    }
                ) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
} 