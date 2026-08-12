package com.example.aienglishkeyboard

import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

class KeyboardService : InputMethodService() {

    private var isShiftOn = false

    override fun onCreateInputView(): View {

        val keyboard = LinearLayout(this)
        keyboard.orientation = LinearLayout.VERTICAL
        keyboard.setPadding(4, 4, 4, 4)

        addLetterRow(
            keyboard,
            arrayOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P")
        )

        addLetterRow(
            keyboard,
            arrayOf("A", "S", "D", "F", "G", "H", "J", "K", "L")
        )

        addBottomRow(keyboard)

        return keyboard
    }

    private fun addLetterRow(
        keyboard: LinearLayout,
        keys: Array<String>
    ) {

        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = Gravity.CENTER

        for (key in keys) {

            val button = Button(this)

            button.text = if (isShiftOn) key else key.lowercase()
            button.textSize = 18f
            button.setTextColor(Color.BLACK)

            button.layoutParams = LinearLayout.LayoutParams(
                0,
                55.dp(),
                1f
            )

            button.setOnClickListener {

                val text = if (isShiftOn) {
                    key
                } else {
                    key.lowercase()
                }

                currentInputConnection.commitText(text, 1)

                // Shift automatically off after one letter
                if (isShiftOn) {
                    isShiftOn = false
                    refreshKeyboard()
                }
            }

            row.addView(button)
        }

        keyboard.addView(row)
    }

    private fun addBottomRow(
        keyboard: LinearLayout
    ) {

        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = Gravity.CENTER

        // Shift
        val shiftButton = Button(this)
        shiftButton.text = "⇧"
        shiftButton.textSize = 20f

        shiftButton.layoutParams = LinearLayout.LayoutParams(
            0,
            55.dp(),
            1f
        )

        shiftButton.setOnClickListener {
            isShiftOn = !isShiftOn
            refreshKeyboard()
        }

        row.addView(shiftButton)

        // Z X C V B N M
        val letters = arrayOf("Z", "X", "C", "V", "B", "N", "M")

        for (key in letters) {

            val button = Button(this)

            button.text = if (isShiftOn) key else key.lowercase()
            button.textSize = 18f

            button.layoutParams = LinearLayout.LayoutParams(
                0,
                55.dp(),
                1f
            )

            button.setOnClickListener {

                val text = if (isShiftOn) {
                    key
                } else {
                    key.lowercase()
                }

                currentInputConnection.commitText(text, 1)

                if (isShiftOn) {
                    isShiftOn = false
                    refreshKeyboard()
                }
            }

            row.addView(button)
        }

        // Backspace
        val backspace = Button(this)
        backspace.text = "⌫"
        backspace.textSize = 20f

        backspace.layoutParams = LinearLayout.LayoutParams(
            0,
            55.dp(),
            1.4f
        )

        backspace.setOnClickListener {
            currentInputConnection.deleteSurroundingText(1, 0)
        }

        row.addView(backspace)

        keyboard.addView(row)
    }

    private fun refreshKeyboard() {
        setInputView(onCreateInputView())
    }

    private fun Int.dp(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}
