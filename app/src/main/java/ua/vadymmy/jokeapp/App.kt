package ua.vadymmy.jokeapp

import android.app.Application
import androidx.room.Room
import ua.vadymmy.jokeapp.data.room.JokesDB

class App : Application() {
    companion object {
        lateinit var instance: App
            private set
    }

    lateinit var database: JokesDB

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(this, JokesDB::class.java, JokesDB.JOKES_DB_NAME).build()
    }
}