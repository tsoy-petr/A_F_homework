package com.android.hootor.academy.fundamentals.homework.data

import com.android.hootor.academy.fundamentals.homework.networking.MovieApiService

class MoviesRepository(private val movieApiService: MovieApiService) {

    suspend fun fetchMovies(page: Int) = movieApiService.fetchMovies(page)

    suspend fun fetchGenres() = movieApiService.fetchGenres()

    suspend fun fetchMovie(movieId: Int) = movieApiService.fetchDetailMovie(movieId = movieId)

    suspend fun fetchActors(movieId: Int) = movieApiService.fetchActors(movieId = movieId)
}