package com.brijwel.zohotask.data.network

import com.brijwel.zohotask.data.local.PostEntity
import com.squareup.moshi.Json

data class PostDto(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "userId") val userId: Int,
    @field:Json(name = "title") val title: String?,
    @field:Json(name = "body") val body: String?
) {
    fun mapToPostEntity() = PostEntity(
        id = this.id,
        userId = this.userId,
        title = this.title,
        body = this.body,
    )
}