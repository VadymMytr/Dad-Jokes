package ua.vadymmy.jokeapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.vadymmy.jokeapp.data.models.JokeEntity

@Database(entities = [JokeEntity::class], version = 1)
abstract class JokesDB : RoomDatabase() {
    companion object {
        const val JOKES_DB_NAME = "jokes_db"
    }

    abstract fun createJokesDAO(): JokesDAO
}