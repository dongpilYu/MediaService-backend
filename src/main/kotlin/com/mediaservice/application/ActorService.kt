package com.mediaservice.application

import com.mediaservice.application.dto.media.ActorCreateRequestDto
import com.mediaservice.application.dto.media.ActorResponseDto
import com.mediaservice.application.dto.media.ActorUpdateRequestDto
import com.mediaservice.application.validator.IsDeletedValidator
import com.mediaservice.application.validator.Validator
import com.mediaservice.domain.Actor
import com.mediaservice.domain.repository.ActorRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ActorService(
    private val actorRepository: ActorRepository
) {
    @Transactional
    fun create(actorCreateRequestDto: ActorCreateRequestDto): ActorResponseDto {
        return ActorResponseDto.from(
            this.actorRepository.save(
                Actor.of(actorCreateRequestDto.name)
            )
        )
    }

    @Transactional
    fun update(id: UUID, actorUpdateRequestDto: ActorUpdateRequestDto): ActorResponseDto {
        val actorForUpdate = this.actorRepository.findById(id) ?: throw BadRequestException(
            ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH ACTOR $id"
        )

        val validator: Validator = IsDeletedValidator(actorForUpdate.isDeleted, Actor.DOMAIN)
        validator.validate()

        actorForUpdate.update(actorUpdateRequestDto.name)

        return ActorResponseDto.from(
            this.actorRepository.update(
                id,
                actorForUpdate
            ) ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH ACTOR $id"
            )
        )
    }
}
