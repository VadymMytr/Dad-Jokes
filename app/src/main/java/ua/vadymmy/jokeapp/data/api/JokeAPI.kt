package ua.vadymmy.jokeapp.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ua.vadymmy.jokeapp.data.models.JokeResponse

interface JokeAPI {

    @GET("joke/Any")
    fun getRandomJokes(
        @Query("amount") amount: Int,
        @Query("type") type: String = "twopart"
    ): Call<JokeResponse>

}
