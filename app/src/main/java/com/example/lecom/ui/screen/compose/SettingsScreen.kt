package com.example.lecom.ui.screen.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Settings screen composable
 * Provides an example of boolean decision logic
 */
@Composable
fun SettingsScreen(
    onNavigateToHome: () -> Unit
) {
    var cartValueText by remember { mutableStateOf("") }
    var thresholdText by remember { mutableStateOf("") }
    var decisionText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Free Shipping Calculator",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = cartValueText,
            onValueChange = { cartValueText = it },
            label = { Text("Cart Value") },
            placeholder = { Text("0.00") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = thresholdText,
            onValueChange = { thresholdText = it },
            label = { Text("Free Shipping Threshold") },
            placeholder = { Text("200.00") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = {
                val cartValue = cartValueText.toDoubleOrNull() ?: 0.0
                val threshold = thresholdText.toDoubleOrNull() ?: 200.0
                val eligible = shouldEnableFreeShipping(cartValue, threshold)
                decisionText = if (eligible) {
                    "✅ Free shipping eligible!\nCart value: $${String.format("%.2f", cartValue)}"
                } else {
                    val needed = threshold - cartValue
                    "❌ Free shipping not eligible.\nYou need $${String.format("%.2f", needed)} more"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Evaluate Free Shipping")
        }

        if (decisionText.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (decisionText.contains("✅")) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    }
                )
            ) {
                Text(
                    text = decisionText,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        OutlinedButton(
            onClick = onNavigateToHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}

/**
 * Simple rule showing how configurable thresholds can be handled.
 */
private fun shouldEnableFreeShipping(cartValue: Double, threshold: Double): Boolean =
    cartValue >= threshold

