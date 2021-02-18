package com.android.hootor.academy.fundamentals.homework.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(

    val id: Int,

    @SerialName("original_title")
    val title: String,

    val overview: String,

    @SerialName("poster_path")
    var poster: String = "",

    @SerialName("backdrop_path")
    var backdrop: String? = null,

    @SerialName("vote_average")
    val ratings: Float,
    @SerialName("vote_count")
    val numberOfRatings: Int,

    @SerialName("genre_ids")
    var genreIds: List<Int> = listOf(),

    var minimumAge: Int = 0,
    var runtime: Int = 0,
    var genres: MutableList<Genre> = mutableListOf(),
    var actors: MutableList<Actor> = mutableListOf()
)