package com.code7979.readerszone.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import code7979.LocalBookDetail
import com.code7979.Database
import com.code7979.readerszone.data.Resource
import com.code7979.readerszone.presentation.model.BookDetail
import com.code7979.readerszone.util.toBookDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class LocalBookRemoteDataSource(
    db: Database
) : LocalDataSource {
    private val queries = db.offline_bookQueries

    override fun getAllBook(context: CoroutineContext): Flow<List<LocalBookDetail>> {
        return queries.getAllOfflineBook().asFlow().mapToList(context)
    }

    override fun getAllBookResource(): Flow<Resource<List<LocalBookDetail>>> {
        return queries.getAllOfflineBook().asFlow().map {
            val offlineBookKList = it.executeAsList()
            if (offlineBookKList.isEmpty()) {
                Resource.Empty()
            } else {
                Resource.Success(offlineBookKList)
            }
        }
    }
    /*
    override suspend fun getAllBook(): Flow<Resource<List<Book>?>> {
        return withContext(Dispatchers.Default) {
            val bookList = mutableListOf<Book>()
            for (bookEntity in queries.getAllOfflineBook().executeAsList()) {
                bookList.add(
                    Book(
                        id = bookEntity.book_id,
                        title = bookEntity.title,
                        subtitle = bookEntity.subtitle,
                        authors = bookEntity.authors,
                        image = bookEntity.image,
                        url = bookEntity.url
                    )
                )
            }
            if (bookList.size <= 0) {
                flowOf(Resource.Empty())
            } else {
                flowOf(Resource.Success(bookList))
            }
        }
    }
     */

    override suspend fun getBookDetailsById(bookId: String): Flow<Resource<BookDetail?>> {
        return withContext(Dispatchers.IO) {
            val localBookDetail = queries.getBookByBookId(bookId).executeAsOneOrNull()
            if (localBookDetail != null) {
                flowOf(Resource.Success(localBookDetail.toBookDetail()))
            } else {
                flowOf(Resource.Empty())
            }
        }
    }

    override suspend fun getBookByBookId(book_id: String): LocalBookDetail? {
        return withContext(Dispatchers.IO) {
            queries.getBookByBookId(book_id).executeAsOneOrNull()
        }
    }

    override suspend fun deleteBookByBookId(book_id: String) {
        withContext(Dispatchers.IO) {
            queries.deleteBookByBookId(book_id)
        }
    }

    override suspend fun insertBookInDb(
        id: Long?,
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
    ) {
        withContext(Dispatchers.IO) {
            queries.insertBook(
                id,
                book_id,
                title,
                subtitle,
                authors,
                image,
                url,
                description,
                publisher,
                pages,
                year,
                download,
                fileUri
            )
        }
    }

}