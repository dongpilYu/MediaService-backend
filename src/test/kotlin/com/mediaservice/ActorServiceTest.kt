package com.mediaservice

import com.mediaservice.application.ActorService
import com.mediaservice.application.dto.media.ActorCreateRequestDto
import com.mediaservice.application.dto.media.ActorUpdateRequestDto
import com.mediaservice.domain.Actor
import com.mediaservice.domain.Role
import com.mediaservice.domain.User
import com.mediaservice.domain.repository.ActorRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

class ActorServiceTest {
    private val actorRepository = mockk<ActorRepository>()
    private val actorService: ActorService = ActorService(this.actorRepository)
    private lateinit var actorId: UUID
    private lateinit var adminId: UUID
    private lateinit var userId: UUID
    private lateinit var actor: Actor
    private lateinit var deletedActor: Actor
    private lateinit var admin: User
    private lateinit var user: User

    @BeforeEach
    fun setup() {
        clearAllMocks()
        this.actorId = UUID.randomUUID()
        this.adminId = UUID.randomUUID()
        this.userId = UUID.randomUUID()
        this.actor = Actor(this.actorId, "test Actor", isDeleted = false)
        this.deletedActor = Actor(this.actorId, "test Actor", isDeleted = true)
        this.admin = User(this.adminId, "admin@gmail.com", "1234", Role.ADMIN)
        this.user = User(this.userId, "user@gmail.com", "1234", Role.USER)
    }

    @Test
    fun successCreate() {
        // given
        mockkObject(Actor)
        val actorCreateRequestDto = ActorCreateRequestDto("test Actor")

        every { Actor.of(actorCreateRequestDto.name) } returns this.actor
        every { actorRepository.save(actor) } returns this.actor

        // when
        val actorResponseDto = this.actorService.create(actorCreateRequestDto)

        // then
        assertEquals(this.actor.name, actorResponseDto.name)
    }

    @Test
    fun successUpdate() {
        // given
        val actorUpdateRequestDto = ActorUpdateRequestDto("test Actor")

        every { actorRepository.findById(actorId) } returns this.actor
        every { actorRepository.update(actorId, actor) } returns this.actor

        // when
        val actorResponseDto = this.actorService.update(actorId, actorUpdateRequestDto)

        // then
        assertEquals(this.actor.name, actorResponseDto.name)
    }

    @Test
    fun failUpdate_CannotFindActor() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val actorUpdateRequestDto = ActorUpdateRequestDto("test Actor")

            every { actorRepository.findById(actorId) } returns null

            // when
            this.actorService.update(actorId, actorUpdateRequestDto)
        }

        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun failUpdate_DeletedActor() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val actorUpdateRequestDto = ActorUpdateRequestDto("test Actor")

            every { actorRepository.findById(actorId) } returns this.deletedActor

            // when
            this.actorService.update(actorId, actorUpdateRequestDto)
        }

        // then
        assertEquals(ErrorCode.ROW_ALREADY_DELETED, exception.errorCode)
    }
}
