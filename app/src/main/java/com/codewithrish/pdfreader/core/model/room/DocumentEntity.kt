package com.codewithrish.pdfreader.core.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey
    val id: Long,
    val path: String,
    val uri: String,
    val name: String,
    val dateTime: Long,
    val mimeType: String,
    val size: Long,
    val bookmarked: Boolean,
)
