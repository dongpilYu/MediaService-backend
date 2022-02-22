package com.mediaservice.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object WishContentTable : UUIDTable("TB_WISHCONTENT") {
    val profile = reference("profile", ProfileTable)
    val mediaAllSeries = reference("media_all_series", MediaAllSeriesTable)
    val isDeleted: Column<Boolean> = bool("isDeleted")
}

class WishContent(
    var id: UUID?,
    var mediaAllSeries: MediaAllSeries,
    var profile: Profile,
    var isDeleted: Boolean
) {
    companion object {
        fun from(wishContentEntity: WishContentEntity) = WishContent(
            id = wishContentEntity.id.value,
            mediaAllSeries = MediaAllSeries.from(wishContentEntity.mediaAllSeries),
            profile = Profile.from(wishContentEntity.profile),
            isDeleted = false
        )
        fun of(mediaAllSeries: MediaAllSeries, profile: Profile) = WishContent(
            id = null,
            mediaAllSeries = mediaAllSeries,
            profile = profile,
            isDeleted = false
        )
    }
}

class WishContentEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<WishContentEntity>(WishContentTable)

    var mediaAllSeries by MediaAllSeriesEntity referencedOn WishContentTable.mediaAllSeries
    var profile by ProfileEntity referencedOn WishContentTable.profile
    var isDeleted by WishContentTable.isDeleted
}
