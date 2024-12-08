package com.codewithrish.pdfreader.core.model.home

import android.os.Parcelable
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
): Parcelable
