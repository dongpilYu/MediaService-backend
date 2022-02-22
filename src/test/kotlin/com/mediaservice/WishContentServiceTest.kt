package com.mediaservice

import com.mediaservice.application.WishContentService
import com.mediaservice.application.dto.media.WishContentResponseDto
import com.mediaservice.domain.MediaAllSeries
import com.mediaservice.domain.Profile
import com.mediaservice.domain.Role
import com.mediaservice.domain.User
import com.mediaservice.domain.WishContent
import com.mediaservice.domain.repository.MediaAllSeriesRepository
import com.mediaservice.domain.repository.ProfileRepository
import com.mediaservice.domain.repository.WishContentRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

class WishContentServiceTest {
    private val wishContentRepository = mockk<WishContentRepository>()
    private val profileRepository = mockk<ProfileRepository>()
    private val mediaAllSeriesRepository = mockk<MediaAllSeriesRepository>()

    private val wishContentService: WishContentService = WishContentService(this.wishContentRepository, this.profileRepository, this.mediaAllSeriesRepository)

    private lateinit var profileId: UUID
    private lateinit var mediaAllSeriesId: UUID
    private lateinit var userId: UUID
    private lateinit var wishContentId: UUID
    private lateinit var profile: Profile
    private lateinit var user: User
    private lateinit var wishContent: WishContent
    private lateinit var mediaAllSeries: MediaAllSeries

    private lateinit var wishContentResponseDto: WishContentResponseDto

    @BeforeEach
    fun setup() {
        clearAllMocks()
        this.profileId = UUID.randomUUID()
        this.mediaAllSeriesId = UUID.randomUUID()
        this.userId = UUID.randomUUID()
        this.wishContentId = UUID.randomUUID()

        this.user = User(userId, "user@gmail.com", "1234", Role.USER)
        this.profile = Profile(profileId, user, "action", "19+", "image_url", false)
        this.mediaAllSeries = MediaAllSeries(mediaAllSeriesId, "title", "synopsis", "trailer", "thumbnail", "rate", false)
        this.wishContent = WishContent(wishContentId, mediaAllSeries, profile, false)

        this.wishContentResponseDto = WishContentResponseDto(wishContentId, "action", "title", "synopsis", "terailer", "thumbnail", "rate", false)
    }


    @Test
    fun successCreate() {
        // given
        every { mediaAllSeriesRepository.findById(mediaAllSeriesId) } returns mediaAllSeries
        every { profileRepository.findById(profileId) } returns profile
        every { wishContentRepository.alreadyInserted(profileId, mediaAllSeriesId) } returns false
        every { wishContentRepository.save(any()) } returns wishContent

        // when
        val wishContentResponseDto = this.wishContentService.createWishContent(profileId, mediaAllSeriesId)

        // then
        assertEquals(wishContentResponseDto.profileName, wishContent.profile.name)
    }

    @Test
    fun failCreate_AlreadyInserted() {
        val exception = Assertions.assertThrows(BadRequestException::class.java) {
            // given
            every { mediaAllSeriesRepository.findById(mediaAllSeriesId) } returns mediaAllSeries
            every { profileRepository.findById(profileId) } returns profile
            every { wishContentRepository.alreadyInserted(profileId, mediaAllSeriesId) } returns true
            every { wishContentRepository.save(any()) } returns wishContent

            // when
            this.wishContentService.createWishContent(profileId, mediaAllSeriesId)
        }

        // then
        assertEquals(ErrorCode.ROW_ALREADY_EXIST, exception.errorCode)
    }

}
