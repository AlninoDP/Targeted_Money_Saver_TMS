package com.tms.targetedmoneysaver.data.remote.retrofit

import com.tms.targetedmoneysaver.data.remote.requestbody.PredictCategoryBody
import com.tms.targetedmoneysaver.data.remote.response.PredictResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiPredictService {
    @POST("predict")
    suspend fun predictCategory(@Body predictCategoryBody: PredictCategoryBody): PredictResponse
}