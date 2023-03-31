package com.code7979.readerszone.presentation.screens.main_screen.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.code7979.readerszone.data.Resource
import com.code7979.readerszone.data.remote.RemoteDataSource
import com.code7979.readerszone.data.remote.domain.model.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _books = MutableStateFlow<Resource<List<Book>?>>(Resource.Empty())

    val books: StateFlow<Resource<List<Book>?>> = searchText
        .debounce(2500L)
        .onEach { _isSearching.update { true } }
        .combine(_books) { searchText, books ->
            if (searchText.isBlank()) {
                books
            } else {
               remoteDataSource.getSearchedBooks(searchText)
            }

        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _books.value
        )

    private suspend fun searchBook(searchText: String) {
        remoteDataSource.searchBook(searchText).collect {
            _books.value = it
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

}