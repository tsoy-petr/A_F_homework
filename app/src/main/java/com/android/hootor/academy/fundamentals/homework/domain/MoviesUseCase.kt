package com.android.hootor.academy.fundamentals.homework.domain

import com.android.hootor.academy.fundamentals.homework.data.MoviesRepository
import com.android.hootor.academy.fundamentals.homework.domain.interactor.UseCase
import com.android.hootor.academy.fundamentals.homework.domain.models.Genre
import com.android.hootor.academy.fundamentals.homework.domain.models.Movie
import kotlinx.coroutines.CoroutineDispatcher

class MoviesUseCase(
    private val moviesRepository: MoviesRepository,
    coroutineDispatcher: CoroutineDispatcher
) : UseCase<MoviesUseCase.Params, List<Movie>>(coroutineDispatcher) {
    override suspend fun execute(parameters: Params): List<Movie> {

        val genres = fetchGenres()
        val movies = fetchMovies(parameters)

        movies.forEach { movie ->
            movie.genreIds?.forEach { genreId ->
                genres.find { genre ->
                    genreId == genre.id
                }?.also {
                    movie.genres.add(it)
                }
            }
        }
        return movies
    }

    private suspend fun fetchMovies(parameters: Params): List<Movie> {
        val response = moviesRepository.fetchMovies(parameters.page)
        return when (response.isSuccessful && response.body() != null) {
            true -> response.body()?.results ?: listOf()
            false -> listOf()
        }
    }

    private suspend fun fetchGenres(): List<Genre> {
        val response = moviesRepository.fetchGenres()
        return when (response.isSuccessful && response.body() != null) {
            true -> response.body()?.genres ?: listOf()
            false -> listOf()
        }
    }

    data class Params(val page: Int)
}