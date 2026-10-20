package com.creator_surge.engines

import java.io.File
import kotlinx.serialization.Serializable

@Serializable
data class GameArtefact(
    val title: String,
    val url: String,
    val filePath: String,
    val description: String
)

class GameEngine {
    fun generate(prompt: String, outputDir: File, baseUrl: String): List<GameArtefact> {
        val gamesDir = File(outputDir, "games").apply { mkdirs() }
        val fileName = "game_${System.currentTimeMillis()}.html"
        val outputFile = File(gamesDir, fileName)

        val htmlContent = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Creator Surge Web Game</title>
                <style>
                    body {
                        margin: 0;
                        background: #181825;
                        color: #cdd6f4;
                        font-family: system-ui, sans-serif;
                        display: flex;
                        flex-direction: column;
                        align-items: center;
                        justify-content: center;
                        height: 100vh;
                        overflow: hidden;
                    }
                    h2 { margin: 10px 0; color: #89b4fa; text-align: center; }
                    p { color: #a6adc8; margin-bottom: 20px; font-size: 14px; }
                    canvas {
                        background: #11111b;
                        border: 2px solid #89b4fa;
                        border-radius: 12px;
                        box-shadow: 0 8px 24px rgba(0,0,0,0.5);
                    }
                    .score { font-size: 18px; font-weight: bold; margin-top: 10px; color: #a6e3a1; }
                    button {
                        margin-top: 15px;
                        padding: 10px 20px;
                        background: #89b4fa;
                        color: #11111b;
                        border: none;
                        border-radius: 8px;
                        font-weight: bold;
                        cursor: pointer;
                    }
                </style>
            </head>
            <body>
                <h2>🎮 Interactive Game</h2>
                <p>Prompt: ${prompt.replace("<", "&lt;").replace(">", "&gt;")}</p>
                <canvas id="gameCanvas" width="320" height="320"></canvas>
                <div class="score">Score: <span id="scoreVal">0</span></div>
                <button onclick="resetGame()">Restart Game</button>

                <script>
                    const canvas = document.getElementById('gameCanvas');
                    const ctx = canvas.getContext('2d');
                    let score = 0;
                    let target = { x: 140, y: 140, size: 40 };

                    function draw() {
                        ctx.clearRect(0, 0, canvas.width, canvas.height);
                        ctx.fillStyle = '#f5c2e7';
                        ctx.beginPath();
                        ctx.arc(target.x + target.size/2, target.y + target.size/2, target.size/2, 0, Math.PI * 2);
                        ctx.fill();
                    }

                    canvas.addEventListener('click', function(e) {
                        const rect = canvas.getBoundingClientRect();
                        const clickX = e.clientX - rect.left;
                        const clickY = e.clientY - rect.top;

                        const dist = Math.hypot(clickX - (target.x + target.size/2), clickY - (target.y + target.size/2));
                        if (dist <= target.size/2) {
                            score += 10;
                            document.getElementById('scoreVal').innerText = score;
                            target.x = Math.random() * (canvas.width - target.size);
                            target.y = Math.random() * (canvas.height - target.size);
                            draw();
                        }
                    });

                    function resetGame() {
                        score = 0;
                        document.getElementById('scoreVal').innerText = score;
                        target.x = 140;
                        target.y = 140;
                        draw();
                    }

                    draw();
                </script>
            </body>
            </html>
        """.trimIndent()

        outputFile.writeText(htmlContent)

        val relativeUrl = "$baseUrl/outputs/games/$fileName"
        return listOf(
            GameArtefact(
                title = "Interactive Canvas Game",
                url = relativeUrl,
                filePath = outputFile.absolutePath,
                description = "Playable interactive mini-game generated based on prompt: '$prompt'"
            )
        )
    }
}
