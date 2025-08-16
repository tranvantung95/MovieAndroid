package com.example.feature.movies.presentation.model

import com.example.feature.movies.presentation.core.UiModel

data class MovieUi(
    val id: Int,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double,
    val adult: Boolean,
    val originalLanguage: String,
    val genreIds: List<Int>,
    val video: Boolean
) : UiModel{

    fun getPosterUrl(size: String = "w500"): String? {
        return posterPath?.let { "https://image.tmdb.org/t/p/$size$it" }
    }


    fun getBackdropUrl(size: String = "w780"): String? {
        return backdropPath?.let { "https://image.tmdb.org/t/p/$size$it" }
    }

    fun getReleaseYear(): String {
        return if (releaseDate.isNotEmpty()) {
            releaseDate.substring(0, 4)
        } else {
            "Unknown"
        }
    }

    fun getFormattedRating(): String {
        return String.format("%.1f", voteAverage)
    }
}