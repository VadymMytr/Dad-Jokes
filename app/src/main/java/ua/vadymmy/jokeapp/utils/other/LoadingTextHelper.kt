package ua.vadymmy.jokeapp.utils.other

import android.widget.TextView
import kotlinx.coroutines.*
import ua.vadymmy.jokeapp.R

class LoadingTextHelper(private val loadingTextView: TextView) {
    private val context = loadingTextView.context
    private val loadingText = context.getString(R.string.loading)

    companion object {
        private const val DOT_SYMBOL = '.'
        private const val MAX_DOTS_AMOUNT = 3
        private const val DOTS_UPDATING_DELAY_MILLIS = 750L
    }

    fun start() {
        loadingTextView.text = loadingText
        startLoading()
    }

    private fun startLoading() =
        CoroutineScope(Dispatchers.IO).launch {
            val currentText = getText()
            while (currentText.count { it == DOT_SYMBOL } <= MAX_DOTS_AMOUNT) {
                setText("$currentText$DOT_SYMBOL")
                delay(DOTS_UPDATING_DELAY_MILLIS)
            }
        }

    private suspend fun getText() = withContext(Dispatchers.Main) {
        loadingTextView.text
    }

    private suspend fun setText(newText: String) = withContext(Dispatchers.Main) {
        loadingTextView.text = newText
    }
}