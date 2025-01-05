package com.codewithrish.pdfreader.core.data.repository

import com.codewithrish.pdfreader.core.data.room.dao.DocumentDao
import com.codewithrish.pdfreader.core.model.room.DocumentEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DocumentsRepository {
    fun getAllDocuments(): Flow<List<DocumentEntity?>?>
    fun getDocumentsByMimeType(mimeType: String): Flow<List<DocumentEntity?>?>
    suspend fun getDocumentById(documentId: Long): DocumentEntity?
    fun getDocumentByIdAsFlow(documentId: Long): Flow<DocumentEntity?>
    fun getDocumentsByIds(documentIds: List<Long>): Flow<List<DocumentEntity?>?>
    fun searchDocuments(searchQuery: String, mimeType: String): Flow<List<DocumentEntity?>?>
    fun searchByName(searchQuery: String): Flow<List<DocumentEntity?>?>
    suspend fun insertDocument(user: DocumentEntity)
    suspend fun updateBookmark(id: Long, bookmarked: Boolean)
    // Delete Document
    suspend fun delete(id: Long)
    suspend fun deleteByUri(uri: String)
    // Bookmark
    fun getBookmarkedDocuments(): Flow<List<DocumentEntity?>?>
    suspend fun updateBookmarkStatus(id: Long, isBookmarked: Boolean)
}

internal class DocumentsRepositoryImpl @Inject constructor(
    private val documentDao: DocumentDao
) : DocumentsRepository {
    override fun getAllDocuments(): Flow<List<DocumentEntity?>?> {
        return documentDao.getAllDocuments()
    }

    override fun getDocumentsByMimeType(mimeType: String): Flow<List<DocumentEntity?>?> {
        return documentDao.getDocumentsByMimeType(mimeType)
    }

    override suspend fun insertDocument(user: DocumentEntity) {
        return documentDao.insertDocument(user)
    }

    override suspend fun getDocumentById(documentId: Long): DocumentEntity? {
        return withContext(Dispatchers.IO) {
            documentDao.getDocumentById(documentId)
        }
    }

    override fun getDocumentByIdAsFlow(documentId: Long): Flow<DocumentEntity?> {
        return documentDao.getDocumentByIdAsFlow(documentId)
    }

    override fun getDocumentsByIds(documentIds: List<Long>): Flow<List<DocumentEntity?>?> {
        return documentDao.getDocumentsByIds(documentIds)
    }

    override fun searchDocuments(
        searchQuery: String,
        mimeType: String
    ): Flow<List<DocumentEntity?>?> {
        return documentDao.searchDocuments(searchQuery, mimeType)
    }

    override fun searchByName(searchQuery: String): Flow<List<DocumentEntity?>?> {
        return documentDao.searchByName(searchQuery)
    }

    override suspend fun updateBookmark(id: Long, bookmarked: Boolean) {
        return documentDao.updateBookmark(id, bookmarked)
    }

    override suspend fun delete(id: Long) {
        return documentDao.delete(id)
    }

    override suspend fun deleteByUri(uri: String) {
        return documentDao.deleteByUri(uri)
    }

    // Bookmark
    override fun getBookmarkedDocuments(): Flow<List<DocumentEntity?>?> {
        return documentDao.getBookmarkedDocuments()
    }

    override suspend fun updateBookmarkStatus(id: Long, isBookmarked: Boolean) {
        return documentDao.updateBookmarkStatus(id, isBookmarked)
    }

}