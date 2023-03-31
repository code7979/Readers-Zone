package com.code7979.readerszone.data.local

import code7979.LocalBookDetail
import com.code7979.readerszone.data.Resource
import com.code7979.readerszone.presentation.model.BookDetail
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

interface LocalDataSource {
//    suspend fun getAllBook(): Flow<Resource<List<Book>?>>
    suspend fun getBookDetailsById(bookId: String): Flow<Resource<BookDetail?>>
    suspend fun getBookByBookId(book_id: String): LocalBookDetail?
    suspend fun deleteBookByBookId(book_id: String)

    suspend fun insertBookInDb(
        id: Long? = null,
        book_id: String,
        title: String,
        subtitle: String,
        authors: String,
        image: String,
        url: String,
        description: String,
        publisher: String,
        pages: String,
        year: String,
        download: String,
        fileUri: String
    )

    fun getAllBook(context: CoroutineContext): Flow<List<LocalBookDetail>>

    fun getAllBookResource(): Flow<Resource<List<LocalBookDetail>>>
}