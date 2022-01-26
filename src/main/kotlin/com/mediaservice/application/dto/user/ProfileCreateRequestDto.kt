package com.mediaservice.application.dto.user

import com.mediaservice.domain.Profile

data class ProfileCreateRequestDto(
    val name: String,
    val rate: String,
    val mainImage: String
)