package com.example.aienglishkeyboard

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.inputmethodservice.InputMethodService
import android.media.AudioManager
import android.media.ToneGenerator
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

class KeyboardService : InputMethodService() {

    private var isShiftOn = false
    private var isNumberMode = false

    private val toneGenerator = ToneGenerator(
        AudioManager.STREAM_SYSTEM,
        60
    )

    override fun onCreateInputView(): View {

        val keyboard = LinearLayout(this)

        keyboard.orientation = LinearLayout.VERTICAL
        keyboard.setPadding(4, 4, 4, 4)

        if (isNumberMode) {

            addNumberRows(keyboard)

        } else {

            addLetterRow(
                keyboard,
                arrayOf(
                    "Q", "W", "E", "R", "T",
                    "Y", "U", "I", "O", "P"
                )
            )

            addLetterRow(
                keyboard,
                arrayOf(
                    "A", "S", "D", "F", "G",
                    "H", "J", "K", "L"
                )
            )

            addBottomLetterRow(keyboard)
        }

        addControlRow(keyboard)

        return keyboard
    }

    // -------------------------
    // LETTER ROW
    // -------------------------

    private fun addLetterRow(
        keyboard: LinearLayout,
        keys: Array<String>
    ) {

        val row = LinearLayout(this)

        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = Gravity.CENTER

        for (key in keys) {

            val text = if (isShiftOn) {
                key
            } else {
                key.lowercase()
            }

            val button = createButton(text)

            button.setOnClickListener {

                val output = if (isShiftOn) {
                    key
                } else {
                    key.lowercase()
                }

                currentInputConnection.commitText(
                    output,
                    1
                )

                if (isShiftOn) {
                    isShiftOn = false
                    refreshKeyboard()
                }
            }

            row.addView(button)
        }

        keyboard.addView(row)
    }

    // -------------------------
    // BOTTOM LETTER ROW
    // -------------------------

    private fun addBottomLetterRow(
        keyboard: LinearLayout
    ) {

        val row = LinearLayout(this)

        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = Gravity.CENTER

        // SHIFT

        val shift = createButton("⇧")

        shift.setOnClickListener {

            isShiftOn = !isShiftOn

            refreshKeyboard()
        }

        row.addView(shift)

        // Z X C V B N M

        val letters = arrayOf(
            "Z", "X", "C", "V",
            "B", "N", "M"
        )

        for (key in letters) {

            val text = if (isShiftOn) {
                key
            } else {
                key.lowercase()
            }

            val button = createButton(text)

            button.setOnClickListener {

                val output = if (isShiftOn) {
                    key
                } else {
                    key.lowercase()
                }

                currentInputConnection.commitText(
                    output,
                    1
                )

                if (isShiftOn) {
                    isShiftOn = false
                    refreshKeyboard()
                }
            }

            row.addView(button)
        }

        // BACKSPACE

        val backspace = createButton("⌫")

        backspace.setOnClickListener {

            currentInputConnection.deleteSurroundingText(
                1,
                0
            )
        }

        row.addView(backspace)

        keyboard.addView(row)
    }

    // -------------------------
    // NUMBERS & SYMBOLS
    // -------------------------

    private fun addNumberRows(
        keyboard: LinearLayout
    ) {

        addSymbolRow(
            keyboard,
            arrayOf(
                "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "0"
            )
        )

        addSymbolRow(
            keyboard,
            arrayOf(
                "@", "#", "$", "%", "&",
                "*", "-", "+", "(", ")"
            )
        )

        addSymbolRow(
            keyboard,
            arrayOf(
                "!", "\"", "'", ":",
                ";", "?", "/", ".", ","
            )
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

                currentInputConnection.commitText(
                    key,
                    1
                )
            }

            row.addView(button)
        }

        keyboard.addView(row)
    }

    // -------------------------
    // CONTROL ROW
    // -------------------------

    private fun addControlRow(
        keyboard: LinearLayout
    ) {

        val row = LinearLayout(this)

        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = Gravity.CENTER

        // EMOJI

        val emoji = createButton("😀")

        emoji.setOnClickListener {

            setInputView(
                EmojiView(this) {
                    refreshKeyboard()
                }
            )
        }

        row.addView(emoji)

        // SETTINGS

        val settings = createButton("⚙")

        settings.setOnClickListener {

            val intent = Intent(
                this,
                KeyboardSettingsActivity::class.java
            )

            startActivity(intent)
        }

        row.addView(settings)

        // 123 / ABC

        val modeButton = createButton(
            if (isNumberMode) {
                "ABC"
            } else {
                "123"
            }
        )

        modeButton.setOnClickListener {

            isNumberMode = !isNumberMode

            refreshKeyboard()
        }

        row.addView(modeButton)

        // SPACE

        val space = createButton("Space")

        space.layoutParams = LinearLayout.LayoutParams(
            0,
            55.dp(),
            3f
        )

        space.setOnClickListener {

            currentInputConnection.commitText(
                " ",
                1
            )
        }

        row.addView(space)

        // ENTER

        val enter = createButton("↵")

        enter.setOnClickListener {

            currentInputConnection.commitText(
                "\n",
                1
            )
        }

        row.addView(enter)

        keyboard.addView(row)
    }

    // -------------------------
    // CREATE BUTTON
    // -------------------------

    private fun createButton(
        text: String
    ): Button {

        val button = Button(this)

        button.text = text
        button.textSize = 17f
        button.setTextColor(Color.BLACK)

        button.layoutParams =
            LinearLayout.LayoutParams(
                0,
                55.dp(),
                1f
            )

        // Font style

        val fontStyle = getSharedPreferences(
            "keyboard_settings",
            MODE_PRIVATE
        ).getString(
            "font_style",
            "normal"
        )

        button.typeface = when (fontStyle) {

            "bold" -> Typeface.DEFAULT_BOLD

            "serif" -> Typeface.SERIF

            else -> Typeface.DEFAULT
        }

        // Typing sound

        button.setOnTouchListener { _, event ->

            if (
                event.action == MotionEvent.ACTION_DOWN &&
                isTypingSoundEnabled()
            ) {

                toneGenerator.startTone(
                    ToneGenerator.TONE_PROP_BEEP,
                    50
                )
            }

            false
        }

        return button
    }

    // -------------------------
    // SOUND SETTING
    // -------------------------

    private fun isTypingSoundEnabled(): Boolean {

        return getSharedPreferences(
            "keyboard_settings",
            MODE_PRIVATE
        ).getBoolean(
            "typing_sound",
            true
        )
    }

    // -------------------------
    // REFRESH
    // -------------------------

    private fun refreshKeyboard() {

        setInputView(
            onCreateInputView()
        )
    }

    // -------------------------
    // DP
    // -------------------------

    private fun Int.dp(): Int {

        return (
            this *
            resources.displayMetrics.density
        ).toInt()
    }

    // -------------------------
    // CLEAN UP
    // -------------------------

    override fun onDestroy() {

        toneGenerator.release()

        super.onDestroy()
    }
}
