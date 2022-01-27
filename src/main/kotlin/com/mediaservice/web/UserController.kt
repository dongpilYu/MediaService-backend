package com.mediaservice.web

import com.mediaservice.application.UserService
import com.mediaservice.application.dto.user.UserResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val userService: UserService) {
    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): UserResponseDto {
        return this.userService.findById(id)
    }

    @PostMapping("/mail-test")
    fun sendTestMail() {
        this.userService.testMail()
    }
}
