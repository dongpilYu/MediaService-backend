package com.mediaservice

import com.mediaservice.application.MediaService
import com.mediaservice.application.dto.media.MediaResponseDto
import com.mediaservice.domain.Actor
import com.mediaservice.domain.Creator
import com.mediaservice.domain.Genre
import com.mediaservice.domain.Media
import com.mediaservice.domain.MediaContents
import com.mediaservice.domain.MediaSeries
import com.mediaservice.domain.Profile
import com.mediaservice.domain.Role
import com.mediaservice.domain.User
import com.mediaservice.domain.repository.MediaRepository
import com.mediaservice.domain.repository.ProfileRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

class MediaServiceTest {
    private var profileRepository = mockk<ProfileRepository>()
    private var mediaRepository = mockk<MediaRepository>()
    private val mediaService: MediaService = MediaService(this.profileRepository, this.mediaRepository)
    private lateinit var media: Media
    private lateinit var mediaSeries: MediaSeries
    private lateinit var mediaContents: MediaContents
    private lateinit var userId: UUID
    private lateinit var profileId: UUID
    private lateinit var mediaId: UUID
    private lateinit var mediaSeriesId: UUID
    private lateinit var mediaContentsId: UUID
    private lateinit var user: User
    private lateinit var profile: Profile
    private lateinit var actorList: List<Actor>
    private lateinit var genreList: List<Genre>
    private lateinit var creatorList: List<Creator>

    @BeforeEach
    fun setup() {
        clearAllMocks()
        this.userId = UUID.randomUUID()
        this.profileId = UUID.randomUUID()
        this.mediaId = UUID.randomUUID()
        this.mediaSeriesId = UUID.randomUUID()
        this.mediaContentsId = UUID.randomUUID()
        this.user = User(userId, "test@gmail.com", "test123!@", Role.USER)
        this.profile = Profile(profileId, this.user, "test profile", "19+", "test.jpg", false)
        this.actorList = listOf(Actor(UUID.randomUUID(), "testActor", false))
        this.genreList = listOf(Genre(UUID.randomUUID(), "testGenre", false))
        this.creatorList = listOf(Creator(UUID.randomUUID(), "testCreator", false))
        this.mediaContents = MediaContents(
            mediaContentsId, "test title", "test synopsis", "test trailer",
            "test thumbnail url", "19+",
            isSeries = true,
            isDeleted = false,
            actorList = this.actorList,
            genreList = this.genreList,
            creatorList = this.creatorList
        )
        this.mediaSeries = MediaSeries(mediaSeriesId, "season 1", 1, false, this.mediaContents)
        this.media = Media(
            mediaId, "test video 1", "test synopsis", 1, "test url",
            "test thumbnail", 100, false, this.mediaSeries
        )
    }

    @Test
    fun successFindById() {
        // given
        every { mediaRepository.findById(mediaId) } returns this.media
        every { profileRepository.findById(profileId) } returns this.profile

        // when
        val mediaResponseDto: MediaResponseDto = this.mediaService.findById(this.userId, this.profileId, this.mediaId)

        // then
        assertEquals(this.media.name, mediaResponseDto.name)
    }

    @Test
    fun failFindById() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            every { mediaRepository.findById(mediaId) } returns null
            every { profileRepository.findById(profileId) } returns this.profile

            // when
            this.mediaService.findById(this.userId, this.profileId, this.mediaId)
        }

        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun successFindByMediaSeries() {
        // given
        every { mediaRepository.findByMediaSeriesId(mediaSeriesId) } returns listOf(this.media)
        every { profileRepository.findById(profileId) } returns this.profile

        // when
        val mediaList: List<MediaResponseDto> = this.mediaService.findByMediaSeries(this.userId, this.profileId, this.mediaSeriesId)

        // then
        assertEquals(this.media.name, mediaList[0].name)
    }

    @Test
    fun failFindByMediaSeries() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            every { mediaRepository.findByMediaSeriesId(mediaSeriesId) } returns null
            every { profileRepository.findById(profileId) } returns this.profile

            // when
            this.mediaService.findByMediaSeries(this.userId, this.profileId, this.mediaSeriesId)
        }

        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun successDeleteById() {
        // given
        every { mediaRepository.deleteById(mediaId) } returns this.media

        // when
        val mediaResponseDto: MediaResponseDto = this.mediaService.deleteById(this.mediaId)

        // then
        assertEquals(this.media.name, mediaResponseDto.name)
    }

    @Test
    fun failDeleteById() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            every { mediaRepository.deleteById(mediaId) } returns null

            // when
            this.mediaService.deleteById(this.mediaId)
        }

        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }
}
