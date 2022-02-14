package com.mediaservice.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object WishContentTable : UUIDTable("TB_WishContent") {
    val profile = reference("profile", ProfileTable)
    val mediaAllSeries = reference("media_all_series", MediaAllSeriesTable)
}

class WishContent(
        var id: UUID?,
        var media: MediaAllSeries,
        var profile: Profile,
) {
    companion object {
        fun from(wishContentEntity: WishContentEntity) = WishContent(
                id = wishContentEntity.id.value,
                media = MediaAllSeries.from(wishContentEntity.mediaAllSeries),
                profile = Profile.from(wishContentEntity.profile)
        )
    }
}

class WishContentEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<WishContentEntity>(WishContentTable)

    var mediaAllSeries by MediaAllSeriesEntity referencedOn WishContentTable.mediaAllSeries
    var profile by ProfileEntity referencedOn WishContentTable.profile
}
