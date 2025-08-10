package io.github.lunarisworks.portalis.shared.auth

import io.github.lunarisworks.portalis.shared.user.validateEmail
import io.github.lunarisworks.portalis.shared.user.validateName
import io.github.lunarisworks.portalis.shared.user.validatePassword
import io.github.lunarisworks.portalis.shared.user.validateUsername
import io.konform.validation.Validation
import io.konform.validation.constraints.notBlank

val validateLoginRequest =
    Validation {
        LoginRequest::username {
            notBlank()
        }
        LoginRequest::password {
            notBlank()
        }
    }

val validateRegisterRequest =
    Validation<RegisterRequest> {
        validate("username", { it.username.lowercase() }) {
            notBlank()
            run(validateUsername)
        }
        RegisterRequest::email {
            notBlank()
            run(validateEmail)
        }
        RegisterRequest::password {
            notBlank()
            run(validatePassword)
        }
        RegisterRequest::name ifPresent {
            notBlank()
            run(validateName)
        }
    }
