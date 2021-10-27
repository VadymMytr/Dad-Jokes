package ua.vadymmy.jokeapp.utils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ua.vadymmy.jokeapp.data.models.Joke
import ua.vadymmy.jokeapp.databinding.ItemJokeBinding

class JokesAdapter : RecyclerView.Adapter<JokesAdapter.VH>() {

    var jokes = mutableListOf<Joke>()
        set(value) {
            field = value
            notifyItemRangeChanged(0, jokes.lastIndex)
        }

    fun removeAt(position: Int) {
        jokes.removeAt(position)
        notifyItemRemoved(position)
    }

    fun jokeAt(position: Int) = jokes[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemJokeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(jokes[position])

    override fun getItemCount() = jokes.size

    inner class VH(private val binding: ItemJokeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(joke: Joke) =
            with(binding) {
                jokeCategoryTv.text = joke.category
                jokeSetupTv.text = joke.setup
                jokeDeliveryTv.text = joke.delivery
            }

    }

}