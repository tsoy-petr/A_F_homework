package com.android.hootor.academy.fundamentals.homework.domain

import com.android.hootor.academy.fundamentals.homework.data.MoviesRepository
import com.android.hootor.academy.fundamentals.homework.domain.interactor.UseCase
import com.android.hootor.academy.fundamentals.homework.domain.models.Actor
import com.android.hootor.academy.fundamentals.homework.domain.models.Movie
import kotlinx.coroutines.CoroutineDispatcher

class MovieDetailUseCase(
    private val moviesRepository: MoviesRepository,
    coroutineDispatcher: CoroutineDispatcher
) : UseCase<MovieDetailUseCase.Params, Movie?>(coroutineDispatcher) {

    override suspend fun execute(parameters: Params): Movie? =
        fetchMovie(parameters.movieId)?.also {
            it.actors.addAll(fetchActors(it.id))
        }

    private suspend fun fetchMovie(movieId: Int): Movie? {
        return moviesRepository.fetchMovie(movieId).body()
    }

    private suspend fun fetchActors(movieId: Int): List<Actor> {
        val response = moviesRepository.fetchActors(movieId)
        return when (response.isSuccessful && response.body() != null) {
            true -> response.body()!!.actors
            false -> listOf()
        }
    }

    data class Params(val movieId: Int)
}