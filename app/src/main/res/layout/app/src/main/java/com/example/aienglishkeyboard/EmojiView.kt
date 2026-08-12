package com.example.aienglishkeyboard

import android.content.Context
import android.view.Gravity
import android.widget.Button
import android.widget.GridLayout

class EmojiView(context: Context) : GridLayout(context) {

    private val emojis = arrayOf(
        "😀", "😃", "😄", "😁", "😆", "😅",
        "😂", "🤣", "😊", "😇", "🙂", "🙃",
        "😉", "😌", "😍", "🥰", "😘", "😎",
        "🤔", "😐", "😑", "😶", "🙄", "😏",
        "😢", "😭", "😡", "🤬", "👍", "👎",
        "👏", "🙏", "❤️", "🔥", "🎉", "⭐"
    )

    init {
        columnCount = 6
        setPadding(8, 8, 8, 8)

        for (emoji in emojis) {

            val button = Button(context)

            button.text = emoji
            button.textSize = 22f

            button.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = 60.dp()
                columnSpec = GridLayout.spec(
                    GridLayout.UNDEFINED,
                    1f
                )
            }

            button.setOnClickListener {
                (context as? android.inputmethodservice.InputMethodService)
                    ?.currentInputConnection
                    ?.commitText(emoji, 1)
            }

            addView(button)
        }
    }

    private fun Int.dp(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}
