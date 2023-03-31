package com.code7979.readerszone.data.remote.domain.model

import com.fasterxml.jackson.annotation.JsonProperty

open class Book(
    @JsonProperty("id") open val id: String,
    @JsonProperty("title") open val title: String,
    @JsonProperty("subtitle") open val subtitle: String,
    @JsonProperty("authors") open val authors: String,
    @JsonProperty("image")open val image: String,
    @JsonProperty("url")open val url: String,
)
