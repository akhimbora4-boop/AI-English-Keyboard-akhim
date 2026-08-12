package com.example.aienglishkeyboard

class SentenceEngine {

    fun suggest(sentence: String): List<String> {

        val text = sentence.trim()

        if (text.isEmpty()) {
            return emptyList()
        }

        val lower = text.lowercase()

        val result = when {

            lower == "i school go" ->
                "I went to school."

            lower == "i go school" ->
                "I go to school."

            lower == "i am go school" ->
                "I am going to school."

            lower == "i going school" ->
                "I am going to school."

            lower == "he are good" ->
                "He is good."

            lower == "she are good" ->
                "She is good."

            lower == "he go school" ->
                "He goes to school."

            lower == "she go school" ->
                "She goes to school."

            lower == "i likes it" ->
                "I like it."

            lower == "he like it" ->
                "He likes it."

            lower == "i has a phone" ->
                "I have a phone."

            lower == "he have a phone" ->
                "He has a phone."

            lower == "they is good" ->
                "They are good."

            lower == "we is ready" ->
                "We are ready."

            lower == "what you are doing" ->
                "What are you doing?"

            lower == "where you are going" ->
                "Where are you going?"

            lower == "how are you" ->
                "How are you?"

            lower == "i want go home" ->
                "I want to go home."

            lower == "i want to go home" ->
                "I want to go home."

            lower == "i am fine" ->
                "I am fine."

            lower == "thank you very much" ->
                "Thank you very much."

            else -> {
                return emptyList()
            }
        }

        return listOf(result)
    }
}
