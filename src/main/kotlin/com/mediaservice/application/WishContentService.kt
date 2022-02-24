package com.mediaservice.application

import com.mediaservice.application.dto.media.WishContentResponseDto
import com.mediaservice.domain.WishContent
import com.mediaservice.domain.repository.MediaAllSeriesRepository
import com.mediaservice.domain.repository.ProfileRepository
import com.mediaservice.domain.repository.WishContentRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class WishContentService(
    private val wishContentRepository: WishContentRepository,
    private val profileRepository: ProfileRepository,
    private val mediaAllSeriesRepository: MediaAllSeriesRepository
) {

    @Transactional
    fun deleteWishContent(profileId: UUID, mediaAllSeriesId: UUID): List<WishContentResponseDto> {
        val mediaAllSeries = mediaAllSeriesRepository.findById(mediaAllSeriesId)
            ?: throw BadRequestException(ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH USER $mediaAllSeriesId")
        val profile = profileRepository.findById(profileId)
            ?: throw BadRequestException(ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH USER $profileId")

        return this.wishContentRepository.delete(
            WishContent.of(
                profile = profile,
                mediaAllSeries = mediaAllSeries
            )
        )?.map {
            WishContentResponseDto.from(it)
        }
    }
}
