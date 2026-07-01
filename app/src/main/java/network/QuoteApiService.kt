package com.example.taskmanager.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// This data class maps directly to the JSON fields the API returns
data class QuoteResponse(
    val q: String,  // quote text
    val a: String   // author
)

interface QuoteApiService {
    @GET("today")
    suspend fun getQuoteOfTheDay(): List<QuoteResponse>
}

object QuoteApi {
    val service: QuoteApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://zenquotes.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuoteApiService::class.java)
    }
}