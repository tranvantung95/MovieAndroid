package com.example.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.feature.movies.presentation.navigation.MovieDetailDestination
import com.example.feature.movies.presentation.navigation.movieGraph
import kotlin.reflect.KClass

@Composable
fun MovieNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: KClass<*>,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        builder = {
            movieGraph(navHostController = navController)
        })

}

suspend fun handleDeepLink(intent: Intent, navController: NavHostController) {
    val data = intent.data
    if (data != null) {
        println("🔗 Deep link received: $data")

        // Add delay to ensure navigation is ready
        kotlinx.coroutines.delay(100)

        when {
            // Handle custom scheme: movieapp://movie/12345
            data.scheme == "movieapp" && data.pathSegments.getOrNull(0) == "movie" -> {
                val movieId = data.pathSegments.getOrNull(1)?.toIntOrNull()
                if (movieId != null) {
                    println("🎬 Opening movie $movieId from custom scheme")
                    navController.navigate(MovieDetailDestination(movieId))
                }
            }

            // Handle HTTPS: https://movieapp.com/movie/12345
            data.scheme == "https" && data.host == "movieapp.com" -> {
                val pathSegments = data.pathSegments
                if (pathSegments.size >= 2 && pathSegments[0] == "movie") {
                    val movieId = pathSegments[1].toIntOrNull()
                    if (movieId != null) {
                        println("🎬 Opening movie $movieId from HTTPS link")
                        navController.navigate(MovieDetailDestination(movieId))
                    }
                }
            }

            // Handle TMDB links: https://www.themoviedb.org/movie/12345
            data.scheme == "https" && data.host == "www.themoviedb.org" -> {
                val pathSegments = data.pathSegments
                println("🎬 TMDB link - pathSegments: $pathSegments")

                if (pathSegments.size >= 2 && pathSegments[0] == "movie") {
                    val movieSegment = pathSegments[1] // This could be "648878" or "648878-eddington"
                    println("🎬 Movie segment: '$movieSegment'")

                    // ✅ Extract ID from segment (everything before first dash, or whole thing if no dash)
                    val movieId = extractMovieIdFromSegment(movieSegment)

                    if (movieId != null) {
                        println("🎬 ✅ Extracted movie ID: $movieId")
                        println("🎬 ✅ Navigating to MovieDetailDestination(id=$movieId)")
                        navController.navigate(MovieDetailDestination(id = movieId))
                    } else {
                        println("❌ Could not extract movie ID from segment: '$movieSegment'")
                    }
                } else {
                    println("❌ TMDB link doesn't match expected pattern")
                    println("❌ Expected: /movie/{id}, Got path: ${data.path}")
                }
            }

            else -> {
                println("🔗 Unhandled deep link: $data")
            }
        }
    }
}
fun extractMovieIdFromSegment(segment: String): Int? {
    return try {
        println("🔍 Extracting ID from segment: '$segment'")

        // Handle different TMDB URL formats:
        // "648878" → 648878
        // "648878-eddington" → 648878
        // "648878-eddington-western-comedy" → 648878

        val idPart = if (segment.contains('-')) {
            val dashIndex = segment.indexOf('-')
            segment.substring(0, dashIndex)
        } else {
            segment
        }

        println("🔍 ID part extracted: '$idPart'")
        val movieId = idPart.toIntOrNull()
        println("🔍 Final movie ID: $movieId")

        movieId
    } catch (e: Exception) {
        println("❌ Error extracting movie ID from '$segment': ${e.message}")
        null
    }
}