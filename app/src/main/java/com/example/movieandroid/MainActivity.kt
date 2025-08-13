package com.example.movieandroid

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.feature.movies.presentation.navigation.MovieDestination
import com.example.movieandroid.ui.theme.MovieAndroidTheme
import com.example.navigation.MovieNavHost
import com.example.navigation.handleDeepLink
import dagger.hilt.android.AndroidEntryPoint
import android.net.Uri

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        println("🚀 MainActivity onCreate")
        println("🚀 Intent action: ${intent.action}")
        println("🚀 Intent data: ${intent.data}")

        setContent {
            MovieAndroidTheme {
                val navHostController = rememberNavController()
                LaunchedEffect(intent) {
                    println("🚀 Intent data: ${intent.data}")
                    handleDeepLink(intent = intent, navController = navHostController)
                }
                CompositionLocalProvider(LocalNavController provides navHostController) {
                    MovieNavHost(
                        modifier = Modifier,
                        navController = navHostController,
                        startDestination = MovieDestination::class
                    )
                }
            }
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}

val LocalNavController = compositionLocalOf<NavHostController> {
    error("NavController not provided")
}
