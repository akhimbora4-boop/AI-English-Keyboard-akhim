package com.example.aienglishkeyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

class KeyboardService : InputMethodService() {

    override fun onCreateInputView(): View {

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val button = Button(this)
        button.text = "A"

        button.setOnClickListener {
            currentInputConnection.commitText("a", 1)
        }

        layout.addView(button)

        return layout
    }
}
