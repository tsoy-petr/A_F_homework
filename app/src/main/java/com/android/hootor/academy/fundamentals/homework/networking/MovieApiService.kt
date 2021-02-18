package com.android.hootor.academy.fundamentals.homework.networking

import com.android.hootor.academy.fundamentals.homework.domain.models.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/popular")
    suspend fun fetchMovies(
        @Query("page") page: Int,
    ): Response<MovieApiResponse>

    @GET("genre/movie/list")
    suspend fun fetchGenres(
    ): Response<GenresApiResponse>

    @GET("movie/{movie_id}")
    suspend fun fetchDetailMovie(
        @Path("movie_id") movieId: Int,
    ): Response<Movie>

    @GET("movie/{movie_id}/casts")
    suspend fun fetchActors(
        @Path("movie_id") movieId: Int,
    ): Response<ActorsApiResponse>
}
