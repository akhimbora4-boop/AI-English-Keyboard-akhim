package com.example.aienglishkeyboard

import android.graphics.Typeface
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout

class SentenceSuggestionView(
    private val service: KeyboardService
) : LinearLayout(service) {

    private val suggestionButton =
        Button(service)

    init {

        orientation =
            HORIZONTAL

        gravity =
            Gravity.CENTER

        setPadding(
            4.dp(),
            2.dp(),
            4.dp(),
            2.dp()
        )

        suggestionButton.text =
            ""

        suggestionButton.textSize =
            14f

        suggestionButton.typeface =
            Typeface.DEFAULT

        suggestionButton.layoutParams =
            LinearLayout.LayoutParams(
                0,
                48.dp(),
                1f
            )

        suggestionButton.setOnClickListener {

            val text =
                suggestionButton
                    .text
                    .toString()

            if (text.isNotEmpty()) {

                service
                    .useSentenceSuggestion(
                        text
                    )
            }
        }

        addView(
            suggestionButton
        )
    }

    fun showSuggestion(
        suggestion: String
    ) {

        suggestionButton.text =
            suggestion
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
