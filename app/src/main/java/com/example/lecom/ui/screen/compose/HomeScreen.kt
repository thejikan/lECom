package com.example.lecom.ui.screen.compose

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lecom.ui.screen.activity.SummaryActivity
import kotlin.random.Random

/**
 * Home screen composable
 * Presents a high-level overview and sample logic
 */
@Composable
fun HomeScreen(
    onNavigateToDetails: () -> Unit
) {
    var resultText by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
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

