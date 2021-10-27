package ua.vadymmy.jokeapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jokes")
data class JokeEntity(
    @PrimaryKey
    var id: Int,
    @ColumnInfo var type: String,
    @ColumnInfo var category: String,
    @ColumnInfo var setup: String,
    @ColumnInfo var delivery: String
) {
    companion object {
        fun from(joke: Joke) = with(joke) {
            JokeEntity(id, type, category, setup, delivery)
        }

        fun JokeEntity.toJoke() = Joke(id, type, category, setup, delivery)
    }
}