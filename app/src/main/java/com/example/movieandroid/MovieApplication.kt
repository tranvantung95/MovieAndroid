package com.example.movieandroid

import android.app.Application
import com.example.movieapp.ShareKoin
import com.example.movieapp.platformModule
import com.example.movieapp.sharedModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@HiltAndroidApp
class MovieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //ShareKoin.initKoin()
        startKoin {
            androidContext(this@MovieApplication)
            modules(sharedModule + platformModule)
        }
    }
}