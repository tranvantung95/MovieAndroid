package com.example.feature.movies.presentation.fakedomain.di

import com.example.feature.movies.presentation.fakedomain.GetMovieDetailUseCase
import com.example.feature.movies.presentation.fakedomain.GetMovieDetailUseCaseImpl
import com.example.feature.movies.presentation.fakedomain.GetMoviesUseCase
import com.example.feature.movies.presentation.fakedomain.GetMoviesUseCaseImpl
import com.example.feature.movies.presentation.fakedomain.GetTrendingMoviesUseCase
import com.example.feature.movies.presentation.fakedomain.GetTrendingMoviesUseCaseImpl
import com.example.feature.movies.presentation.fakedomain.SearchMoviesUseCase
import com.example.feature.movies.presentation.fakedomain.SearchMoviesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    // Movie Use Cases
    @Binds
    abstract fun bindGetTrendingMoviesUseCase(
        getTrendingMoviesUseCaseImpl: GetTrendingMoviesUseCaseImpl
    ): GetTrendingMoviesUseCase

    @Binds
    abstract fun bindSearchMoviesUseCase(
        searchMoviesUseCaseImpl: SearchMoviesUseCaseImpl
    ): SearchMoviesUseCase

    @Binds
    abstract fun bindGetMoviesUseCase(
        getMoviesUseCaseImpl: GetMoviesUseCaseImpl
    ): GetMoviesUseCase

    @Binds
    abstract fun bindGetMovieDetailUseCase(
        getMovieDetailUseCaseImpl: GetMovieDetailUseCaseImpl
    ): GetMovieDetailUseCase
}
