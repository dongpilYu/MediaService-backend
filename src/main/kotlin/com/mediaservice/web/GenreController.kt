package com.mediaservice.web

import com.mediaservice.application.GenreService
import com.mediaservice.application.dto.media.GenreCreateRequestDto
import com.mediaservice.application.dto.media.GenreResponseDto
import com.mediaservice.application.dto.media.GenreUpdateRequestDto
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/genre")
class GenreController(private val genreService: GenreService) {
    @PostMapping("")
    fun create(
        @RequestBody genreCreateRequestDto: GenreCreateRequestDto
    ): GenreResponseDto {
        return this.genreService.create(genreCreateRequestDto)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody genreUpdateRequestDto: GenreUpdateRequestDto
    ): GenreResponseDto {
        return this.genreService.update(id, genreUpdateRequestDto)
    }
}
