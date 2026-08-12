package com.example.aienglishkeyboard

import android.app.Activity
import android.os.Bundle
import android.graphics.Typeface
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch

class KeyboardSettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(24, 24, 24, 24)

        val title = Button(this)
        title.text = "AI English Keyboard Settings"
        title.textSize = 20f
        title.isEnabled = false

        layout.addView(
            title,
            LinearLayout.LayoutParams(
                -1,
                60
            )
        )

        // Typing sound
        val soundSwitch = Switch(this)
        soundSwitch.text = "Typing Sound"
        soundSwitch.textSize = 17f

        soundSwitch.isChecked =
            getPreferences(MODE_PRIVATE)
                .getBoolean("typing_sound", true)

        soundSwitch.setOnCheckedChangeListener { _, checked ->

            getPreferences(MODE_PRIVATE)
                .edit()
                .putBoolean("typing_sound", checked)
                .apply()
        }

        layout.addView(
            soundSwitch,
            LinearLayout.LayoutParams(
                -1,
                60
            )
        )

        // Font style title
        val fontTitle = Button(this)
        fontTitle.text = "Keyboard Font Style"
        fontTitle.textSize = 17f
        fontTitle.isEnabled = false

        layout.addView(fontTitle)

        // Normal font
        val normal = Button(this)
        normal.text = "Normal"
        normal.setOnClickListener {
            saveFont("normal")
        }

        layout.addView(normal)

        // Bold font
        val bold = Button(this)
        bold.text = "Bold"
        bold.typeface = Typeface.DEFAULT_BOLD

        bold.setOnClickListener {
            saveFont("bold")
        }

        layout.addView(bold)

        // Serif font
        val serif = Button(this)
        serif.text = "Serif"
        serif.typeface = Typeface.SERIF

        serif.setOnClickListener {
            saveFont("serif")
        }

        layout.addView(serif)

        setContentView(layout)
    }

    private fun saveFont(font: String) {

        getPreferences(MODE_PRIVATE)
            .edit()
            .putString("font_style", font)
            .apply()
    }
}
