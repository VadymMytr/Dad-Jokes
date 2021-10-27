package ua.vadymmy.jokeapp.data.repos

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.vadymmy.jokeapp.App
import ua.vadymmy.jokeapp.data.api.JokeAPI
import ua.vadymmy.jokeapp.data.api.JokeAPIClient
import ua.vadymmy.jokeapp.data.models.Joke
import ua.vadymmy.jokeapp.data.models.JokeEntity
import ua.vadymmy.jokeapp.data.models.JokeEntity.Companion.toJoke
import ua.vadymmy.jokeapp.data.models.JokeResponse

class JokesRepo {

    private val jokesDAO get() = App.instance.database.createJokesDAO()
    private val jokeAPI get() = JokeAPIClient().retrofit.create(JokeAPI::class.java)

    fun loadJokesFromAPI(
        amount: Int,
        onJokesLoaded: () -> Unit,
        onJokesFailedToLoad: () -> Unit
    ) =
        jokeAPI.getRandomJokes(amount).enqueue(object : Callback<JokeResponse> {
            override fun onResponse(
                call: Call<JokeResponse>,
                response: Response<JokeResponse>
            ) {
                val body = response.body()
                Log.i("TAG", "onResponse: $body")
                if (body == null || body.jokes.isNullOrEmpty() || !response.isSuccessful) {
                    onJokesFailedToLoad()
                    return
                }

                GlobalScope.launch {
                    saveJokes(body.jokes).join()
                    withContext(Dispatchers.Main) { onJokesLoaded() }
                }
            }

            override fun onFailure(call: Call<JokeResponse>, t: Throwable) {
                t.printStackTrace()
                onJokesFailedToLoad()
            }
        })

    fun getStoredJokesAmount(jokesAmountReceived: (jokesAmount: Int) -> Unit) =
        GlobalScope.launch {
            val amount = jokesDAO.getJokesAmount()
            withContext(Dispatchers.Main) { jokesAmountReceived(amount) }
        }

    private fun saveJokes(jokes: List<Joke>) = GlobalScope.launch {
        jokesDAO.createJokes(jokes.map { JokeEntity.from(it) })
    }

    fun readStoredJokes(onJokesRead: (jokes: List<Joke>) -> Unit) = GlobalScope.launch {
        val allJokes = jokesDAO.readAllJokes()

        withContext(Dispatchers.Main) {
            onJokesRead(allJokes.map { it.toJoke() })
        }
    }

    fun deleteStoredJoke(joke: Joke) = GlobalScope.launch {
        jokesDAO.deleteJokeById(joke.id)
    }

}