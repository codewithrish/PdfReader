package com.codewithrish.pdfreader.core.data.room.di

import android.content.Context
import androidx.room.Room
import com.codewithrish.pdfreader.core.data.room.dao.DocumentDao
import com.codewithrish.pdfreader.core.data.room.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "user_database"
        ).build()
    }
    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase): DocumentDao {
        return db.documentDao()
    }
}