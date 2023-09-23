/*
 * Copyright (c) Rodrigo Sicarelli 2023.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.rsicarelli.kplatform.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rsicarelli.kplatform.features.details.DetailsScreen

@Composable
fun HomeScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        DetailsScreen()
    }
}
