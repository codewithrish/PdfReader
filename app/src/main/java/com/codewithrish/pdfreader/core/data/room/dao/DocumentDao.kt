package com.codewithrish.pdfreader.core.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codewithrish.pdfreader.core.model.room.DocumentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents")
    fun getAllDocuments(): Flow<List<DocumentEntity?>?>
    @Query("SELECT * FROM documents WHERE mimeType = :mimeType")
    fun getDocumentsByMimeType(mimeType: String): Flow<List<DocumentEntity?>?>
    @Query("SELECT * FROM documents WHERE name LIKE :searchQuery AND mimeType LIKE :mimeType")
    fun searchDocuments(searchQuery: String, mimeType: String): Flow<List<DocumentEntity?>?>
    @Query("SELECT * FROM documents WHERE name LIKE :searchQuery")
    fun searchByName(searchQuery: String): Flow<List<DocumentEntity?>?>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDocument(user: DocumentEntity)
    @Query("SELECT * FROM documents WHERE id = :documentId")
    fun getDocumentById(documentId: Long): DocumentEntity?
    @Query("SELECT * FROM documents WHERE id = :documentId")
    fun getDocumentByIdAsFlow(documentId: Long): Flow<DocumentEntity?>
    @Query("SELECT * FROM documents WHERE id IN (:documentIds)")
    fun getDocumentsByIds(documentIds: List<Long>): Flow<List<DocumentEntity?>?>
    @Query("UPDATE documents SET bookmarked = :bookmarked WHERE id = :id")
    suspend fun updateBookmark(id: Long, bookmarked: Boolean)
    // Delete Document
    @Query("DELETE FROM documents WHERE id = :id")
    suspend fun delete(id: Long)
    @Query("DELETE FROM documents WHERE uri = :uri")
    suspend fun deleteByUri(uri: String)
    // Bookmark
    @Query("SELECT * FROM documents WHERE bookmarked = 1")
    fun getBookmarkedDocuments(): Flow<List<DocumentEntity?>?>
    @Query("UPDATE documents SET bookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateBookmarkStatus(id: Long, isBookmarked: Boolean)
}