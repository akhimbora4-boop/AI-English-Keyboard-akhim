package com.example.aienglishkeyboard

import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

class KeyboardService : InputMethodService() {

    override fun onCreateInputView(): View {

        val keyboard = LinearLayout(this)
        keyboard.orientation = LinearLayout.VERTICAL
        keyboard.setPadding(4, 4, 4, 4)

        addRow(keyboard, arrayOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"))
        addRow(keyboard, arrayOf("A", "S", "D", "F", "G", "H", "J", "K", "L"))
        addRow(keyboard, arrayOf("Z", "X", "C", "V", "B", "N", "M"))

        return keyboard
    }

    private fun addRow(
        keyboard: LinearLayout,
        keys: Array<String>
    ) {

        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = Gravity.CENTER

        for (key in keys) {

            val button = Button(this)

            button.text = key
            button.textSize = 18f
            button.setTextColor(Color.BLACK)

            button.layoutParams = LinearLayout.LayoutParams(
                0,
                55.dp(),
                1f
            )

            button.setOnClickListener {
                currentInputConnection.commitText(
                    key.lowercase(),
                    1
                )
            }

            row.addView(button)
        }

        keyboard.addView(row)
    }

    private fun Int.dp(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}
