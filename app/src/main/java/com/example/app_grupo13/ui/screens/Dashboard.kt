package com.example.app_grupo13.ui.screens

import android.content.Context
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_grupo13.R
import com.example.app_grupo13.ui.components.NavBar
import com.example.app_grupo13.ui.viewmodels.DashboardViewModel
import com.example.app_grupo13.utils.PreferencesManager
import java.util.Locale


@Composable
fun Dashboard(
    navController: NavController,
    viewModel: DashboardViewModel,
    onLanguageChange: (String) -> Unit
) {
    val context = LocalContext.current
    val currentLanguage = context.resources.configuration.locales[0].language
    println(currentLanguage)
    var selectedLanguage by remember { mutableStateOf(if (currentLanguage == "es") "Español" else "English") }
    var isBalanceVisible by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) } // Estado del diálogo
    val user = viewModel.user.value
    val isLoading = viewModel.isLoading.value
    val balance = viewModel.balance.value

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80121212)) // Semi-transparent overlay
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Column {
                        Text(stringResource(R.string.hello), color = Color.White, fontSize = 16.sp)
                        if (isLoading) {
                            Text(
                                text = stringResource(R.string.loading),
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            user?.let {
                                Text(
                                    text = "${it.firstName} ${it.lastName}",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            } ?: Text(
                                text = stringResource(R.string.user_default),
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    IconButton(
                        onClick = { showSettingsDialog = true }, // Abre el diálogo de ajustes
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ajustes",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Balance
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFFEDEDED))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(stringResource(R.string.balance), color = Color.Black, fontSize = 16.sp)
                            Icon(
                                painter = painterResource(id = if (isBalanceVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                                contentDescription = if (isBalanceVisible) 
                                    stringResource(R.string.hide_balance) 
                                else 
                                    stringResource(R.string.show_balance),
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable { isBalanceVisible = !isBalanceVisible },
                                tint = Color.Black
                            )
                        }
                        Text(
                            if (isBalanceVisible)
                                balance?.let { "$${String.format("%.2f", it)}" } ?: "Cargando..."
                            else "****",
                            color = Color.Black,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        // Botones de acción
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            ActionIcon(
                                R.drawable.ic_deposit, 
                                stringResource(R.string.deposit), 
                                Color(0xFF66344A),
                                onClick = { navController.navigate("deposit") }
                            )
                            ActionIcon(
                                R.drawable.ic_transfer, 
                                stringResource(R.string.transfer), 
                                Color(0xFFE08453),
                                onClick = { navController.navigate("transfer") }
                            )
                            ActionIcon(
                                R.drawable.ic_qr, 
                                stringResource(R.string.pay), 
                                Color(0xFFFFBC52),
                                onClick = { showPaymentDialog = true }) 
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    stringResource(R.string.services), 
                    color = Color.White, 
                    fontSize = 18.sp, 
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                GridServices(navController)

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    stringResource(R.string.special_offers), 
                    color = Color.White, 
                    fontSize = 18.sp, 
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                SpecialOffers()
            }

            NavBar(navController = navController)
        }
    }

    // Diálogo de ajustes
    if (showSettingsDialog) {
        SettingsDialog(
            onDismiss = { showSettingsDialog = false },
            onLanguageChange = { language ->
                onLanguageChange(language)
                showSettingsDialog = false
            },
            onLogout = {
                navController.navigate("login") {
                    popUpTo("dashboard") { inclusive = true }
                }
            }
        )
    }

    if (showPaymentDialog) {
        AlertDialog(
            onDismissRequest = { showPaymentDialog = false },
            title = { Text(text = stringResource(R.string.select_payment_method)) },
            text = {
                Column {
                    Text(stringResource(R.string.choose_option))
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            showPaymentDialog = false
                            navController.navigate("pay_qr")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.pay_with_qr))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            showPaymentDialog = false
                            navController.navigate("pay_with_balance")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.pay_with_balance))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            showPaymentDialog = false
                            navController.navigate("pay_with_card")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.pay_with_card))
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showPaymentDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    onLanguageChange: (String) -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    var selectedLanguage by remember { 
        mutableStateOf(PreferencesManager.getLanguage(context))
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(R.string.settings), color = Color.White) },
        text = {
            Column {
                // Opciones de idioma
                LanguageOption(
                    language = "Español",
                    selectedLanguage = selectedLanguage,
                    onSelect = { selectedLanguage = "es" }
                )
                LanguageOption(
                    language = "English",
                    selectedLanguage = selectedLanguage,
                    onSelect = { selectedLanguage = "en" }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(stringResource(R.string.cancel), color = Color.White)
                    }

                    Button(
                        onClick = {
                            onLanguageChange(selectedLanguage)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7059AB)),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(stringResource(R.string.save), color = Color.White)
                    }
                }

                Button(
                    onClick = { onLogout() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.logout), color = Color.White)
                }
            }
        },
        confirmButton = {},
        dismissButton = {},
        containerColor = Color(0xFF2C2C2E),
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun LanguageOption(language: String, selectedLanguage: String, onSelect: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(8.dp)
    ) {
        RadioButton(
            selected = (language == "Español" && selectedLanguage == "es") ||
                    (language == "English" && selectedLanguage == "en"),
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF9C8AE0))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = language, color = Color.White, fontSize = 16.sp)
    }
}


@Composable
fun ActionIcon(
    icon: Int,
    text: String,
    backgroundColor: Color = Color.Transparent,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .clickable(onClick = onClick)
                .size(60.dp)
                .background(color = backgroundColor, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = text,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
        Text(
            text = text,
            color = Color.Black,
            fontSize = 14.sp
        )
    }
}

@Composable
fun GridServices(navController: NavController) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ServiceCard(R.string.movements, R.drawable.ic_movements) { navController.navigate("movements") }
            ServiceCard(R.string.cards, R.drawable.ic_cards) { navController.navigate("cards") }
            ServiceCard(R.string.investments, R.drawable.ic_invest) { navController.navigate("invest") }
        }
    }
}

@Composable
fun ServiceCard(title: Int, icon: Int, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(8.dp)
            .size(100.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEDEDED))
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = stringResource(id = title),
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(48.dp)
                    .padding(bottom = 4.dp)
            )
            Text(
                text = stringResource(id = title),
                color = Color.Black,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpecialOffers() {
    var currentPage by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState(pageCount = { 3 })
    val offers = listOf(
        Offer(stringResource(R.string.junk_food), stringResource(R.string.junk_food_desc), R.drawable.ic_burger, Color(0xFF66344A)),
        Offer(stringResource(R.string.sports), stringResource(R.string.sports_desc), R.drawable.ic_sports, Color(0xFFE08453)), 
        Offer(stringResource(R.string.cinema), stringResource(R.string.cinema_desc), R.drawable.ic_cinema, Color(0xFFFFBC52))
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.height(200.dp)
        ) { page ->
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(offers[page].backgroundColor)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = offers[page].title,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = offers[page].description,
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    Image(
                        painter = painterResource(id = offers[page].image),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Row(
            Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(offers.size) { iteration ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray)
                        .size(if (pagerState.currentPage == iteration) 12.dp else 8.dp)
                )
            }
        }
    }
}

private fun updateLanguage(context: Context, language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    context.createConfigurationContext(config)

    (context as? ComponentActivity)?.recreate()
}

data class Offer(
    val title: String,
    val description: String,
    val image: Int,
    val backgroundColor: Color
)
