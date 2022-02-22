package com.mediaservice.application.dto.media

import com.mediaservice.domain.WishContent
import java.util.UUID

data class WishContentResponseDto(
    val id: UUID,
    val profileName: String,
    val title: String,
    val synopsis: String,
    val trailer: String,
    val thumbnail: String,
    val rate: String,
    val isSeries: Boolean

) {
    companion object {
        fun from(wishContent: WishContent) = WishContentResponseDto(
            id = wishContent.id!!,
            profileName = wishContent.profile.name,
            title = wishContent.mediaAllSeries.title,
            synopsis = wishContent.mediaAllSeries.synopsis,
            trailer = wishContent.mediaAllSeries.trailer,
            thumbnail = wishContent.mediaAllSeries.thumbnail,
            rate = wishContent.mediaAllSeries.rate,
            isSeries = wishContent.mediaAllSeries.isSeries
        )
    }
}
