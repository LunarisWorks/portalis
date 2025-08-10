package io.github.lunarisworks.portalis.server.infrastructure.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

internal fun createPasswordEncoder(): PasswordEncoder {
    val bcrypt = "bcrypt"
    return DelegatingPasswordEncoder(
        bcrypt,
        mapOf(bcrypt to BCryptPasswordEncoder()),
    )
}
