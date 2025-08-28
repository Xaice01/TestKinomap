package com.xavier_carpentier.testkinomap.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.xavier_carpentier.testkinomap.presentation.navigation.NavigationScreen
import com.xavier_carpentier.testkinomap.presentation.theme.TestKinomapTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestKinomapTheme {
                NavigationScreen()
            }
        }
    }
}