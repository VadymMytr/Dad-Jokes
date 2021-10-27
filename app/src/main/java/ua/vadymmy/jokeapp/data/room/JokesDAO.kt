package ua.vadymmy.jokeapp.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ua.vadymmy.jokeapp.data.models.JokeEntity

@Dao
interface JokesDAO {

    @Query("SELECT * FROM jokes")
    fun readAllJokes(): List<JokeEntity>

    @Query("SELECT COUNT(*) FROM jokes")
    fun getJokesAmount(): Int

    @Insert
    @JvmSuppressWildcards
    fun createJokes(jokes: List<JokeEntity>)

    @Query("DELETE FROM jokes WHERE id LIKE :id")
    fun deleteJokeById(id: Int)

}