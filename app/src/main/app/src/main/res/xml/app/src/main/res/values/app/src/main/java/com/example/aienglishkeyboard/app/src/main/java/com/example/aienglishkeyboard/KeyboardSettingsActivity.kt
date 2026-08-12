package com.example.aienglishkeyboard

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView

class KeyboardSettingsActivity : Activity() {

    private val prefsName = "keyboard_settings"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences(
            prefsName,
            MODE_PRIVATE
        )

        val layout = LinearLayout(this)

        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(
            24,
            24,
            24,
            24
        )

        // TITLE

        val title = TextView(this)

        title.text = "AI English Keyboard"
        title.textSize = 24f
        title.gravity = Gravity.CENTER

        layout.addView(
            title,
            LinearLayout.LayoutParams(
                -1,
                70
            )
        )

        // TYPING SOUND

        val soundSwitch = Switch(this)

        soundSwitch.text = "Typing Sound"
        soundSwitch.textSize = 18f

        soundSwitch.isChecked =
            prefs.getBoolean(
                "typing_sound",
                true
            )

        soundSwitch.setOnCheckedChangeListener { _, checked ->

            prefs.edit()
                .putBoolean(
                    "typing_sound",
                    checked
                )
                .apply()
        }

        layout.addView(
            soundSwitch,
            LinearLayout.LayoutParams(
                -1,
                60
            )
        )

        // FONT TITLE

        val fontTitle = TextView(this)

        fontTitle.text = "Keyboard Font Style"
        fontTitle.textSize = 18f

        layout.addView(
            fontTitle,
            LinearLayout.LayoutParams(
                -1,
                60
            )
        )

        // NORMAL

        val normal = Button(this)

        normal.text = "Normal"

        normal.setOnClickListener {

            saveFont("normal")
        }

        layout.addView(normal)

        // BOLD

        val bold = Button(this)

        bold.text = "Bold"
        bold.typeface = Typeface.DEFAULT_BOLD

        bold.setOnClickListener {

            saveFont("bold")
        }

        layout.addView(bold)

        // SERIF

        val serif = Button(this)

        serif.text = "Serif"
        serif.typeface = Typeface.SERIF

        serif.setOnClickListener {

            saveFont("serif")
        }

        layout.addView(serif)

        setContentView(layout)
    }

    private fun saveFont(
        font: String
    ) {

        getSharedPreferences(
            prefsName,
            MODE_PRIVATE
        )
            .edit()
            .putString(
                "font_style",
                font
            )
            .apply()
    }
}
