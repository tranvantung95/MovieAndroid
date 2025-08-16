package com.example.feature.movies.presentation.fakedomain.repo

import com.example.feature.movies.presentation.model.MovieUi
import com.example.feature.movies.presentation.model.MovieDetail

interface MovieRepository {
    suspend fun getTrendingMovies(): List<MovieUi>
    suspend fun searchMovies(query: String): List<MovieUi>
    suspend fun getMovieDetail(movieId: Int): MovieDetail
}