package com.example.feature.movies.presentation.fakedomain.di

import com.example.feature.movies.presentation.fakedomain.repo.FakeMovieRepositoryImpl
import com.example.feature.movies.presentation.fakedomain.repo.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        fakeMovieRepositoryImpl: FakeMovieRepositoryImpl
    ): MovieRepository

}