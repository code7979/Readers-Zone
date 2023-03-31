package com.code7979.readerszone.worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.code7979.readerszone.App.Companion.CHANNEL_ID
import com.code7979.readerszone.R
import com.code7979.readerszone.data.TempUUID
import com.code7979.readerszone.data.local.LocalDataSource
import com.code7979.readerszone.data.remote.domain.BookApi
import com.code7979.readerszone.util.getTrueFile
import com.code7979.readerszone.util.toRemoteBookDetail
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

@HiltWorker
class FileDownloader @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val params: WorkerParameters,
    private val bookApi: BookApi,
    private val localDataSourceDb: LocalDataSource
) : CoroutineWorker(appContext = appContext, params = params) {
    companion object {
        const val KEY_BOOK_DETAIL_JSON = "book-details"
    }

    private val notificationId = 245

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        val inputData = params.inputData
        val bookDetailsJsonString =
            inputData.getString(KEY_BOOK_DETAIL_JSON) ?: return Result.failure(
                workDataOf(WorkerConstant.ERROR_MSG to "BookDetailJsonString is null")
            )
        val bookDetails = bookDetailsJsonString.toRemoteBookDetail() ?: return Result.failure(
            workDataOf(WorkerConstant.ERROR_MSG to "BookDetails is null")
        )

        //store uuid in TempUUID singleton class
        TempUUID.getInstance().addUUIDInDb(bookDetails.book_id, params.id)

        setForeground(createForegroundInfo(bookDetails.title))

        val response = bookApi.downloadFile(bookDetails.download)
        response.body()?.let { body ->
            return withContext(Dispatchers.IO) {
                val file = getTrueFile(appContext, bookDetails.book_id)
                val outputStream = FileOutputStream(file)
                outputStream.use { stream ->
                    try {
                        stream.write(body.bytes())
                    } catch (e: IOException) {
                        return@withContext Result.failure(
                            workDataOf(
                                WorkerConstant.ERROR_MSG to e.localizedMessage
                            )
                        )
                    }
                }
                //insert book details in database
                localDataSourceDb.insertBookInDb(
                    book_id = bookDetails.book_id,
                    title = bookDetails.title,
                    subtitle = bookDetails.subtitle,
                    image = bookDetails.image,
                    download = bookDetails.download,
                    authors = bookDetails.authors,
                    description = bookDetails.description,
                    url = bookDetails.url,
                    publisher = bookDetails.publisher,
                    pages = bookDetails.pages,
                    year = bookDetails.year,
                    fileUri = file.toUri().toString()
                )

                Result.success(
                    workDataOf(
                        WorkerConstant.FILE_URI to file.toUri().toString()
                    )
                )
            }
        }
        if (!response.isSuccessful) {
            if (response.code().toString().startsWith("5")) {
                return Result.Failure(
                    workDataOf(
                        WorkerConstant.ERROR_MSG to "Server error"
                    )
                )
            }
            return Result.failure(
                workDataOf(
                    WorkerConstant.ERROR_MSG to "NetWork error"
                )
            )
        }
        return Result.failure(
            workDataOf(
                WorkerConstant.ERROR_MSG to "Unknown error"
            )
        )
    }

    private fun createForegroundInfo(title: String = ""): ForegroundInfo {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText("Downloading...")
            .setSmallIcon(R.drawable.file_download)
            .setOngoing(true)
            .build()

        return ForegroundInfo(notificationId, notification)
    }
}