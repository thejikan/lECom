package com.example.lecom.ui.screen.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lecom.ui.component.GlassContainer
import androidx.compose.ui.graphics.Color

/**
 * Details screen composable
 * Shows product detail logic and navigation to settings
 */
@Composable
fun DetailsScreen(
    onNavigateToSettings: () -> Unit
) {
    var productName by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var discountText by remember { mutableStateOf("") }
    var summaryText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Product Details",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Product Name") },
            placeholder = { Text("Sample Sneakers") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = priceText,
            onValueChange = { priceText = it },
            label = { Text("Price") },
            placeholder = { Text("89.99") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = discountText,
            onValueChange = { discountText = it },
            label = { Text("Discount %") },
            placeholder = { Text("15") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = {
                val name = productName.ifBlank { "Sample Sneakers" }
                val price = priceText.toDoubleOrNull() ?: 89.99
                val discount = discountText.toIntOrNull() ?: 15
                summaryText = buildProductSummary(name, price, discount)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate Summary")
        }

        if (summaryText.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = summaryText,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        OutlinedButton(
            onClick = onNavigateToSettings,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Settings")
        }

        GlassContainer(
            modifier = Modifier.padding(16.dp),
            width = 300.dp,
            height = 200.dp,
            borderRadius = 24.dp,
            blur = 12.dp,
            opacity = 0.25f
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Your content here
                Text(
                    text = summaryText,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                            color = Color.Red
                )
            }
        }
    }
}

/**
 * Demonstrates simple business logic that calculates a discounted price
 * and returns a user-friendly summary.
 */
private fun buildProductSummary(name: String, price: Double, discount: Int): String {
    val discountedPrice = price * (1 - (discount / 100.0))
    val savings = price - discountedPrice
    return """
        Product: $name
        Original Price: $${String.format("%.2f", price)}
        Discount: $discount%
        Final Price: $${String.format("%.2f", discountedPrice)}
        You Save: $${String.format("%.2f", savings)}
    """.trimIndent()
}

