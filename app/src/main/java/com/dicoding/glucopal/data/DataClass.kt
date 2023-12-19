package com.dicoding.glucopal.data

data class RegistrationData(
    val username: String,
    val email: String,
    val password: String,
    val repeatPassword: String
)

data class LoginData(
    val email: String,
    val password: String
)

