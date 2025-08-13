package com.example.feature.movies.presentation

import com.example.feature.movies.presentation.model.Movie
import com.example.feature.movies.presentation.model.MovieDetail

data class MovieDetailUiState(
    val movieDetail: MovieDetail? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isFavorite: Boolean = false,
    val isWatchlisted: Boolean = false,
    val similarMovies: List<Movie> = emptyList(),
    val isLoadingSimilar: Boolean = false
)