package com.brijwel.zohotask.domain.model

data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    val isFavourite: Boolean
)