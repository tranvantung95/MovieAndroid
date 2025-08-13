package com.example.feature.movies.presentation.navigation

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.feature.movies.presentation.MovieDetailRouter
import com.example.feature.movies.presentation.MovieRouter

fun NavGraphBuilder.movieGraph(navHostController: NavHostController) {
    composable<MovieDestination> {
        MovieRouter(modifier = Modifier.safeDrawingPadding(), onMovieClick = {
            navHostController.navigate(MovieDetailDestination(id = it.id))
        })
    }
    composable<MovieDetailDestination>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "movieapp://movie/{id}"
            },
            navDeepLink {
                uriPattern = "https://movieapp.com/movie/{id}"
            },
            navDeepLink {
                uriPattern = "https://www.themoviedb.org/movie/{id}*"
            }
        )) {
        MovieDetailRouter(modifier = Modifier.safeDrawingPadding(), onBackClick = {
            navHostController.popBackStack()
        }, onPlayTrailerClick = {

        })
    }
}