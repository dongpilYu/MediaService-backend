package com.mediaservice.web

import com.mediaservice.application.WishContentService
import com.mediaservice.application.dto.media.WishContentResponseDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/wish-contents")
class WishContentController(private val wishContentService: WishContentService) {

    @DeleteMapping("/{profileId}")
    fun deleteWishContent(@PathVariable profileId: UUID, @RequestParam("mediaAllSeriesId") mediaAllSeriesId: UUID): List<WishContentResponseDto> {
        return this.wishContentService.deleteWishContent(profileId, mediaAllSeriesId)
    }
}
