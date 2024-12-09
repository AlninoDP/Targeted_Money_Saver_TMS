package com.tms.targetedmoneysaver.data.remote.retrofit

import com.tms.targetedmoneysaver.data.remote.requestbody.UpdateSavingBody
import com.tms.targetedmoneysaver.data.remote.response.GoalResponse
import com.tms.targetedmoneysaver.data.remote.response.UpdateSavingResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface ApiService {

    @GET("getDreamProduct")
    suspend fun getDreamProduct(): GoalResponse

    @PATCH("updateSavings")
    suspend fun updateSaving(@Body requestBody: UpdateSavingBody): UpdateSavingResponse
}