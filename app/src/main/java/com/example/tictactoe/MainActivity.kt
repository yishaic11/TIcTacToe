package com.example.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var gameBoard: Array<Array<Button>>
    private lateinit var currentPlayerTextView: TextView
    private lateinit var endGameTextView: TextView
    private lateinit var playAgainButton: Button

    private var currentPlayer = "X"
    private var moveCount = 0
    private var gameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentPlayerTextView = findViewById(R.id.current_player_message)
        endGameTextView = findViewById(R.id.end_game_message)
        playAgainButton = findViewById(R.id.play_again_button)

        updateTurnIndicator()

        playAgainButton.isEnabled = false
        playAgainButton.setOnClickListener { resetGame() }

        gameBoard = Array(3) { row ->
            Array(3) { col ->
                val buttonId = resources.getIdentifier("button_$row$col", "id", packageName)
                findViewById<Button>(buttonId).apply {
                    setOnClickListener { handleCellClick(this, row, col) }
                }
            }
        }
    }

    private fun handleCellClick(button: Button, row: Int, col: Int) {
        if (button.text.isNotEmpty() || gameOver) return

        button.text = currentPlayer
        moveCount++

        when {
            isWinner() -> {
                endGameTextView.text = getString(R.string.win_message, currentPlayer)
                gameOver = true
                enableResetButton()
            }

            moveCount == 9 -> {
                endGameTextView.text = getString(R.string.draw_message)
                gameOver = true
                enableResetButton()
            }

            else -> switchPlayer()
        }
    }

    private fun isWinner(): Boolean {
        return (0..2).any { row ->
            gameBoard[row][0].text == currentPlayer &&
                    gameBoard[row][1].text == currentPlayer &&
                    gameBoard[row][2].text == currentPlayer
        } || (0..2).any { col ->
            gameBoard[0][col].text == currentPlayer &&
                    gameBoard[1][col].text == currentPlayer &&
                    gameBoard[2][col].text == currentPlayer
        } || (gameBoard[0][0].text == currentPlayer &&
                gameBoard[1][1].text == currentPlayer &&
                gameBoard[2][2].text == currentPlayer) ||
                (gameBoard[0][2].text == currentPlayer &&
                        gameBoard[1][1].text == currentPlayer &&
                        gameBoard[2][0].text == currentPlayer)
    }

    private fun switchPlayer() {
        currentPlayer = if (currentPlayer == "X") "O" else "X"
        updateTurnIndicator()
    }

    private fun updateTurnIndicator() {
        currentPlayerTextView.text = getString(R.string.current_player_message, currentPlayer)
    }

    private fun resetGame() {
        for (row in gameBoard) {
            for (button in row) {
                button.text = ""
            }
        }
        currentPlayer = "X"
        moveCount = 0
        gameOver = false
        updateTurnIndicator()
        endGameTextView.text = ""
        playAgainButton.isEnabled = false
    }

    private fun enableResetButton() {
        playAgainButton.isEnabled = true
    }
}