package com.codewithrish.pdfreader.core.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codewithrish.pdfreader.core.model.home.Document
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey
    val id: Long?,
    val uri: String?,
    val path: String?,
    val name: String?,
    val dateTime: Long?,
    val mimeType: String?,
    val size: Long?,
    val bookmarked: Boolean?,
    val isLocked: Boolean?,
)


fun DocumentEntity.toDocument(): Document {
    return Document(
        id = id ?: -1,
        path = path ?: "",
        uri = uri ?: "",
        name = name ?: "",
        dateTime = dateTime ?: 0,
        mimeType = mimeType ?: "",
        size = size ?: 0,
        bookmarked = bookmarked == true,
        isLocked = isLocked == true
    )
}