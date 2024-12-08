package com.tms.targetedmoneysaver.data.remote.retrofit

import com.tms.targetedmoneysaver.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {

    companion object {
//        fun getApiService(datastore: DataStore<Preferences>): ApiService {
        fun getApiService (): ApiService {

//            val pref = PreferencesRepository.getInstance(datastore)

            val loggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

//            val authInterceptor = Interceptor { chain ->
//                val requestBuilder = chain.request().newBuilder()
//                val authToken = runBlocking {
//                    pref.getUserToken().firstOrNull()?.token
//                }
//                if (!authToken.isNullOrEmpty()) {
//                    requestBuilder.addHeader("Authorization", "Bearer $authToken")
//                }
//                chain.proceed(requestBuilder.build())
//            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)

        }
    }
}