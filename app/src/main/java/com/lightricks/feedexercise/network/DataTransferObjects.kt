package com.lightricks.feedexercise.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Feed(
    @SerialName("templatesMetadata")
    val templates: List<Template>
)

@Serializable
data class Template(
    @SerialName("id")
    val id: String,
    @SerialName("isPremium")
    val isPremium: Boolean,
    @SerialName("templateThumbnailURI")
    val thumbnailUrl: String,
)
