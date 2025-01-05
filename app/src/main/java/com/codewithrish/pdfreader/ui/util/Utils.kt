package com.codewithrish.pdfreader.ui.util

import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.core.model.room.DocumentEntity
import com.codewithrish.pdfreader.core.model.room.toDocument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

fun Flow<DocumentEntity?>.toDocumentFlow(): Flow<Document> {
    return this
        .filterNotNull() // Filter out null values
        .map { entity ->
            entity.toDocument() // Map each non-null DocumentEntity to Document
        }
}

fun Flow<List<DocumentEntity?>?>.toDocumentsFlow(): Flow<List<Document>> {
    return this
        .filterNotNull() // Filter out null lists
        .map { entityList ->
            entityList
                .filterNotNull() // Filter out null entities in the list
                .map { entity ->
                    entity.toDocument() // Convert DocumentEntity to Document
                }
        }
}
