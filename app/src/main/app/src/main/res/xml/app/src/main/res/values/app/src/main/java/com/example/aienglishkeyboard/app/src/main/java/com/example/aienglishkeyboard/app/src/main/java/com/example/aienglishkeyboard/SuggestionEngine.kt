package com.example.aienglishkeyboard

class SuggestionEngine {

    private val corrections = mapOf(
        "teh" to "the",
        "adn" to "and",
        "hte" to "the",
        "recieve" to "receive",
        "seperate" to "separate",
        "definately" to "definitely",
        "becouse" to "because",
        "becuase" to "because",
        "dont" to "don't",
        "cant" to "can't",
        "wont" to "won't",
        "im" to "I'm",
        "ive" to "I've",
        "id" to "I'd",
        "ill" to "I'll",
        "youre" to "you're",
        "theyre" to "they're",
        "thats" to "that's",
        "whats" to "what's",
        "didnt" to "didn't",
        "doesnt" to "doesn't",
        "isnt" to "isn't",
        "wasnt" to "wasn't",
        "werent" to "weren't"
    )

    private val words = listOf(
        "the",
        "there",
        "their",
        "then",
        "this",
        "that",
        "these",
        "those",
        "school",
        "schedule",
        "scholarship",
        "science",
        "student",
        "study",
        "start",
        "story",
        "street",
        "strong",
        "simple",
        "something",
        "someone",
        "sometimes",
        "computer",
        "complete",
        "correct",
        "country",
        "could",
        "would",
        "should",
        "because",
        "before",
        "between",
        "beautiful",
        "better",
        "hello",
        "help",
        "home",
        "house",
        "how",
        "have",
        "happy",
        "good",
        "great",
        "going",
        "give",
        "get",
        "from",
        "friend",
        "family",
        "future",
        "important",
        "information",
        "internet",
        "keyboard",
        "language",
        "learning",
        "mobile",
        "morning",
        "night",
        "please",
        "question",
        "really",
        "right",
        "today",
        "tomorrow",
        "typing",
        "welcome",
        "where",
        "which",
        "who",
        "what",
        "when",
        "with",
        "work",
        "world",
        "your",
        "you're"
    )

    fun autoCorrect(word: String): String {

        val lower = word.lowercase()

        return corrections[lower] ?: word
    }

    fun getSuggestions(prefix: String): List<String> {

        if (prefix.isBlank()) {
            return emptyList()
        }

        val lower = prefix.lowercase()

        return words
            .filter {
                it.startsWith(lower)
            }
            .take(3)
    }
}
