package com.code7979.readerszone.presentation.screens.main_screen.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.code7979.readerszone.data.local.LocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
   private val localDataSource: LocalDataSource
) : ViewModel() {
    val allBooks = localDataSource.getAllBookResource()

    fun deleteBook(bookId: String) {
        viewModelScope.launch {
            localDataSource.deleteBookByBookId(bookId)
        }
    }
}
