package com.example.movieandroid

import android.app.Application
import com.example.feature.movies.presentation.di.movieUiModule
import com.example.movieapp.platformModule
import com.example.movieapp.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MovieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MovieApplication)
            modules(sharedModule + platformModule + movieUiModule)
        }
    }
}