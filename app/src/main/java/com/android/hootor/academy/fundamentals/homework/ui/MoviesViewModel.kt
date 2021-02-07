package com.android.hootor.academy.fundamentals.homework.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.hootor.academy.fundamentals.homework.data.MoviesRepository
import com.android.hootor.academy.fundamentals.homework.domain.MoviesUseCase
import com.android.hootor.academy.fundamentals.homework.domain.Result
import com.android.hootor.academy.fundamentals.homework.domain.models.Movie
import com.android.hootor.academy.fundamentals.homework.networking.MovieApiService
import com.android.hootor.academy.fundamentals.homework.networking.MovieClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class MoviesViewModel(
    private val moviesUseCase: MoviesUseCase = MoviesUseCase(
        MoviesRepository(MovieClient.getClient().create(MovieApiService::class.java)),
        Dispatchers.IO
    )
) : ViewModel() {

    private var iter = 1
    private var getDataJob: Job? = null

    private val _uiSate = MutableStateFlow(
        MoviesViewState(FetchStatus.Loading, mutableListOf())
    )
    val uiState = _uiSate.asStateFlow()

    init {
        loadPagePopular(1)
    }

    fun loadPagePopular(page: Int?) {

        if (getDataJob?.isActive == true) return

        getDataJob = viewModelScope.launch {
            _uiSate.value = _uiSate.value.copy(fetchStatus = FetchStatus.Loading)
            val newPage = page ?: 1
            val result = moviesUseCase(MoviesUseCase.Params(newPage))
            when (result) {
                is Result.Success -> {
                    val n = _uiSate.value.data + result.data
                    n.onEach { movie ->
                        movie.minimumAge = iter++
                    }
                    _uiSate.value = _uiSate.value.copy(fetchStatus = FetchStatus.AddMore,
                    data = n)
                }
                is Result.Error -> {
                    _uiSate.value = _uiSate.value.copy(
                        fetchStatus = FetchStatus.ShowError(
                            result.exception.message.toString()
                        )
                    )
                }
            }
        }
    }
}

data class MoviesViewState(
    val fetchStatus: FetchStatus,
    val data: List<Movie>
)

sealed class FetchStatus {
    object Success : FetchStatus()
    object AddMore : FetchStatus()
    data class ShowError(val message: String) : FetchStatus()
    object Loading : FetchStatus()
}