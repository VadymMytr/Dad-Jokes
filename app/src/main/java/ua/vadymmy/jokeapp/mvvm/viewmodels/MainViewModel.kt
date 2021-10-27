package ua.vadymmy.jokeapp.mvvm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.vadymmy.jokeapp.data.models.Joke
import ua.vadymmy.jokeapp.data.repos.JokesRepo
import ua.vadymmy.jokeapp.utils.extensions.refresh
import kotlin.random.Random

class MainViewModel(private val jokesRepo: JokesRepo) : ViewModel() {
    private val showLoadingLiveData = MutableLiveData(true)
    private val showNoJokesLiveData = MutableLiveData(false)
    private val showJokesLiveData = MutableLiveData<List<Joke>>()
    private val showJokeDeletedSnackbarLiveData = MutableLiveData(false)
    private val showLoadingFailedSnackbarLiveData = MutableLiveData(false)

    val showLoading: LiveData<Boolean> get() = showLoadingLiveData
    val showNoJokes: LiveData<Boolean> get() = showNoJokesLiveData
    val showJokes: LiveData<List<Joke>> get() = showJokesLiveData
    val showJokeDeletedSnackbar: LiveData<Boolean> get() = showJokeDeletedSnackbarLiveData
    val showLoadingFailedSnackbar: LiveData<Boolean> get() = showLoadingFailedSnackbarLiveData

    private val randomAmountOfJokes: Int
        get() = Random.nextInt(1, 10)

    init {
        checkDatabaseForJokes()
    }

    fun onLoadButtonClick() {
        updateNoJokes(areNoJokes = false)
        updateLoading(isLoading = true)

        jokesRepo.loadJokesFromAPI(
            randomAmountOfJokes,
            onJokesLoaded = ::checkDatabaseForJokes,
            onJokesFailedToLoad = {
                updateLoading(isLoading = false)
                updateNoJokes(areNoJokes = true)
                showLoadingFailedSnackbarLiveData.refresh()
            }
        )
    }

    fun onJokeDeleteSwipe(joke: Joke) {
        jokesRepo.deleteStoredJoke(joke)
        showJokeDeletedSnackbarLiveData.refresh()

        jokesRepo.getStoredJokesAmount { amount ->
            if (amount == 0) {
                hideStoredJokes()
                updateNoJokes(areNoJokes = true)
            }
        }
    }

    private fun checkDatabaseForJokes() {
        jokesRepo.getStoredJokesAmount { amount ->
            if (amount > 0)
                showStoredJokes()
            else {
                updateLoading(isLoading = false)
                updateNoJokes(areNoJokes = true)
            }
        }
    }

    private fun updateLoading(isLoading: Boolean) {
        showLoadingLiveData.value = isLoading
    }

    private fun updateNoJokes(areNoJokes: Boolean) {
        showNoJokesLiveData.value = areNoJokes
    }

    private fun showStoredJokes() =
        jokesRepo.readStoredJokes { storedJokes ->
            showJokesLiveData.apply {
                value = storedJokes
                updateLoading(isLoading = false)
            }
        }

    private fun hideStoredJokes() {
        showJokesLiveData.apply {
            value = null
        }
    }
}