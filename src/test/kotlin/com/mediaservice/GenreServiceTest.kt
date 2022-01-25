package com.mediaservice

import com.mediaservice.application.GenreService
import com.mediaservice.application.dto.media.GenreCreateRequestDto
import com.mediaservice.application.dto.media.GenreUpdateRequestDto
import com.mediaservice.domain.Genre
import com.mediaservice.domain.Role
import com.mediaservice.domain.User
import com.mediaservice.domain.repository.GenreRepository
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

class GenreServiceTest {
    private val genreRepository = mockk<GenreRepository>()
    private val genreService: GenreService = GenreService(this.genreRepository)
    private lateinit var genreId: UUID
    private lateinit var adminId: UUID
    private lateinit var userId: UUID
    private lateinit var genre: Genre
    private lateinit var deletedGenre: Genre
    private lateinit var admin: User
    private lateinit var user: User

    @BeforeEach
    fun setup() {
        clearAllMocks()
        this.genreId = UUID.randomUUID()
        this.adminId = UUID.randomUUID()
        this.userId = UUID.randomUUID()
        this.genre = Genre(this.genreId, "test Genre", isDeleted = false)
        this.deletedGenre = Genre(this.genreId, "test Genre", isDeleted = true)
        this.admin = User(this.adminId, "admin@gmail.com", "1234", Role.ADMIN)
        this.user = User(this.userId, "user@gmail.com", "1234", Role.USER)
    }

    @Test
    fun successCreate() {
        // given
        mockkObject(Genre)
        val genreCreateRequestDto = GenreCreateRequestDto("test Genre")

        every { Genre.of(genreCreateRequestDto.name) } returns this.genre
        every { genreRepository.save(genre) } returns this.genre

        // when
        val genreResponseDto = this.genreService.create(genreCreateRequestDto)

        // then
        assertEquals(this.genre.name, genreResponseDto.name)
    }

    @Test
    fun successUpdate() {
        // given
        val genreUpdateRequestDto = GenreUpdateRequestDto("test Genre")

        every { genreRepository.findById(genreId) } returns this.genre
        every { genreRepository.update(genreId, genre) } returns this.genre

        // when
        val actorResponseDto = this.genreService.update(genreId, genreUpdateRequestDto)

        // then
        assertEquals(this.genre.name, actorResponseDto.name)
    }

    @Test
    fun failUpdate_CannotFindGenre() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val genreUpdateRequestDto = GenreUpdateRequestDto("test Genre")

            every { genreRepository.findById(genreId) } returns null

            // when
            this.genreService.update(genreId, genreUpdateRequestDto)
        }

        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun failUpdate_DeletedGenre() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val genreUpdateRequestDto = GenreUpdateRequestDto("test Genre")

            every { genreRepository.findById(genreId) } returns this.deletedGenre

            // when
            this.genreService.update(genreId, genreUpdateRequestDto)
        }

        // then
        assertEquals(ErrorCode.ROW_ALREADY_DELETED, exception.errorCode)
    }
}
