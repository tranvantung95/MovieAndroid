package com.example.feature.movies.presentation.fakedomain

import com.example.feature.movies.presentation.fakedomain.repo.MovieRepository
import com.example.feature.movies.presentation.model.MovieDetail
import javax.inject.Inject

interface GetMovieDetailUseCase {
    suspend operator fun invoke(movieId: Int): Result<MovieDetail>
}

class GetMovieDetailUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetMovieDetailUseCase {

    override suspend fun invoke(movieId: Int): Result<MovieDetail> {
        return try {
            val movieDetail = movieRepository.getMovieDetail(movieId)
            Result.success(movieDetail)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
