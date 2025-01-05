package com.codewithrish.pdfreader.ui.screen.bookmark

import com.codewithrish.pdfreader.core.model.home.Document

sealed class BookmarksUiEvent {
    data object LoadBookMarks: BookmarksUiEvent()
    data class OnBookmarkClick(val id: Long, val isBookmarked: Boolean): BookmarksUiEvent()
    data class OnThreeDotClick(val id: Long): BookmarksUiEvent()
    data class DeleteDocument(val document: Document): BookmarksUiEvent()
}