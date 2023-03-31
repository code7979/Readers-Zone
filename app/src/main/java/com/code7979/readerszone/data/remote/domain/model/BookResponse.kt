package com.code7979.readerszone.data.remote.domain.model

import com.fasterxml.jackson.annotation.JsonProperty

data class BookResponse(
    @JsonProperty("status") val status: String,
    @JsonProperty("total") val total: Int,
    @JsonProperty("books") val books: List<Book>?,
)
