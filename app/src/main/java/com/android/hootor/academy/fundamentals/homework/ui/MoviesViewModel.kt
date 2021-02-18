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

    private var isScrolling = false
    private var getDataJob: Job? = null
    private var breakingNewPage = 1
    private val _uiSate = MutableStateFlow(
        MoviesViewState(FetchStatus.Loading, mutableListOf())
    )
    val uiState = _uiSate.asStateFlow()

    init {
        loadPagePopular()
    }

    private fun isLoading() = getDataJob?.isActive ?: false

    private fun loadPagePopular() {

        if (getDataJob?.isActive == true) return

        getDataJob = viewModelScope.launch {
            _uiSate.value = _uiSate.value.copy(fetchStatus = FetchStatus.Loading)
            val result = moviesUseCase(MoviesUseCase.Params(breakingNewPage))
            when (result) {
                is Result.Success -> {
                    breakingNewPage++
                    val newData = _uiSate.value.data + result.data
                    _uiSate.value = _uiSate.value.copy(
                        fetchStatus = FetchStatus.AddMore,
                        data = newData
                    )
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

    fun onLoadMoreTriggered(
        firstVisibleItemPosition: Int,
        visibleItemCount: Int,
        totalItemCount: Int
    ) {

        val isNotLoading = !isLoading()
        val isAtLastPage = firstVisibleItemPosition + visibleItemCount >= totalItemCount
        val isNotAtBeginning = firstVisibleItemPosition >= 0
        val isTotalMoreThanVisible = totalItemCount >= MovieClient.QUERY_PAGE_SIZE

        val shouldPaginate = isNotLoading && isAtLastPage
                && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

        if (shouldPaginate) {
            loadPagePopular()
            isScrolling = false
        }
    }

    fun isScrolling() {
        isScrolling = true
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