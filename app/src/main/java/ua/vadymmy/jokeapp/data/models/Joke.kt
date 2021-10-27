package ua.vadymmy.jokeapp.data.models

data class Joke(
    val id: Int,
    val type: String,
    val category: String,
    val setup: String,
    val delivery: String
)
