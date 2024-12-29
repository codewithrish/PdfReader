package com.codewithrish.pdfreader.core.data.di

import com.codewithrish.pdfreader.core.data.repository.DocumentsRepository
import com.codewithrish.pdfreader.core.data.repository.DocumentsRepositoryImpl
import com.codewithrish.pdfreader.core.data.repository.LoadFilesRepository
import com.codewithrish.pdfreader.core.data.repository.LoadFilesRepositoryImpl
import com.codewithrish.pdfreader.core.data.repository.PdfOperationsRepository
import com.codewithrish.pdfreader.core.data.repository.PdfOperationsRepositoryImpl
import com.codewithrish.pdfreader.core.data.repository.UserDataRepository
import com.codewithrish.pdfreader.core.data.repository.UserDataRepositoryImpl
import com.codewithrish.pdfreader.core.data.util.TimeZoneBroadcastMonitor
import com.codewithrish.pdfreader.core.data.util.TimeZoneMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsUserDataRepository(
        userDataRepository: UserDataRepositoryImpl
    ): UserDataRepository

    @Binds
    internal abstract fun bindsDocumentsRepository(
        documentsRepository: DocumentsRepositoryImpl
    ): DocumentsRepository

    @Binds
    internal abstract fun bindsLoadFilesRepository(
        loadFilesRepository: LoadFilesRepositoryImpl
    ): LoadFilesRepository

    @Binds
    internal abstract fun bindsPdfOperationsRepository(
        pdfOperationsRepository: PdfOperationsRepositoryImpl
    ): PdfOperationsRepository

    @Binds
    internal abstract fun binds(impl: TimeZoneBroadcastMonitor): TimeZoneMonitor
}