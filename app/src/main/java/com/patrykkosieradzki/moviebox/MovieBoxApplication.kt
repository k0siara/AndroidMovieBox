package com.patrykkosieradzki.moviebox

import android.app.Application
import com.google.firebase.FirebaseApp
import com.patrykkosieradzki.moviebox.di.appModule
import com.patrykkosieradzki.moviebox.network.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class MovieBoxApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@MovieBoxApplication)
            modules(networkModule, appModule)
        }
    }
}
