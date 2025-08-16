package com.example.feature.movies.presentation.fakedomain

import com.example.feature.movies.presentation.fakedomain.repo.MovieRepository
import com.example.feature.movies.presentation.model.MovieUi
import javax.inject.Inject

interface GetTrendingMoviesUseCase {
    suspend operator fun invoke(): Result<List<MovieUi>>
}

class GetTrendingMoviesUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetTrendingMoviesUseCase {

    override suspend fun invoke(): Result<List<MovieUi>> {
        return try {
            val movies = movieRepository.getTrendingMovies()
            Result.success(movies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}