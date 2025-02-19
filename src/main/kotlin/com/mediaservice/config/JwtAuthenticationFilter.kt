package com.mediaservice.config

import io.jsonwebtoken.io.IOException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(
    private val tokenProvider: JwtTokenProvider
) : GenericFilterBean() {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val accessToken: String? = this.tokenProvider.resolveAccessToken(request as HttpServletRequest)
        val refreshToken: String? = this.tokenProvider.resolveRefreshToken(request)

        if (accessToken != null && this.tokenProvider.validateToken(
                accessToken,
                refreshToken,
                request,
                response as HttpServletResponse
            )
        ) {
            val auth: Authentication = this.tokenProvider.getAuthentication(accessToken, request)
            SecurityContextHolder.getContext().authentication = auth
        }
        chain.doFilter(request, response)
    }
}
