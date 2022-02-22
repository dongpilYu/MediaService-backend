package com.mediaservice

import com.mediaservice.domain.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import com.mediaservice.infrastructure.AppInitiator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@Retention(AnnotationRetention.BINARY)
annotation class NoCoverageGenerated

typealias NoCoverage = NoCoverageGenerated

@NoCoverage
@SpringBootApplication
class MediaServiceBackendApplication

@NoCoverage
fun main(args: Array<String>) {
    runApplication<MediaServiceBackendApplication>(*args)

    if (System.getProperty("spring.profiles.active") == "local") {
        transaction {
            SchemaUtils.drop(
                UserTable, ProfileTable, MediaTable, MediaSeriesTable, MediaAllSeriesTable, ActorTable, CreatorTable, GenreTable, WishContentTable
            )
            SchemaUtils.create(
                UserTable, ProfileTable, MediaTable, MediaSeriesTable, MediaAllSeriesTable, ActorTable, CreatorTable, GenreTable, WishContentTable
            )
        }
        AppInitiator.localInit()
    }
}
