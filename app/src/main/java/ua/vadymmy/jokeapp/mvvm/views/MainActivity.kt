package ua.vadymmy.jokeapp.mvvm.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ua.vadymmy.jokeapp.R
import ua.vadymmy.jokeapp.data.repos.JokesRepo
import ua.vadymmy.jokeapp.databinding.ActivityMainBinding
import ua.vadymmy.jokeapp.mvvm.factories.JokesFactory
import ua.vadymmy.jokeapp.mvvm.viewmodels.MainViewModel
import ua.vadymmy.jokeapp.utils.adapters.JokesAdapter
import ua.vadymmy.jokeapp.utils.extensions.updateVisibility
import ua.vadymmy.jokeapp.utils.other.JokeItemSwipeCallback
import ua.vadymmy.jokeapp.utils.other.LoadingTextHelper


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel
    private lateinit var jokesAdapter: JokesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        initViews()

        observeViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            JokesFactory(JokesRepo())
        ).get(MainViewModel::class.java)
    }

    private fun initViews() {
        jokesAdapter = JokesAdapter()
        val jokeSwipeCallback = JokeItemSwipeCallback(applicationContext) { swipePosition ->
            with(jokesAdapter) {
                val jokeToRemove = jokeAt(swipePosition)

                removeAt(swipePosition)
                viewModel.onJokeDeleteSwipe(jokeToRemove)
            }
        }

        with(binding) {
            mainLoadButton.setOnClickListener { viewModel.onLoadButtonClick() }

            mainJokesRecycler.apply {
                adapter = jokesAdapter
                layoutManager = LinearLayoutManager(applicationContext)
                ItemTouchHelper(jokeSwipeCallback).attachToRecyclerView(this)
            }
        }
    }

    private fun observeViewModel() {
        val lifecycleOwner = this

        with(viewModel) {
            showLoading.observe(lifecycleOwner) {
                with(binding) {
                    mainLoadingLayout.updateVisibility(it)
                    if (it) LoadingTextHelper(mainLoadingTv).start()
                }
            }

            showNoJokes.observe(lifecycleOwner) {
                binding.mainNoJokesLayout.updateVisibility(it)
            }

            showJokes.observe(lifecycleOwner) {
                Log.i("TAG", "observeViewModel $it")
                jokesAdapter.jokes = it?.toMutableList() ?: mutableListOf()
                binding.mainJokesRecycler.updateVisibility(isVisible = it != null)
            }

            fun showSnackbar(textId: Int) =
                Snackbar.make(binding.root, textId, Snackbar.LENGTH_SHORT).show()

            showLoadingFailedSnackbar.observe(lifecycleOwner) {
                if (it) showSnackbar(R.string.snackbar_loading_failed)
            }

            showJokeDeletedSnackbar.observe(lifecycleOwner) {
                if (it) showSnackbar(R.string.snackbar_item_removed)
            }
        }
    }
}