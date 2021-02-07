package com.android.hootor.academy.fundamentals.homework.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.hootor.academy.fundamentals.homework.data.MoviesRepository
import com.android.hootor.academy.fundamentals.homework.domain.MovieDetailUseCase
import com.android.hootor.academy.fundamentals.homework.domain.Result
import com.android.hootor.academy.fundamentals.homework.domain.models.Movie
import com.android.hootor.academy.fundamentals.homework.networking.MovieApiService
import com.android.hootor.academy.fundamentals.homework.networking.MovieClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieDetailUseCase: MovieDetailUseCase = MovieDetailUseCase(
        MoviesRepository(MovieClient.getClient().create(MovieApiService::class.java)),
        Dispatchers.IO
    )
) : ViewModel() {

    private val _flow = MutableSharedFlow<Movie?>(replay = 1, extraBufferCapacity = 1)
    val flow = _flow.asSharedFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    fun fetchMovie(movieId: Int) {
        viewModelScope.launch {
            val result = movieDetailUseCase(MovieDetailUseCase.Params(movieId = movieId))
            when (result) {
                is Result.Success -> {
                    _flow.emit(result.data)
                }
                is Result.Error -> {
                    Log.e("happy", result.exception.message.toString())
                    _error.emit(result.exception.message.toString())
                }
            }
        }
    }
}