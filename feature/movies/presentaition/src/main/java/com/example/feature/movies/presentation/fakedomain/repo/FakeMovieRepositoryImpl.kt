package com.example.feature.movies.presentation.fakedomain.repo

import com.example.feature.movies.presentation.getSampleMovieDetail
import com.example.feature.movies.presentation.getSampleMovies
import com.example.feature.movies.presentation.model.Movie
import com.example.feature.movies.presentation.model.MovieDetail
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeMovieRepositoryImpl @Inject constructor() : MovieRepository {
    override suspend fun getTrendingMovies(): List<Movie> {
        delay(1000)
        return getSampleMovies()
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        delay(800)
        val allMovies = getSampleMovies()
        return allMovies.filter { movie ->
            movie.title.contains(query, ignoreCase = true) ||
                    movie.overview.contains(query, ignoreCase = true) ||
                    movie.originalTitle.contains(query, ignoreCase = true)
        }
    }

    override suspend fun getMovieDetail(movieId: Int): MovieDetail {
        // Simulate network delay
        delay(600)
        // In real implementation, you would call API with movieId
        // For demo, return sample data regardless of ID
        return getSampleMovieDetail()
    }

}
