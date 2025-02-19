package com.mediaservice.web

import com.mediaservice.application.ActorService
import com.mediaservice.application.dto.media.ActorCreateRequestDto
import com.mediaservice.application.dto.media.ActorResponseDto
import com.mediaservice.application.dto.media.ActorUpdateRequestDto
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/actor")
class ActorController(private val actorService: ActorService) {
    @PostMapping("")
    fun create(
        @RequestBody actorCreateRequestDto: ActorCreateRequestDto
    ): ActorResponseDto {
        return this.actorService.create(actorCreateRequestDto)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody actorUpdateRequestDto: ActorUpdateRequestDto
    ): ActorResponseDto {
        return this.actorService.update(id, actorUpdateRequestDto)
    }
}
