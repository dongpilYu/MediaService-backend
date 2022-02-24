package com.mediaservice.domain.repository

import com.mediaservice.domain.WishContent
import com.mediaservice.domain.WishContentEntity
import com.mediaservice.domain.WishContentTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insertAndGetId
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class WishContentRepository {

    fun save(wishContent: WishContent): WishContent {
        val wishContentId = WishContentTable.insertAndGetId {
            it[mediaAllSeries] = wishContent.mediaAllSeries.id
            it[profile] = wishContent.profile.id
            it[isDeleted] = false
        }
        wishContent.id = wishContentId.value

        return wishContent
    }

    fun delete(wishContent: WishContent): List<WishContent> {
        return WishContentEntity.find {
            WishContentTable.profile eq wishContent.profile.id and (WishContentTable.mediaAllSeries eq wishContent.mediaAllSeries.id) and (WishContentTable.isDeleted eq false)
        }?.map {
            it.isDeleted = true
            WishContent.from(it)
        }
    }

    fun findByProfileIdAndMediaAllSeriesId(profileId: UUID, mediaAllSeriesId: UUID): Boolean {
        return !WishContentEntity.find {
            WishContentTable.profile eq profileId and (WishContentTable.mediaAllSeries eq mediaAllSeriesId) and (WishContentTable.isDeleted eq false)
        }.empty()
    }
}
