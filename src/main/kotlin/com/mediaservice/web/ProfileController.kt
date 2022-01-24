package com.mediaservice.web

import com.mediaservice.application.ProfileService
import com.mediaservice.application.dto.user.ProfileCreateDto
import com.mediaservice.application.dto.user.ProfileCreateResponseDto
import com.mediaservice.application.dto.user.ProfileResponseDto
import com.mediaservice.application.dto.user.SignInProfileResponseDto
import com.mediaservice.domain.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/profiles")
class ProfileController(private val profileService: ProfileService) {
    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): ProfileResponseDto {
        return this.profileService.findById(id)
    }

    @GetMapping("/sign-in/{id}")
    fun findByUserId(@PathVariable id: UUID): List<SignInProfileResponseDto> {
        return this.profileService.findByUserId(id)
    }

    @PostMapping("/create-profile")
    fun createProfile(
        @RequestBody profileCreateDto: ProfileCreateDto,
        @AuthenticationPrincipal member: User
    ): ProfileCreateResponseDto? {
        return member.id?.let { this.profileService.createProfile(profileCreateDto, it) }
    }
}
