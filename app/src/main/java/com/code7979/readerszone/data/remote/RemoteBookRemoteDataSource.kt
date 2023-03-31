package com.code7979.readerszone.data.remote

import com.code7979.readerszone.data.Resource
import com.code7979.readerszone.data.remote.domain.BookApi
import com.code7979.readerszone.data.remote.domain.model.Book
import com.code7979.readerszone.data.remote.domain.model.RemoteBookDetail
import com.code7979.readerszone.data.remote.domain.model.BookResponse
import com.code7979.readerszone.presentation.model.BookDetail
import com.code7979.readerszone.util.toBookDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class RemoteBookRemoteDataSource constructor(private val bookApi: BookApi) : RemoteDataSource {

    override suspend fun getAllBook(): Flow<Resource<List<Book>?>> = callbackFlow {
        send(Resource.Loading())
        val callback = object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                val body = response.body()
                if (body != null) {
                    trySend(Resource.Success(body.books))
                } else {
                    trySend(Resource.Error(response.code().toString()))
                }
            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                trySend(
                    Resource.Error(
                        buildString {
                            append(call.request().url.toString())
                            append('\n')
                            append(t.message.toString())
                        },
                    ),
                )
            }

        }
        bookApi.getRecentBook().enqueue(callback)
        awaitClose {
            this.close()
        }
    }

    /**
    companion object {
    @Volatile
    private var INSTANCE: BookDataSource? = null

    fun getInstance(bookApi: BookApi): BookDataSource {
    return INSTANCE ?: synchronized(this) {
    val instance = BookDataSource(bookApi)
    INSTANCE = instance
    return instance
    }
    }
    }
     */
    override suspend fun getBookDetailsById(bookId: String): Flow<Resource<BookDetail?>> =
        callbackFlow {
            val callback = object : Callback<RemoteBookDetail> {
                override fun onResponse(
                    call: Call<RemoteBookDetail>,
                    response: Response<RemoteBookDetail>
                ) {
                    val body = response.body()
                    if (body != null) {
                        trySend(Resource.Success(body.toBookDetail()))
                    } else {
                        trySend(Resource.Error("Error with code :${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<RemoteBookDetail>, t: Throwable) {
                    trySend(
                        Resource.Error(
                            buildString {
                                append(call.request().url.toString())
                                append('\n')
                                append(t.message.toString())
                            },
                        ),
                    )

                }

            }
            bookApi.getBookById(bookId).enqueue(callback)
            awaitClose {
                this.close()
            }
        }

    override suspend fun searchBook(query: String): Flow<Resource<List<Book>?>> = callbackFlow {
        send(Resource.Loading())
        val callback = object : Callback<BookResponse> {
            override fun onResponse(
                call: Call<BookResponse>,
                response: Response<BookResponse>
            ) {
                val body = response.body()
                if (body != null) {
                    trySend(Resource.Success(body.books))
                } else {
                    trySend(Resource.Error(response.code().toString()))
                }
            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                trySend(
                    Resource.Error(
                        buildString {
                            append(call.request().url.toString())
                            append('\n')
                            append(t.message.toString())
                        },
                    ),
                )
            }
        }
        bookApi.searchBook(query).enqueue(callback)
        awaitClose {
            this.close()
        }
    }

    override suspend fun getSearchedBooks(query: String): Resource<List<Book>> {
        return withContext(Dispatchers.IO) {
            getSearchedBookResponse(query)
        }
    }

    private fun getSearchedBookResponse(query: String?): Resource<List<Book>> {
        val response: Response<BookResponse> = try {
            bookApi.searchBook(query!!).execute()
        } catch (e: IOException) {
            return Resource.Error("Turn on the internet")
        }
        return if (response.isSuccessful) {
            val bookResponse = response.body()
            if (bookResponse != null) {
                val bookList = bookResponse.books
                if (bookList.isNullOrEmpty()) {
                    Resource.Empty()
                } else {
                    Resource.Success(bookList)
                }
            } else {
                Resource.Empty()
            }
        } else {
            Resource.Error("Error Code " + response.code())
        }
    }
}
