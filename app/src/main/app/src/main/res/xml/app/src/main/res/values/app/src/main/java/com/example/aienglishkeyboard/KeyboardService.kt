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

    private val suggestionEngine =
        SuggestionEngine()

    private val sentenceEngine =
        SentenceEngine()

    private var currentWord = ""
    private var currentSentence = ""

    private var suggestionView:
        SuggestionView? = null

    private var sentenceSuggestionView:
        SentenceSuggestionView? = null

    private val toneGenerator =
        ToneGenerator(
            AudioManager.STREAM_SYSTEM,
            60
        )

    override fun onCreateInputView(): View {

        val keyboard = LinearLayout(this)

        keyboard.orientation =
            LinearLayout.VERTICAL

        keyboard.setPadding(
            4,
            4,
            4,
            4
        )

        // Sentence suggestion
        val sentenceSuggestions =
            SentenceSuggestionView(this)

        sentenceSuggestionView =
            sentenceSuggestions

        keyboard.addView(
            sentenceSuggestions,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                48.dp()
            )
        )

        // Word suggestions
        val suggestions =
            SuggestionView(this)

        suggestionView =
            suggestions

        keyboard.addView(
            suggestions,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                45.dp()
            )
        )

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

    // =================================
    // LETTER ROW
    // =================================

    private fun addLetterRow(
        keyboard: LinearLayout,
        keys: Array<String>
    ) {

        val row = LinearLayout(this)

        row.orientation =
            LinearLayout.HORIZONTAL

        row.gravity =
            Gravity.CENTER

        for (key in keys) {

            val text =
                if (isShiftOn) {
                    key
                } else {
                    key.lowercase()
                }

            val button =
                createButton(text)

            button.setOnClickListener {

                val output =
                    if (isShiftOn) {
                        key
                    } else {
                        key.lowercase()
                    }

                currentInputConnection
                    ?.commitText(
                        output,
                        1
                    )

                currentWord += output
                currentSentence += output

                updateSuggestions()
                updateSentenceSuggestion()

                if (isShiftOn) {

                    isShiftOn = false

                    refreshKeyboard()
                }
            }

            row.addView(button)
        }

        keyboard.addView(row)
    }

    // =================================
    // BOTTOM LETTER ROW
    // =================================

    private fun addBottomLetterRow(
        keyboard: LinearLayout
    ) {

        val row = LinearLayout(this)

        row.orientation =
            LinearLayout.HORIZONTAL

        row.gravity =
            Gravity.CENTER

        // SHIFT

        val shift =
            createButton("⇧")

        shift.setOnClickListener {

            isShiftOn = !isShiftOn

            refreshKeyboard()
        }

        row.addView(shift)

        // Z X C V B N M

        val letters = arrayOf(
            "Z",
            "X",
            "C",
            "V",
            "B",
            "N",
            "M"
        )

        for (key in letters) {

            val text =
                if (isShiftOn) {
                    key
                } else {
                    key.lowercase()
                }

            val button =
                createButton(text)

            button.setOnClickListener {

                val output =
                    if (isShiftOn) {
                        key
                    } else {
                        key.lowercase()
                    }

                currentInputConnection
                    ?.commitText(
                        output,
                        1
                    )

                currentWord += output
                currentSentence += output

                updateSuggestions()
                updateSentenceSuggestion()

                if (isShiftOn) {

                    isShiftOn = false

                    refreshKeyboard()
                }
            }

            row.addView(button)
        }

        // BACKSPACE

        val backspace =
            createButton("⌫")

        backspace.setOnClickListener {

            currentInputConnection
                ?.deleteSurroundingText(
                    1,
                    0
                )

            if (currentWord.isNotEmpty()) {

                currentWord =
                    currentWord.dropLast(1)
            }

            if (currentSentence.isNotEmpty()) {

                currentSentence =
                    currentSentence.dropLast(1)
            }

            updateSuggestions()
            updateSentenceSuggestion()
        }

        row.addView(backspace)

        keyboard.addView(row)
    }

    // =================================
    // NUMBERS
    // =================================

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

        row.orientation =
            LinearLayout.HORIZONTAL

        row.gravity =
            Gravity.CENTER

        for (key in keys) {

            val button =
                createButton(key)

            button.setOnClickListener {

                currentInputConnection
                    ?.commitText(
                        key,
                        1
                    )

                currentWord = ""

                updateSuggestions()
            }

            row.addView(button)
        }

        keyboard.addView(row)
    }

    // =================================
    // CONTROL ROW
    // =================================

    private fun addControlRow(
        keyboard: LinearLayout
    ) {

        val row = LinearLayout(this)

        row.orientation =
            LinearLayout.HORIZONTAL

        row.gravity =
            Gravity.CENTER

        // EMOJI

        val emoji =
            createButton("😀")

        emoji.setOnClickListener {

            setInputView(
                EmojiView(this) {
                    refreshKeyboard()
                }
            )
        }

        row.addView(emoji)

        // SETTINGS

        val settings =
            createButton("⚙")

        settings.setOnClickListener {

            val intent =
                Intent(
                    this,
                    KeyboardSettingsActivity::class.java
                )

            startActivity(intent)
        }

        row.addView(settings)

        // 123 / ABC

        val modeButton =
            createButton(
                if (isNumberMode) {
                    "ABC"
                } else {
                    "123"
                }
            )

        modeButton.setOnClickListener {

            isNumberMode =
                !isNumberMode

            refreshKeyboard()
        }

        row.addView(modeButton)

        // SPACE

        val space =
            createButton("Space")

        space.layoutParams =
            LinearLayout.LayoutParams(
                0,
                55.dp(),
                3f
            )

        space.setOnClickListener {

            commitCurrentWord()

            currentInputConnection
                ?.commitText(
                    " ",
                    1
                )

            currentWord = ""

            if (currentSentence.isNotEmpty()) {
                currentSentence += " "
            }

            updateSuggestions()
            updateSentenceSuggestion()
        }

        row.addView(space)

        // ENTER

        val enter =
            createButton("↵")

        enter.setOnClickListener {

            commitCurrentWord()

            currentInputConnection
                ?.commitText(
                    "\n",
                    1
                )

            currentWord = ""
            currentSentence = ""

            updateSuggestions()
            updateSentenceSuggestion()
        }

        row.addView(enter)

        keyboard.addView(row)
    }

    // =================================
    // CREATE BUTTON
    // =================================

    private fun createButton(
        text: String
    ): Button {

        val button =
            Button(this)

        button.text =
            text

        button.textSize =
            17f

        button.setTextColor(
            Color.BLACK
        )

        button.layoutParams =
            LinearLayout.LayoutParams(
                0,
                55.dp(),
                1f
            )

        val fontStyle =
            getSharedPreferences(
                "keyboard_settings",
                MODE_PRIVATE
            ).getString(
                "font_style",
                "normal"
            )

        button.typeface =
            when (fontStyle) {

                "bold" ->
                    Typeface.DEFAULT_BOLD

                "serif" ->
                    Typeface.SERIF

                else ->
                    Typeface.DEFAULT
            }

        button.setOnTouchListener { _, event ->

            if (
                event.action ==
                MotionEvent.ACTION_DOWN &&
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

    // =================================
    // AUTO CORRECT
    // =================================

    private fun commitCurrentWord() {

        if (currentWord.isEmpty()) {
            return
        }

        val corrected =
            suggestionEngine.autoCorrect(
                currentWord
            )

        if (corrected != currentWord) {

            currentInputConnection
                ?.deleteSurroundingText(
                    currentWord.length,
                    0
                )

            currentInputConnection
                ?.commitText(
                    corrected,
                    1
                )

            if (currentSentence.length >=
                currentWord.length
            ) {

                currentSentence =
                    currentSentence.dropLast(
                        currentWord.length
                    )

                currentSentence += corrected
            }
        }
    }

    // =================================
    // WORD SUGGESTIONS
    // =================================

    private fun updateSuggestions() {

        val suggestions =
            suggestionEngine
                .getSuggestions(
                    currentWord
                )

        suggestionView
            ?.showSuggestions(
                suggestions
            )
    }

    // =================================
    // SENTENCE SUGGESTION
    // =================================

    private fun updateSentenceSuggestion() {

        val suggestions =
            sentenceEngine.suggest(
                currentSentence
            )

        if (suggestions.isNotEmpty()) {

            sentenceSuggestionView
                ?.showSuggestion(
                    suggestions[0]
                )

        } else {

            sentenceSuggestionView
                ?.showSuggestion("")
        }
    }

    // =================================
    // USE WORD SUGGESTION
    // =================================

    fun useSuggestion(
        suggestion: String
    ) {

        if (currentWord.isEmpty()) {
            return
        }

        currentInputConnection
            ?.deleteSurroundingText(
                currentWord.length,
                0
            )

        currentInputConnection
            ?.commitText(
                suggestion + " ",
                1
            )

        if (currentSentence.length >=
            currentWord.length
        ) {

            currentSentence =
                currentSentence.dropLast(
                    currentWord.length
                )

            currentSentence +=
                suggestion + " "
        }

        currentWord = ""

        updateSuggestions()
        updateSentenceSuggestion()
    }

    // =================================
    // USE SENTENCE SUGGESTION
    // =================================

    fun useSentenceSuggestion(
        suggestion: String
    ) {

        if (currentSentence.isEmpty()) {
            return
        }

        currentInputConnection
            ?.deleteSurroundingText(
                currentSentence.length,
                0
            )

        currentInputConnection
            ?.commitText(
                suggestion + " ",
                1
            )

        currentSentence = ""
        currentWord = ""

        updateSuggestions()
        updateSentenceSuggestion()
    }

    // =================================
    // SOUND
    // =================================

    private fun isTypingSoundEnabled():
        Boolean {

        return getSharedPreferences(
            "keyboard_settings",
            MODE_PRIVATE
        ).getBoolean(
            "typing_sound",
            true
        )
    }

    // =================================
    // REFRESH
    // =================================

    private fun refreshKeyboard() {

        setInputView(
            onCreateInputView()
        )
    }

    // =================================
    // DP
    // =================================

    private fun Int.dp(): Int {

        return (
            this *
                resources
                    .displayMetrics
                    .density
            ).toInt()
    }

    // =================================
    // DESTROY
    // =================================

    override fun onDestroy() {

        toneGenerator.release()

        super.onDestroy()
    }
}
