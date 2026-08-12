package com.example.aienglishkeyboard

import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

class KeyboardService : InputMethodService() {

    private var isShiftOn = false
    private var isNumberMode = false

    override fun onCreateInputView(): View {

        val keyboard = LinearLayout(this)
        keyboard.orientation = LinearLayout.VERTICAL
        keyboard.setPadding(4, 4, 4, 4)

        if (isNumberMode) {
            addNumberRows(keyboard)
        } else {
            addLetterRow(
                keyboard,
                arrayOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P")
            )

            addLetterRow(
                keyboard,
                arrayOf("A", "S", "D", "F", "G", "H", "J", "K", "L")
            )

            addBottomLetterRow(keyboard)
        }

        addControlRow(keyboard)

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
            val button = createButton(
                if (isShiftOn) key else key.lowercase()
            )

            button.setOnClickListener {
                val text = if (isShiftOn) key else key.lowercase()
                currentInputConnection.commitText(text, 1)

                if (isShiftOn) {
                    isShiftOn = false
                    refreshKeyboard()
                }
            }

            row.addView(button)
        }

        keyboard.addView(row)
    }

    private fun addBottomLetterRow(
        keyboard: LinearLayout
    ) {
        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = Gravity.CENTER

        val shift = createButton("⇧")

        shift.setOnClickListener {
            isShiftOn = !isShiftOn
            refreshKeyboard()
        }

        row.addView(shift)

        val letters = arrayOf("Z", "X", "C", "V", "B", "N", "M")

        for (key in letters) {
            val button = createButton(
                if (isShiftOn) key else key.lowercase()
            )

            button.setOnClickListener {
                val text = if (isShiftOn) key else key.lowercase()
                currentInputConnection.commitText(text, 1)

                if (isShiftOn) {
                    isShiftOn = false
                    refreshKeyboard()
                }
            }

            row.addView(button)
        }

        val backspace = createButton("⌫")

        backspace.setOnClickListener {
            currentInputConnection.deleteSurroundingText(1, 0)
        }

        row.addView(backspace)

        keyboard.addView(row)
    }

    private fun addNumberRows(
        keyboard: LinearLayout
    ) {
        addSymbolRow(
            keyboard,
            arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
        )

        addSymbolRow(
            keyboard,
            arrayOf("@", "#", "$", "%", "&", "*", "-", "+", "(", ")")
        )

        addSymbolRow(
            keyboard,
            arrayOf("!", "\"", "'", ":", ";", "?", "/", ".", ",")
        )
    }

    private fun addSymbolRow(
        keyboard: LinearLayout,
        keys: Array<String>
    ) {
        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = Gravity.CENTER

        for (key in keys) {
            val button = createButton(key)

            button.setOnClickListener {
                currentInputConnection.commitText(key, 1)
            }

            row.addView(button)
        }

        keyboard.addView(row)
    }

    private fun addControlRow(
        keyboard: LinearLayout
    ) {
        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = Gravity.CENTER

        // 123 / ABC
        val modeButton = createButton(
            if (isNumberMode) "ABC" else "123"
        )

        modeButton.setOnClickListener {
            isNumberMode = !isNumberMode
            refreshKeyboard()
        }

        row.addView(modeButton)

        // Space
        val space = createButton("Space")

        space.layoutParams = LinearLayout.LayoutParams(
            0,
            55.dp(),
            3f
        )

        space.setOnClickListener {
            currentInputConnection.commitText(" ", 1)
        }

        row.addView(space)

        // Enter
        val enter = createButton("↵")

        enter.setOnClickListener {
            currentInputConnection.commitText("\n", 1)
        }

        row.addView(enter)

        keyboard.addView(row)
    }

    private fun createButton(
        text: String
    ): Button {
        val button = Button(this)

        button.text = text
        button.textSize = 17f
        button.setTextColor(Color.BLACK)

        button.layoutParams = LinearLayout.LayoutParams(
            0,
            55.dp(),
            1f
        )

        return button
    }

    private fun refreshKeyboard() {
        setInputView(onCreateInputView())
    }

    private fun Int.dp(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}
