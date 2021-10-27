package ua.vadymmy.jokeapp.utils.other

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ua.vadymmy.jokeapp.R
import ua.vadymmy.jokeapp.utils.extensions.color
import ua.vadymmy.jokeapp.utils.extensions.drawable

class JokeItemSwipeCallback(
    private val context: Context,
    private val onItemSwiped: (position: Int) -> Unit
) : ItemTouchHelper.Callback() {

    private val coloredBackground = ColorDrawable()
    private val deleteDrawable = context.drawable(R.drawable.ic_delete)

    private val imageWidth = deleteDrawable?.intrinsicWidth ?: 0
    private val imageHeight = deleteDrawable?.intrinsicHeight ?: 0

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int = makeMovementFlags(0, ItemTouchHelper.LEFT)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight: Int = itemView.height
        val isMoveCancelled = dX == 0f && !isCurrentlyActive

        val itemTop = itemView.top
        val itemBottom = itemView.bottom
        val itemRight = itemView.right

        fun configureBackground() = with(coloredBackground) {
            //set color and bounds
            color = context.color(R.color.red_dark)
            setBounds(itemRight + dX.toInt(), itemTop, itemRight, itemBottom)

            //draw into canvas
            draw(c)
        }

        fun configureDeleteDrawable() = deleteDrawable?.apply {
            //calculate position
            val deleteIconTop = itemTop + (itemHeight - imageHeight) / 2
            val deleteIconMargin = (itemHeight - imageHeight) / 2
            val deleteIconLeft = itemRight - deleteIconMargin - imageWidth
            val deleteIconRight = itemRight - deleteIconMargin
            val deleteIconBottom = deleteIconTop + imageHeight

            setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            draw(c)
        }

        if (isMoveCancelled)
            c.clean(itemRight + dX, itemTop.toFloat(), itemRight.toFloat(), itemBottom.toFloat())
        else {
            configureBackground()
            configureDeleteDrawable()
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) =
        onItemSwiped(viewHolder.adapterPosition)

    //utils
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder) = 0.8f

    private fun Canvas.clean(left: Float, top: Float, right: Float, bottom: Float) = drawRect(
        left, top, right, bottom, Paint()
    )

}