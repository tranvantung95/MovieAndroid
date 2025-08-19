package com.example.feature.movies.presentation.di

import com.example.feature.movies.presentation.MovieDetailViewModel
import com.example.feature.movies.presentation.MovieListViewModel
import com.example.feature.movies.presentation.mapper.MovieDetailMapper
import com.example.feature.movies.presentation.mapper.MovieUiMapper
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val mapperModule = module {
    factory {
        MovieDetailMapper()
    }
    factory {
        MovieUiMapper()
    }
}
internal val viewModelModule = module {
    viewModel {
        MovieListViewModel(get(), get(), get())
    }
    viewModel {
        MovieDetailViewModel(get(), get(), get())
    }
}
val movieUiModule = module {
    includes(mapperModule, viewModelModule)
}