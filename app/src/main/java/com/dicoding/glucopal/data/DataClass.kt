package com.dicoding.glucopal.data

import com.dicoding.glucopal.utils.Gender

data class RegistrationData(
    val username: String,
    val email: String,
    val password: String,
    val repeatPassword: String,
    val gender: Gender
)

data class LoginData(
    val email: String,
    val password: String
)

