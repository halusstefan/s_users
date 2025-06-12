package com.slide.test.repository.model

import com.slide.test.network.model.PostDto

/**
 * Created by Stefan Halus on 11 Jun 2025
 */
data class PostModel(
    val id: Long,
    val userId: Long,
    val title: String,
    val body: String,
)

fun PostDto.toModel(): PostModel {
    return PostModel(
        id = id,
        userId = userId,
        title = title,
        body = body
    )
}