package com.example.feature.movies.presentation.fakedomain.repo

import com.example.feature.movies.presentation.model.Movie
import com.example.feature.movies.presentation.model.MovieDetail

interface MovieRepository {
    suspend fun getTrendingMovies(): List<Movie>
    suspend fun searchMovies(query: String): List<Movie>
    suspend fun getMovieDetail(movieId: Int): MovieDetail
}