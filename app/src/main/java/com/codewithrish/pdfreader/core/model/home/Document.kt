package com.codewithrish.pdfreader.core.model.home

import android.os.Parcelable
import com.codewithrish.pdfreader.core.model.room.DocumentEntity
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Document(
    val id: Long,
    val path: String,
    val uri: String,
    val name: String,
    val dateTime: Long,
    val mimeType: String,
    val size: Long,
    val bookmarked: Boolean,
    val numberOfPages: Int = 0
): Parcelable


fun Document.toDocumentEntity(): DocumentEntity {
    return DocumentEntity(
        id = id,
        path = path,
        uri = uri,
        name = name,
        dateTime = dateTime,
        mimeType = mimeType,
        size = size,
        bookmarked = bookmarked
    )
}