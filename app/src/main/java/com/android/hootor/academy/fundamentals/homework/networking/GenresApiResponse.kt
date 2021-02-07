package com.android.hootor.academy.fundamentals.homework.networking

import com.android.hootor.academy.fundamentals.homework.domain.models.Genre
import kotlinx.serialization.Serializable

@Serializable
data class GenresApiResponse(val genres: List<Genre>)
