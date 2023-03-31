package com.code7979.readerszone.util

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.os.ParcelFileDescriptor
import androidx.core.net.toUri
import code7979.LocalBookDetail
import com.code7979.readerszone.data.remote.domain.model.RemoteBookDetail
import com.code7979.readerszone.presentation.model.BookDetail
import com.code7979.readerszone.presentation.model.BookState
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Request
import java.io.File

fun getTrueFile(context: Context, fileName: String): File {
    val parent = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    return File(parent, fileName)
}

fun LocalBookDetail.convertToJson(): String? {
    val mapper = jacksonObjectMapper()
    return mapper.writeValueAsString(this)
}

fun String.toRemoteBookDetail(): LocalBookDetail? {
    val mapper = jacksonObjectMapper()
    return mapper.readValue(this, LocalBookDetail::class.java)
}

fun BookDetail.toLocalBookDetail(): LocalBookDetail {
    val state = this.bookState
    if (state is BookState.Download) {
        val download = state.data ?: ""
        return LocalBookDetail(
            id = 0L,
            book_id = this.id,
            title = this.title,
            subtitle = this.subtitle,
            authors = this.authors,
            image = this.image,
            url = this.url,
            description = this.description,
            publisher = this.publisher,
            pages = this.page,
            year = this.year,
            download = download,
            fileUri = ""
        )
    } else {
        val fileUri = state.data ?: ""
        return LocalBookDetail(
            id = 0L,
            book_id = this.id,
            title = this.title,
            subtitle = this.subtitle,
            authors = this.authors,
            image = this.image,
            url = this.url,
            description = this.description,
            publisher = this.publisher,
            pages = this.page,
            year = this.year,
            download = "",
            fileUri = fileUri
        )

    }
}

fun LocalBookDetail.toBookDetail(): BookDetail {
    return BookDetail(
        status = "success",
        id = this.book_id,
        title = this.title,
        subtitle = this.subtitle,
        authors = this.authors,
        image = this.image,
        url = this.url,
        description = this.description,
        publisher = this.publisher,
        page = this.pages,
        year = this.year,
        bookState = BookState.Read(this.fileUri)
//        download = this.download,
//        fileUri = null
    )
}

fun RemoteBookDetail.toBookDetail(): BookDetail {
    return BookDetail(
        status = "success",
        id = this.id,
        title = this.title,
        subtitle = this.subtitle,
        authors = this.authors,
        image = this.image,
        url = this.url,
        description = this.description,
        publisher = this.publisher,
        page = this.page,
        year = this.year,
        bookState = BookState.Download(this.download)
//        download = this.download,
//        fileUri = null
    )
}

fun String.replaceFS(): String {
    return this.replace("/", "-")
}

fun String.toReadUri(): Uri {
    return this.replace("-", "/").toUri()
}