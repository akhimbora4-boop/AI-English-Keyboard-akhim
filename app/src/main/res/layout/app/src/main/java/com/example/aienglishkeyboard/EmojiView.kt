package com.example.aienglishkeyboard

import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.view.Gravity
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout

class EmojiView(
    private val service: InputMethodService
) : LinearLayout(service) {

    private val emojis = arrayOf(
        "😀", "😃", "😄", "😁", "😆", "😅",
        "😂", "🤣", "😊", "😇", "🙂", "🙃",
        "😉", "😌", "😍", "🥰", "😘", "😎",
        "🤔", "😐", "😑", "😶", "🙄", "😏",
        "😢", "😭", "😡", "🤬", "👍", "👎",
        "👏", "🙏", "❤️", "🔥", "🎉", "⭐",
        "💯", "✨", "💔", "❤️‍🔥", "🥳", "🤩",
        "😴", "🤗", "😋", "😜", "🤭", "🫡"
    )

    init {

        orientation = VERTICAL

        // -------------------------
        // ABC BUTTON
        // -------------------------

        val topRow = LinearLayout(service)

        topRow.orientation = HORIZONTAL
        topRow.gravity = Gravity.CENTER

        val abcButton = Button(service)

        abcButton.text = "ABC"
        abcButton.textSize = 17f

        abcButton.setOnClickListener {

            service.setInputView(
                service.onCreateInputView()
            )
        }

        topRow.addView(
            abcButton,
            LinearLayout.LayoutParams(
                80.dp(),
                55.dp()
            )
        )

        addView(topRow)

        // -------------------------
        // EMOJI GRID
        // -------------------------

        val grid = GridLayout(service)

        grid.columnCount = 6

        grid.setPadding(
            4.dp(),
            4.dp(),
            4.dp(),
            4.dp()
        )

        for (emoji in emojis) {

            val button = Button(service)

            button.text = emoji
            button.textSize = 22f
            button.setTextColor(Color.BLACK)

            val params =
                GridLayout.LayoutParams()

            params.width = 0
            params.height = 60.dp()

            params.columnSpec =
                GridLayout.spec(
                    GridLayout.UNDEFINED,
                    1f
                )

            button.layoutParams = params

            button.setOnClickListener {

                service.currentInputConnection
                    ?.commitText(
                        emoji,
                        1
                    )
            }

            grid.addView(button)
        }

        addView(
            grid,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        )
    }

    private fun Int.dp(): Int {

        return (
            this *
            service.resources.displayMetrics.density
        ).toInt()
    }
}
