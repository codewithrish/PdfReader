package com.codewithrish.pdfreader.ui.screen.home

import android.webkit.MimeTypeMap

enum class DocumentType(val mimeTypes: List<String?>) {
    PDF(
        listOf(
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf"),
        ),
    ),
    WORD(
        listOf(
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc"),
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx"),
        ),
    ),
    PPT(
        listOf(
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppt"),
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("pptx"),
        ),
    ),
    EXCEL(
        listOf(
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls"),
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx"),
        ),
    ),
    CSV(
        listOf(
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("text/csv"),
        ),
    ),
    TXT(
        listOf(
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt"),
        )
    ),
    IMAGE(
        listOf(
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("png"),
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg"),
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg"),
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("webp"),
        )
    ),
    EBOOK(
        listOf(
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("epub"),
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("mobi"),
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("azw"),
        )
    ),
    NONE(
        listOf()
    ),
}