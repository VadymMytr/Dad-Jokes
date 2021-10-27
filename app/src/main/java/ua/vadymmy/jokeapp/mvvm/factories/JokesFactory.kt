package ua.vadymmy.jokeapp.mvvm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.vadymmy.jokeapp.data.repos.JokesRepo

class JokesFactory(private val jokesRepo: JokesRepo) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor(JokesRepo::class.java).newInstance(jokesRepo)

}