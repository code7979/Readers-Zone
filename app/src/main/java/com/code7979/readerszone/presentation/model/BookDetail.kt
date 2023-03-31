package com.code7979.readerszone.presentation.model

data class BookDetail(
    val status: String,
    val id: String,
    val title: String,
    val subtitle: String,
    val authors: String,
    val image: String,
    val url: String,
    val description: String,
    val publisher: String,
    val page: String,
    val year: String,
    val bookState: BookState<String>
//    val download: String,
//    val fileUri: Uri?
)