package com.patrykkosieradzki.moviebox.network.services

import com.patrykkosieradzki.moviebox.network.model.MovieDetailsResponse
import com.patrykkosieradzki.moviebox.network.model.MoviesPageResponse
import com.patrykkosieradzki.moviebox.network.utils.ApiResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: String = "undefined"
    ): ApiResult<MoviesPageResponse>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1
    ): ApiResult<MoviesPageResponse>

    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: Int
    ): ApiResult<MovieDetailsResponse>
}
