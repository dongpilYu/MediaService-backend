package com.mediaservice.application

import com.mediaservice.application.dto.media.WishContentResponseDto
import com.mediaservice.domain.repository.WishContentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class WishContentService(
    private val wishContentRepository: WishContentRepository
) {

    @Transactional(readOnly = true)
    fun findByProfileId(id: UUID): List<WishContentResponseDto> {
        return this.wishContentRepository.findByProfileId(id)
            .map { wishContent -> WishContentResponseDto.from(wishContent) }
    }
}
