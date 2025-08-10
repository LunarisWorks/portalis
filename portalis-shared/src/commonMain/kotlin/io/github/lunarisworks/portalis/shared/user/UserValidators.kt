package io.github.lunarisworks.portalis.shared.user

import io.konform.validation.Validation
import io.konform.validation.constraints.maxLength
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.pattern
import io.konform.validation.required

private val EmailPattern = "^[\\w\\-.]+@([\\w-]+\\.)+[\\w-]{2,}$".toRegex()

val validateEmail =
    Validation {
        required {
            pattern(EmailPattern) hint "must be a valid email address"
            maxLength(64)
        }
    }

private val UsernamePattern = "^(?![0-9_])[a-zA-Z0-9_]{3,32}(?<!_)$".toRegex()

val validateUsername =
    Validation {
        required {
            pattern(UsernamePattern) hint "must be 3-32 characters long and can only contain letters, numbers, and underscores"
        }
    }

private val PasswordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$".toRegex()

val validatePassword =
    Validation {
        required {
            pattern(PasswordPattern) hint
                "must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one number"
        }
    }

val validateName =
    Validation {
        required {
            minLength(3)
            maxLength(64)
        }
    }
