package com.codewithrish.pdfreader.core.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

interface PdfOperationRepository {

}

internal class PdfOperationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : PdfOperationRepository {

}