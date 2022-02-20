package com.brijwel.zohotask.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.brijwel.zohotask.domain.model.Post

@Entity(tableName = "post_table")
data class PostEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "userId") val userId: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "body") val body: String?,
    @ColumnInfo(name = "isFavourite") val isFavourite: Boolean = false,
) {
    fun mapToPost() = Post(
        id = this.id,
        userId = this.userId,
        title = this.title ?: "",
        body = this.body ?: "",
        isFavourite = this.isFavourite
    )
}