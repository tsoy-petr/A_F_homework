package com.android.hootor.academy.fundamentals.homework.networking

import com.android.hootor.academy.fundamentals.homework.domain.models.Actor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorsApiResponse(@SerialName("cast") val actors: List<Actor>)
