const express = require("express");

const app = express();

app.use(express.json());

const PORT = process.env.PORT || 3000;

app.post("/suggest", async (req, res) => {
    try {

        const sentence = req.body.sentence;

        if (!sentence || !sentence.trim()) {
            return res.status(400).json({
                error: "Sentence is required"
            });
        }

        const response = await fetch(
            "https://api.groq.com/openai/v1/chat/completions",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json",
                    "Authorization":
                        `Bearer ${process.env.GROQ_API_KEY}`
                },

                body: JSON.stringify({

                    model: "llama-3.1-8b-instant",

                    messages: [
                        {
                            role: "system",
                            content:
                                "You are an English keyboard assistant. Correct grammar and improve the user's English sentence. Return only one natural corrected sentence. Do not explain."
                        },
                        {
                            role: "user",
                            content: sentence
                        }
                    ],

                    temperature: 0.2,
                    max_tokens: 100
                })
            }
        );

        const data =
            await response.json();

        if (!response.ok) {

            console.error(data);

            return res.status(
                response.status
            ).json({
                error: "AI request failed"
            });
        }

        const suggestion =
            data.choices?.[0]?.message?.content
                ?.trim();

        if (!suggestion) {

            return res.status(500).json({
                error:
                    "No AI suggestion received"
            });
        }

        res.json({
            suggestion: suggestion
        });

    } catch (error) {

        console.error(error);

        res.status(500).json({
            error:
                "AI service unavailable"
        });
    }
});

app.get("/", (req, res) => {

    res.json({
        status: "AI English Keyboard backend is running"
    });
});

app.listen(
    PORT,
    () => {
        console.log(
            `Server running on port ${PORT}`
        );
    }
);
