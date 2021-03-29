package com.mazrou.boilerplate.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mazrou.boilerplate.network.network_response.LoginResponse
import com.mazrou.boilerplate.network.network_response.RegistrationResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

const val BASE_URL = "https://somethis.com"

interface WebService {

    @POST("/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): LoginResponse

    @POST("/account/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("password2") password2: String
    ): RegistrationResponse


    companion object {

        fun invoke(): WebService {

            val gson: Gson = GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val loggingHeader = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.HEADERS

            val httpClient = OkHttpClient.Builder()
                .callTimeout(2, java.util.concurrent.TimeUnit.MINUTES)
                .connectTimeout(1000, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(1000, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(1000, java.util.concurrent.TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addInterceptor(loggingHeader)


            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(WebService::class.java)
        }
    }
}