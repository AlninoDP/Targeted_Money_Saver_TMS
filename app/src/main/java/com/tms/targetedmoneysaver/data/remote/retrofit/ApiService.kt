package com.tms.targetedmoneysaver.data.remote.retrofit

import com.tms.targetedmoneysaver.data.remote.response.Response
import retrofit2.http.GET

interface ApiService {

    @GET("getDreamProduct")
    suspend fun getDreamProduct(): Response
}