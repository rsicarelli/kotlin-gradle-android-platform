package com.rsicarelli.kplatform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rsicarelli.kplatform.home.HomeScreen
import com.rsicarelli.kplatform.ui.theme.KplatformTheme

class MainActivity : ComponentActivity() {

    init {
        //test rather BuildConfig is generated
        println("Hello from debug ${BuildConfig.DEBUG}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KplatformTheme {
                HomeScreen()
            }
        }
    }
}
