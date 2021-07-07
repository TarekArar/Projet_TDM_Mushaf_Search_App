package com.mazrou.boilerplate.network


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mazrou.boilerplate.model.database.Ayat
import com.mazrou.boilerplate.model.database.RacineModel
import com.mazrou.boilerplate.model.database.Surah
import com.mazrou.boilerplate.model.database.World
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


const val BASE_URL = "https://json-server-api22.herokuapp.com"

interface WebService {

    @GET("/ayah")
    suspend fun getAllAyat(
    ): List<Ayat>

    @GET("/surah ")
    suspend fun getAllSurah(
    ): List<Surah>


    @GET("/mots")
    suspend fun getAllWorlds(
    ): List<World>

    @GET("/racines")
    suspend fun getAllRacine(
    ): List<RacineModel>



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
                .callTimeout(5, java.util.concurrent.TimeUnit.MINUTES)
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