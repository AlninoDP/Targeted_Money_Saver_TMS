package com.tms.targetedmoneysaver.data.remote.response

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token:String,
)
