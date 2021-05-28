package com.simplegapps.fundamentosandroid.common

import java.util.regex.Pattern

object Validators {

    private val USERNAME_PATTERN = "^[a-zA-Z0-9_]{5,}\$"
    private val PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}\$"

    fun isValidUsername(username: String): Boolean {
        return Pattern.compile(USERNAME_PATTERN).matcher(username).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return Pattern.compile(PASSWORD_PATTERN).matcher(password).matches()
    }

}