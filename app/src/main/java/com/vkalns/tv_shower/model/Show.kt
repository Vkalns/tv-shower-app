package com.vkalns.tv_shower.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Show(
    @JsonProperty("name") val name: String,
    @JsonProperty("image") val image: ShowImages,
    @JsonProperty("premiered") val premiered: String?
) {

    data class ShowImages(
        @JsonProperty("medium") val mediumImage: String,
        @JsonProperty("original") val originalImage: String
    )
}


