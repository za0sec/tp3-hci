package com.example.app_grupo13.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.app_grupo13.R
import com.example.app_grupo13.ui.components.NavBar

@Composable
fun DashboardScreen(navController: NavController) {
    var isBalanceVisible by remember { mutableStateOf(false) }

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
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF8A56AC),
                                Color(0xFF6D3989)
                            )
                        ),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text("Hola,", color = Color.White, fontSize = 16.sp)
                    Text("John Doe", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
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
                        Text("Balance", color = Color.Black, fontSize = 16.sp)
                        Icon(
                            painter = painterResource(id = if (isBalanceVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                            contentDescription = if (isBalanceVisible) "Ocultar balance" else "Mostrar balance",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { isBalanceVisible = !isBalanceVisible },
                            tint = Color.Black
                        )
                    }
                    Text(
                        if (isBalanceVisible) "$150,000" else "****",
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ActionIcon(R.drawable.ic_deposit, "Depositar")
                        ActionIcon(R.drawable.ic_transfer, "Transferir")
                        ActionIcon(R.drawable.ic_pay, "Pagar")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Servicios", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            GridServices(navController)

            Spacer(modifier = Modifier.height(16.dp))

            SpecialOffers()
        }

        NavBar(navController = navController)
    }
}

@Composable
fun ActionIcon(icon: Int, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = text,
            tint = Color.Unspecified,
            modifier = Modifier.size(40.dp)
        )
        Text(text, color = Color.Black, fontSize = 14.sp)
    }
}

@Composable
fun GridServices(navController: NavController) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ServiceCard("Movimientos", R.drawable.ic_movements) { navController.navigate("movements") }
            ServiceCard("Tarjetas", R.drawable.ic_cards) { navController.navigate("cards") }
            ServiceCard("Más", R.drawable.ic_more) { navController.navigate("more") }
        }
    }
}

@Composable
fun ServiceCard(title: String, icon: Int, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFFEDEDED))
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                tint = Color.Unspecified,
                modifier = Modifier.size(40.dp)
            )
            Text(title, color = Color.Black, fontSize = 14.sp)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)

@Composable
fun SpecialOffers() {
    var currentPage by remember { mutableStateOf(0) }
    val offers = listOf(
        Offer("JUNK FOOD", "Hasta 60% de descuento", R.drawable.ic_burger, Color(0xFF8A56AC)),
        Offer("DEPORTES", "30% de descuento", R.drawable.ic_sports, Color(0xFF56AC8A)),
        Offer("CINE", "2x1 en entradas", R.drawable.ic_cinema, Color(0xFFAC8A56))
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = rememberPagerState { offers.size }
        ) { page ->
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp) // Added fixed height
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize() // Fill the entire card
                        .background(offers[page].backgroundColor)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween // Space between text and image
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            offers[page].title,
                            color = Color.White,
                            fontSize = 24.sp, // Increased font size
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            offers[page].description,
                            color = Color.White,
                            fontSize = 16.sp // Increased font size
                        )
                    }
                    Image(
                        painter = painterResource(id = offers[page].image),
                        contentDescription = "Oferta especial",
                        modifier = Modifier
                            .size(120.dp) // Increased image size
                            .padding(start = 16.dp),
                        contentScale = ContentScale.Fit // Ensures image fits properly
                    )
                }
            }
        }

        Row(
            Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(offers.size) { iteration ->
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(if (currentPage == iteration) Color.DarkGray else Color.LightGray)
                        .size(8.dp)
                )
            }
        }
    }
}

data class Offer(
    val title: String,
    val description: String,
    val image: Int,
    val backgroundColor: Color
)