package com.code7979.readerszone.data.remote

import com.code7979.readerszone.data.Resource
import com.code7979.readerszone.data.remote.domain.model.Book
import com.code7979.readerszone.presentation.model.BookDetail
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun getAllBook(): Flow<Resource<List<Book>?>>
    suspend fun getBookDetailsById(bookId: String): Flow<Resource<BookDetail?>>
    suspend fun searchBook(query:String):Flow<Resource<List<Book>?>>

    suspend fun getSearchedBooks(query: String): Resource<List<Book>>
}