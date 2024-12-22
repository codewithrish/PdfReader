package com.codewithrish.pdfreader.core.data.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codewithrish.pdfreader.core.data.room.dao.DocumentDao
import com.codewithrish.pdfreader.core.model.room.DocumentEntity

@Database(entities = [DocumentEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun documentDao(): DocumentDao
}