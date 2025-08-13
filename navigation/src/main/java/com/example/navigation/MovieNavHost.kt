package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
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