package ua.vadymmy.jokeapp.data.models

data class JokeResponse(
    val error: Boolean,
    val jokes: List<Joke>
)