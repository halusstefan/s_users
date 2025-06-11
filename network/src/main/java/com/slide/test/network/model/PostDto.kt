package com.slide.test.network.model

import com.squareup.moshi.Json

/**
 * Created by Stefan Halus on 11 Jun 2025
 */
data class PostDto(
    val id: Int,
    @Json(name = "user_id")
    val userId: Int,
    val title: String,
    val body: String
)