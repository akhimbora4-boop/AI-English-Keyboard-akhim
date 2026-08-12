package com.example.aienglishkeyboard

import android.graphics.Typeface
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout

class SuggestionView(
    private val service: KeyboardService
) : LinearLayout(service) {

    private val buttons =
        ArrayList<Button>()

    init {

        orientation =
            HORIZONTAL

        gravity =
            Gravity.CENTER

        setPadding(
            2.dp(),
            2.dp(),
            2.dp(),
            2.dp()
        )

        repeat(3) {

            val button =
                Button(service)

            button.text =
                ""

            button.textSize =
                14f

            button.typeface =
                Typeface.DEFAULT

            button.layoutParams =
                LinearLayout.LayoutParams(
                    0,
                    45.dp(),
                    1f
                )

            button.setOnClickListener {

                val text =
                    button.text.toString()

                if (text.isNotEmpty()) {

                    service.useSuggestion(
                        text
                    )
                }
            }

            buttons.add(button)

            addView(button)
        }
    }

    fun showSuggestions(
        suggestions: List<String>
    ) {

        for (i in buttons.indices) {

            if (i < suggestions.size) {

                buttons[i].text =
                    suggestions[i]

            } else {

                buttons[i].text =
                    ""
            }
        }
    }

    private fun Int.dp(): Int {

        return (
            this *
                resources
                    .displayMetrics
                    .density
            ).toInt()
    }
}
