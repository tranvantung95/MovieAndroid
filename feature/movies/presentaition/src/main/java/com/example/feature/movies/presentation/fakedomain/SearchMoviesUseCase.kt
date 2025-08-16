package com.example.feature.movies.presentation.fakedomain

import com.example.feature.movies.presentation.fakedomain.repo.MovieRepository
import com.example.feature.movies.presentation.model.MovieUi
import javax.inject.Inject

interface SearchMoviesUseCase {
    suspend operator fun invoke(query: String): Result<List<MovieUi>>
}

class SearchMoviesUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : SearchMoviesUseCase {

    override suspend fun invoke(query: String): Result<List<MovieUi>> {
        return try {
            if (query.isBlank()) {
                Result.success(emptyList())
            } else {
                val movies = movieRepository.searchMovies(query)
                Result.success(movies)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}