package com.example.feature.movies.presentation

import com.example.feature.movies.presentation.model.MovieUi

data class MovieListUiState(
    val movieUis: List<MovieUi> = emptyList(),
    val filteredMovieUis: List<MovieUi> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false
)