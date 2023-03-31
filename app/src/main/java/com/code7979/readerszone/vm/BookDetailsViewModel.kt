package com.code7979.readerszone.vm

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import code7979.LocalBookDetail
import com.code7979.readerszone.data.local.LocalDataSource
import com.code7979.readerszone.data.remote.RemoteDataSource
import com.code7979.readerszone.presentation.model.BookDetail
import com.code7979.readerszone.presentation.navigation.BOOK_ID
import com.code7979.readerszone.util.toBookDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.code7979.readerszone.data.Resource
import com.code7979.readerszone.data.TempUUID
import com.code7979.readerszone.presentation.model.BookState
import com.code7979.readerszone.util.convertToJson
import com.code7979.readerszone.util.toLocalBookDetail
import com.code7979.readerszone.worker.FileDownloader
import com.code7979.readerszone.worker.WorkerConstant.UNIQUE_WORK_NAME
import kotlinx.coroutines.Dispatchers
import java.util.*

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val remoteBookDataSource: RemoteDataSource,
    private val localBookDataSource: LocalDataSource
) : ViewModel() {

    private val _bookDetail = MutableStateFlow<Resource<BookDetail?>>(Resource.Empty())
    val bookDetails = _bookDetail.asStateFlow()

    private val _bookState = MutableStateFlow<BookState<String>>(BookState.Download(""))
    val bookState = _bookState.asStateFlow()

    private val workManager: WorkManager = WorkManager.getInstance(application)
    val workInfos = workManager.getWorkInfosForUniqueWorkLiveData(UNIQUE_WORK_NAME)

    init {
        getBookDetail(savedStateHandle)
    }

    private fun getBookDetail(savedStateHandle: SavedStateHandle) {
        _bookDetail.value = Resource.Loading()
        savedStateHandle.get<String>(BOOK_ID)?.let { bookId ->
            viewModelScope.launch {
                val localBookDetail = getLocalBookDetail(bookId = bookId, scope = this)
                if (localBookDetail != null) {
                    _bookDetail.value = Resource.Success(localBookDetail.toBookDetail())
                    _bookState.value = BookState.Read(localBookDetail.fileUri)
                    return@launch
                }
                remoteBookDataSource.getBookDetailsById(bookId = bookId).collect {
                    when (it) {
                        is Resource.Success -> {
                            _bookDetail.value = it
                            if (it.data != null) {
                                _bookState.value = it.data.bookState
                            }
                        }
                        is Resource.Error -> {
                            _bookDetail.value = it
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private suspend fun getLocalBookDetail(
        bookId: String,
        scope: CoroutineScope
    ): LocalBookDetail? = withContext(scope.coroutineContext) {
        localBookDataSource.getBookByBookId(bookId)
    }

    fun downloadBook(bookDetail: BookDetail) {
        viewModelScope.launch(Dispatchers.Default) {
            startWorker(bookDetail, workManager)
        }

    }

    private fun startWorker(
        bookDetail: BookDetail,
        workManager: WorkManager
    ) {
        val bookDetailJson = bookDetail.toLocalBookDetail().convertToJson()
        val data = Data.Builder()
            .putString(FileDownloader.KEY_BOOK_DETAIL_JSON, bookDetailJson)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .build()

        val downloadWorkReq = OneTimeWorkRequestBuilder<FileDownloader>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        workManager.beginUniqueWork(
            UNIQUE_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            downloadWorkReq
        ).enqueue()
    }

    fun getUUIDByBookId(bookId: String): UUID? {
        return TempUUID.getInstance().getUUIDbyId(bookId)
    }

}
