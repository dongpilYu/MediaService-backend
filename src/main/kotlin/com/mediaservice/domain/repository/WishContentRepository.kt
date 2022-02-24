package com.mediaservice.domain.repository

import com.mediaservice.domain.WishContent
import com.mediaservice.domain.WishContentEntity
import com.mediaservice.domain.WishContentTable
import org.jetbrains.exposed.sql.and
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class WishContentRepository {
    fun findByProfileId(id: UUID): List<WishContent> {
        return WishContentEntity.find {
            WishContentTable.profile eq id and (WishContentTable.isDeleted eq false)
        }.map { WishContent.from(it) }
    }
}
