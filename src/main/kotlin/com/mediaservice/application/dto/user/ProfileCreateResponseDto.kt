package com.mediaservice.application.dto.user

import com.mediaservice.domain.Profile
import java.util.*

data class ProfileCreateResponseDto(
        val profileId: UUID,
        val mainImage : String

) {
    companion object {
        fun from(profile: Profile) = ProfileCreateResponseDto(
                profileId = profile.id!!,
                mainImage = profile.mainImage
        )
    }
}