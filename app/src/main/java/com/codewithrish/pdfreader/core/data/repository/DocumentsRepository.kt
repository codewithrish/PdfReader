package com.codewithrish.pdfreader.core.data.repository

import com.codewithrish.pdfreader.core.data.room.dao.DocumentDao
import com.codewithrish.pdfreader.core.model.room.DocumentEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DocumentsRepository {
    fun getAllDocuments(): Flow<List<DocumentEntity>>
    fun getBookmarkedDocuments(): Flow<List<DocumentEntity>>
    fun getDocumentsByMimeType(mimeType: String): Flow<List<DocumentEntity>>
    fun loadById(id: Long): Flow<List<DocumentEntity>>
    fun searchDocuments(searchQuery: String, mimeType: String): Flow<List<DocumentEntity>>
    fun searchByName(searchQuery: String): Flow<List<DocumentEntity>>
    suspend fun insertDocument(user: DocumentEntity)
    suspend fun updateBookmark(id: Long, bookmarked: Boolean)
    suspend fun delete(id: Long)
}

internal class DocumentsRepositoryImpl @Inject constructor(
    private val documentDao: DocumentDao
) : DocumentsRepository {
    override fun getAllDocuments(): Flow<List<DocumentEntity>> {
        return documentDao.getAllDocuments()
    }

    override fun getBookmarkedDocuments(): Flow<List<DocumentEntity>> {
        return documentDao.getBookmarkedDocuments()
    }

    override fun getDocumentsByMimeType(mimeType: String): Flow<List<DocumentEntity>> {
        return documentDao.getDocumentsByMimeType(mimeType)
    }

    override suspend fun insertDocument(user: DocumentEntity) {
        return documentDao.insertDocument(user)
    }

    override fun loadById(id: Long): Flow<List<DocumentEntity>> {
        return documentDao.loadById(id)
    }

    override fun searchDocuments(
        searchQuery: String,
        mimeType: String
    ): Flow<List<DocumentEntity>> {
        return documentDao.searchDocuments(searchQuery, mimeType)
    }

    override fun searchByName(searchQuery: String): Flow<List<DocumentEntity>> {
        return documentDao.searchByName(searchQuery)
    }

    override suspend fun updateBookmark(id: Long, bookmarked: Boolean) {
        return documentDao.updateBookmark(id, bookmarked)
    }

    override suspend fun delete(id: Long) {
        return documentDao.delete(id)
    }

}