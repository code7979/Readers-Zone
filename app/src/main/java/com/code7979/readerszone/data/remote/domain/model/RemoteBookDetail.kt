package com.code7979.readerszone.data.remote.domain.model

import com.fasterxml.jackson.annotation.JsonProperty

data class RemoteBookDetail(
    @JsonProperty("status") val status: String,
    @JsonProperty("id") override val id: String,
    @JsonProperty("title") override val title: String,
    @JsonProperty("subtitle") override val subtitle: String,
    @JsonProperty("authors") override val authors: String,
    @JsonProperty("image") override val image: String,
    @JsonProperty("url") override val url: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("publisher") val publisher: String,
    @JsonProperty("pages") val page: String,
    @JsonProperty("year") val year: String,
    @JsonProperty("download") val download: String
) : Book(id, title, subtitle, authors, image, url)
