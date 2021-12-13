package com.mediaservice.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object UserTable : UUIDTable("TB_USER") {
    val email: Column<String> = varchar("email", 255).uniqueIndex()
    val password: Column<String> = varchar("password", 255)
    val admin: Column<Boolean> = bool("admin")
}

class User(var id: UUID, var email: String, var password: String, var admin: Boolean) {
    companion object {
        fun from(userEntity: UserEntity) = User(
            id = userEntity.id.value,
            email = userEntity.email,
            password = userEntity.password,
            admin = userEntity.admin
        )
    }
}

class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(UserTable)

    var email by UserTable.email
    var password by UserTable.password
    var admin by UserTable.admin
}

