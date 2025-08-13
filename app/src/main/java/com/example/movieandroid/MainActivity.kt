package com.example.movieandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.feature.movies.presentation.navigation.MovieDestination
import com.example.movieandroid.ui.theme.MovieAndroidTheme
import com.example.navigation.MovieNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieAndroidTheme {
                val navHostController = rememberNavController()
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
}

val LocalNavController = compositionLocalOf<NavHostController> {
    error("NavController not provided")
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MovieAndroidTheme {
        Greeting("Android")
    }
}