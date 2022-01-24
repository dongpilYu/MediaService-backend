package com.mediaservice.application

import com.mediaservice.application.dto.user.ProfileCreateDto
import com.mediaservice.application.dto.user.ProfileCreateResponseDto
import com.mediaservice.application.dto.user.ProfileResponseDto
import com.mediaservice.application.dto.user.SignInProfileResponseDto
import com.mediaservice.domain.Profile
import com.mediaservice.domain.repository.ProfileRepository
import com.mediaservice.domain.repository.UserRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ProfileService(
        private val profileRepository: ProfileRepository,
        private val userRepository: UserRepository
) {

    @Transactional(readOnly = true)
    fun findById(id: UUID): ProfileResponseDto {
        return ProfileResponseDto.from(
            this.profileRepository.findById(id)
                ?: throw BadRequestException(ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH PROFILE $id")
        )
    }

    @Transactional(readOnly = true)
    fun findByUserId(id: UUID): List<SignInProfileResponseDto> {
        return this.profileRepository.findByUserId(id)
            .map { profile -> SignInProfileResponseDto.from(profile) }
    }

    @Transactional(readOnly = true)
    fun createProfile(profileCreateDto : ProfileCreateDto, userId : UUID) : ProfileCreateResponseDto {

        val user = userRepository.findById(userId)
                ?: throw BadRequestException(ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH USER $userId")

        return ProfileCreateResponseDto.from(
                this.profileRepository.save(
                Profile.of(
                        name = profileCreateDto.name,
                        rate = profileCreateDto.rate,
                        mainImage = profileCreateDto.mainImage,
                        user = user,
                        isDeleted = true
                )
            )
        )

    }



}
