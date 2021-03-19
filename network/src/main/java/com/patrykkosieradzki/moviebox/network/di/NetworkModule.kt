package com.patrykkosieradzki.moviebox.network.di

import com.patrykkosieradzki.moviebox.domain.AppConfiguration
import com.patrykkosieradzki.moviebox.domain.repositories.MoviesRepository
import com.patrykkosieradzki.moviebox.network.repositories.MoviesApiRepository
import com.patrykkosieradzki.moviebox.network.services.MoviesService
import com.patrykkosieradzki.moviebox.network.utils.ApiKeyInterceptor
import com.patrykkosieradzki.moviebox.network.utils.CustomOkHttpClientFactory
import com.patrykkosieradzki.moviebox.network.utils.ErrorHandlingCallAdapterFactory
import com.patrykkosieradzki.moviebox.network.utils.NetworkHandler
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {

    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single {
        CustomOkHttpClientFactory(
            appConfiguration = get()
        ).createOkHttpClient()
    } bind OkHttpClient::class

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(get<AppConfiguration>().baseApiUrl)
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .addCallAdapterFactory(ErrorHandlingCallAdapterFactory())
            .client(get())
            .build()
    }

    single {
        NetworkHandler(
            appConfiguration = get()
        )
    }

    single<MoviesService> {
        get<Retrofit>().create(MoviesService::class.java)
    }

    single<MoviesRepository> {
        MoviesApiRepository(
            moviesService = get(),
            networkHandler = get()
        )
    }
}
