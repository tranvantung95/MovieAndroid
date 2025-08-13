package com.example.feature.movies.presentation.navigation

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.feature.movies.presentation.MovieDetailRouter
import com.example.feature.movies.presentation.MovieRouter

fun NavGraphBuilder.movieGraph(navHostController: NavHostController) {
    composable<MovieDestination> {
        MovieRouter(modifier = Modifier.safeDrawingPadding(), onMovieClick = {
            navHostController.navigate(MovieDetailDestination(id = it.id))
        })
    }
    composable<MovieDetailDestination> {
        MovieDetailRouter(modifier = Modifier.safeDrawingPadding(), onBackClick = {
            navHostController.popBackStack()
        }, onPlayTrailerClick = {

        })
    }
}