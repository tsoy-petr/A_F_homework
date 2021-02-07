package com.android.hootor.academy.fundamentals.homework.networking

import com.android.hootor.academy.fundamentals.homework.domain.models.Movie
import kotlinx.serialization.Serializable

@Serializable
data class MovieApiResponse(val page: Int, val results: List<Movie>)
