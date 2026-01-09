package com.example.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class TicTacToeActivity : AppCompatActivity() {
    private lateinit var gameBoard: Array<Array<Button>>
    private lateinit var turnIndicator: TextView
    private lateinit var resultMessage: TextView
    private lateinit var resetGameButton: Button

    private var currentPlayer = "X"
    private var moveCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        turnIndicator = findViewById(R.id.turnIndicator)
        resultMessage = findViewById(R.id.resultMessage)
        resetGameButton = findViewById(R.id.resetGameButton)

        updateTurnIndicator()

        // Disable reset button until the game ends
        resetGameButton.isEnabled = false
        resetGameButton.setOnClickListener { resetGame() }

        // Initialize game board buttons
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
        if (button.text.isNotEmpty()) return

        button.text = currentPlayer
        moveCount++

        // Check for winner or tie
        when {
            isWinner() -> {
                resultMessage.text = getString(R.string.winner_message, currentPlayer)
                enableResetButton()
            }
            moveCount == 9 -> {
                resultMessage.text = getString(R.string.tie_message)
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
        turnIndicator.text = getString(R.string.turn_message, currentPlayer)
    }

    private fun resetGame() {
        for (row in gameBoard) {
            for (button in row) {
                button.text = ""
            }
        }
        currentPlayer = "X"
        moveCount = 0
        updateTurnIndicator()
        resultMessage.text = ""
        resetGameButton.isEnabled = false
    }

    private fun enableResetButton() {
        resetGameButton.isEnabled = true
    }
}
