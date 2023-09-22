package com.rsicarelli.kplatform.features.details

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

@Composable
fun DetailsScreen() {
    Text("Olá mundo!")
}

fun main() {
    flowOf(true).flatMapLatest { flowOf(false) }
}
