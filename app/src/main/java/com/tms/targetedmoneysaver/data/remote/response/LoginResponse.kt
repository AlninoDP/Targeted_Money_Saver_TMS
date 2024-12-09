package com.tms.targetedmoneysaver.data.remote.response

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val token:String,
)
