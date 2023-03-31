package com.code7979.readerszone.presentation.screens.main_screen.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.code7979.readerszone.data.Resource
import com.code7979.readerszone.data.remote.RemoteDataSource
import com.code7979.readerszone.data.remote.domain.model.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val remoteBookDataSource: RemoteDataSource,
) : ViewModel() {

    private val _allBooks: MutableStateFlow<Resource<List<Book>?>> =
        MutableStateFlow(Resource.Empty())
    val allBook = _allBooks.asStateFlow()

    init {
        getAllBooks()
    }

    fun getAllBooks() {
        viewModelScope.launch {
            remoteBookDataSource.getAllBook().collect {
                _allBooks.value = it
            }
        }
    }
}
