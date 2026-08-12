const express = require("express");

const app = express();

app.use(express.json());

const PORT = process.env.PORT || 3000;

app.post("/suggest", async (req, res) => {

    try {

        const sentence = req.body.sentence;

        if (!sentence) {
            return res.status(400).json({
                error: "Sentence is required"
            });
        }

        const response = await fetch(
            "https://api.openai.com/v1/responses",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json",
                    "Authorization":
                        `Bearer ${process.env.OPENAI_API_KEY}`
                },

                body: JSON.stringify({

                    model: "gpt-5-mini",

                    input:
                        `Correct this English sentence.
Return only the corrected sentence.
Do not explain anything.

Sentence:
${sentence}`

                })
            }
        );

        const data =
            await response.json();

        if (!response.ok) {

            return res.status(
                response.status
            ).json(data);
        }

        const result =
            data.output_text || "";

        res.json({
            suggestion: result.trim()
        });

    } catch (error) {

        console.error(error);

        res.status(500).json({
            error:
                "AI service unavailable"
        });
    }
});

app.listen(
    PORT,
    () => {
        console.log(
            `Server running on port ${PORT}`
        );
    }
);
