package ua.vadymmy.jokeapp.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JokeAPIClient {
    companion object {
        private const val JOKES_BASE_URL = "https://v2.jokeapi.dev/"
    }

    private val okHttpClient: OkHttpClient
        get() = OkHttpClient.Builder().build()

    val retrofit: Retrofit
        get() = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(JOKES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}