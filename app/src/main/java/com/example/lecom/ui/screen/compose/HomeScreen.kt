package com.example.lecom.ui.screen.compose

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.*
import androidx.compose.ui.graphics.*
//import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lecom.ui.component.AdvancedProductCard
import com.example.lecom.ui.screen.activity.SummaryActivity
import kotlin.random.Random

/**
 * Home screen composable
 * Presents a high-level overview and sample logic
 */
@SuppressLint("DefaultLocale")
@Composable
fun HomeScreen(
    onNavigateToDetails: () -> Unit
) {
    var resultText by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background from URL using Coil
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://images.unsplash.com/photo-1523275335684-37898b6baf30")
                .crossfade(true)
                .build(),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )

        // Optional gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.4f)
                        )
                    )
                )
        )

        // Rest of your content...
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Home Screen",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Welcome to lECom Dashboard",
                style = MaterialTheme.typography.bodyLarge
            )
            AdvancedProductCard(
                imageUrl = "https://images.unsplash.com/photo-1523275335684-37898b6baf30",
                title = "Watch",
                price = "₹2,999"
            )

            Button(
                onClick = {
                    val orders = generateMockOrders()
                    val total = calculateDailyRevenue(orders)
                    resultText = "Processed ${orders.size} orders\nTotal Revenue: $${String.format("%.2f", total)}"
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Process Daily Orders")
            }

            if (resultText.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = resultText,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            OutlinedButton(
                onClick = onNavigateToDetails,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go to Details")
            }

            TextButton(
                onClick = {
                    context.startActivity(Intent(context, SummaryActivity::class.java))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open Summary Page")
            }
        }
    }
}

/**
 * Simple business logic example that sums daily revenue
 */
private fun calculateDailyRevenue(orders: List<Double>): Double = orders.sum()

/**
 * Generates random order amounts to feed into the logic
 */
private fun generateMockOrders(): List<Double> {
    return List(size = Random.nextInt(3, 8)) {
        Random.nextDouble(25.0, 150.0)
    }
}

