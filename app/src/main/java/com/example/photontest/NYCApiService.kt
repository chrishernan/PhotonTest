package com.example.photontest

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


interface NYCApiService {
    @GET("/resource/s3k6-pzi2.json")
    suspend fun fetch(): List<NYCHighSchool>

    companion object {
        private val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        private var retrofit = Retrofit.Builder()
            .baseUrl("https://data.cityofnewyork.us")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        var service: NYCApiService = retrofit.create(NYCApiService::class.java)
    }
}