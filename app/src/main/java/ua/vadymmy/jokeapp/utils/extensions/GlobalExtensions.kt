package ua.vadymmy.jokeapp.utils.extensions

import android.content.Context
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import ua.vadymmy.jokeapp.App

val app: App get() = App.instance

fun Context.color(id: Int) = ContextCompat.getColor(this, id)
fun Context.drawable(id: Int) = ContextCompat.getDrawable(this, id)

fun MutableLiveData<Boolean>.refresh() {
    value = true
    value = false
}

fun View.updateVisibility(isVisible: Boolean) {
    visibility = if (isVisible) VISIBLE else GONE
}