package com.example.aienglishkeyboard

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.TextView

class KeyboardSettingsActivity : Activity() {

    private val prefsName =
        "keyboard_settings"

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences(
            prefsName,
            MODE_PRIVATE
        )

        val layout = LinearLayout(this)

        layout.orientation =
            LinearLayout.VERTICAL

        layout.setPadding(
            24,
            24,
            24,
            24
        )

        // =================================
        // TITLE
        // =================================

        val title =
            TextView(this)

        title.text =
            "AI English Keyboard Settings"

        title.textSize =
            23f

        title.gravity =
            Gravity.CENTER

        layout.addView(
            title,
            LinearLayout.LayoutParams(
                -1,
                70
            )
        )

        // =================================
        // TYPING SOUND
        // =================================

        val soundSwitch =
            Switch(this)

        soundSwitch.text =
            "Typing Sound"

        soundSwitch.textSize =
            18f

        soundSwitch.isChecked =
            prefs.getBoolean(
                "typing_sound",
                true
            )

        soundSwitch.setOnCheckedChangeListener {
                _,
                checked ->

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

        // =================================
        // AI SENTENCE SUGGESTION
        // =================================

        val aiTitle =
            TextView(this)

        aiTitle.text =
            "AI Sentence Suggestions"

        aiTitle.textSize =
            19f

        aiTitle.setPadding(
            0,
            20,
            0,
            10
        )

        layout.addView(
            aiTitle,
            LinearLayout.LayoutParams(
                -1,
                60
            )
        )

        val aiGroup =
            RadioGroup(this)

        aiGroup.orientation =
            RadioGroup.VERTICAL

        // AUTO

        val auto =
            RadioButton(this)

        auto.text =
            "Auto — Online when available, Offline otherwise"

        auto.textSize =
            16f

        auto.id =
            1001

        aiGroup.addView(auto)

        // ONLINE

        val online =
            RadioButton(this)

        online.text =
            "Online AI"

        online.textSize =
            16f

        online.id =
            1002

        aiGroup.addView(online)

        // OFFLINE

        val offline =
            RadioButton(this)

        offline.text =
            "Offline AI"

        offline.textSize =
            16f

        offline.id =
            1003

        aiGroup.addView(offline)

        // OFF

        val off =
            RadioButton(this)

        off.text =
            "AI Off"

        off.textSize =
            16f

        off.id =
            1004

        aiGroup.addView(off)

        // =================================
        // LOAD CURRENT MODE
        // =================================

        val currentMode =
            prefs.getString(
                "ai_mode",
                "auto"
            )

        when (currentMode) {

            "auto" ->
                auto.isChecked = true

            "online" ->
                online.isChecked = true

            "offline" ->
                offline.isChecked = true

            "off" ->
                off.isChecked = true
        }

        // =================================
        // SAVE MODE
        // =================================

        aiGroup.setOnCheckedChangeListener {
                _,
                checkedId ->

            val mode =
                when (checkedId) {

                    1001 ->
                        "auto"

                    1002 ->
                        "online"

                    1003 ->
                        "offline"

                    1004 ->
                        "off"

                    else ->
                        "auto"
                }

            prefs.edit()
                .putString(
                    "ai_mode",
                    mode
                )
                .apply()
        }

        layout.addView(aiGroup)

        // =================================
        // FONT STYLE
        // =================================

        val fontTitle =
            TextView(this)

        fontTitle.text =
            "Keyboard Font Style"

        fontTitle.textSize =
            19f

        fontTitle.setPadding(
            0,
            20,
            0,
            10
        )

        layout.addView(
            fontTitle,
            LinearLayout.LayoutParams(
                -1,
                60
            )
        )

        // NORMAL

        val normal =
            Button(this)

        normal.text =
            "Normal"

        normal.setOnClickListener {

            saveFont("normal")
        }

        layout.addView(normal)

        // BOLD

        val bold =
            Button(this)

        bold.text =
            "Bold"

        bold.typeface =
            Typeface.DEFAULT_BOLD

        bold.setOnClickListener {

            saveFont("bold")
        }

        layout.addView(bold)

        // SERIF

        val serif =
            Button(this)

        serif.text =
            "Serif"

        serif.typeface =
            Typeface.SERIF

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
