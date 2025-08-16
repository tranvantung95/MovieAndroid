package com.example.feature.movies.presentation

import com.example.feature.movies.presentation.model.MovieUi
import com.example.feature.movies.presentation.model.MovieDetailUiModel

data class MovieDetailUiState(
    val movieDetail: MovieDetailUiModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isFavorite: Boolean = false,
    val isWatchlisted: Boolean = false,
    val similarMovieUis: List<MovieUi> = emptyList(),
    val isLoadingSimilar: Boolean = false
)