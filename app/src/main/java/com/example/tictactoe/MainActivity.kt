package com.example.tictactoe

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.media.MediaPlayer
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var player1Turn: Boolean = true
    private var roundCount = 0

    private var buttons: ArrayList<ArrayList<Button>> = ArrayList(3)

    private var player1Score = 0
    private var player2Score = 0

    private var player1Name = ""
    private var player2Name = ""

    private lateinit var winSound: MediaPlayer
    private lateinit var regularSound: MediaPlayer

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR

        player1Name = intent.getStringExtra("player1Name")
        player2Name = intent.getStringExtra("player2Name")

        textViewPlayer1.text = "$player1Name: 0"
        textViewPlayer2.text = "$player2Name: 0"

        winSound = MediaPlayer.create(this, R.raw.win_sound)
//        winSound.setOnCompletionListener {
//            winSound.release()
//        }

        regularSound = MediaPlayer.create(this, R.raw.regular_sound)
//        regularSound.setOnCompletionListener {
//            regularSound.release()
//        }

        resetButton.setOnClickListener {
            restartGame()
        }

        for (i in 0..2) {
            buttons.add(ArrayList())
            for (j in 0..2) {
                val buttonID = "button$i$j"
                val resID = resources.getIdentifier(buttonID, "id", packageName)
                Log.i("BUTTON", "$resID")

                buttons[i].add(findViewById(resID))
            }
        }
    }

    private fun restartGame() {
        Toast.makeText(this, "Game restarted", Toast.LENGTH_SHORT).show()
        resetBoard()
        player1Score = 0
        player2Score = 0
        updateScore()
        player1Turn = true

        regularSound.start()
    }


    fun xoButtonClicked(v: View) {
//        Toast.makeText(this, "xoButtonClicked", Toast.LENGTH_SHORT).show()

        if ((v as Button).text == "") {
            roundCount++
            if (player1Turn) {
                v.text = "X"
            } else {
                v.text = "O"
            }

            if (checkForWin()) {
                winSound.start()
                if (player1Turn) {
                    player1Wins()
                } else {
                    player2Wins()
                }
            } else if (roundCount == 9) {
                regularSound.start()
                draw()
            } else {
                regularSound.start()
                player1Turn = !player1Turn
            }
        }
    }

    fun continueButtonClicked(v: View) {
        resetButton.visibility = View.VISIBLE
        continueButton.visibility = View.INVISIBLE

        resetBoard()
        enableButtons()
        setDefaultButtonColor()

        regularSound.start()
    }

    private fun checkForWin(): Boolean {
        val field: ArrayList<ArrayList<String>> = ArrayList()
        for (i in 0..2) {
            field.add(ArrayList())
            for (j in 0..2) {
                field[i].add(String())
            }
        }

        for (i in 0..2) {
            for (j in 0..2) {
                field[i][j] = buttons[i][j].text.toString()
            }
        }

        for (i in 0..2) {
            if (field[i][0] == field[i][1]
                && field[i][0] == field[i][2]
                && field[i][0] != ""
            ) {
                buttons[i][0].setTextColor(Color.RED)
                buttons[i][1].setTextColor(Color.RED)
                buttons[i][2].setTextColor(Color.RED)
                return true
            }
        }

        for (i in 0..2) {
            if (field[0][i] == field[1][i]
                && field[0][i] == field[2][i]
                && field[0][i] != ""
            ) {
                buttons[0][i].setTextColor(Color.RED)
                buttons[1][i].setTextColor(Color.RED)
                buttons[2][i].setTextColor(Color.RED)
                return true
            }
        }

        if (field[0][0] == field[1][1]
            && field[0][0] == field[2][2]
            && field[0][0] != ""
        ) {
            buttons[0][0].setTextColor(Color.RED)
            buttons[1][1].setTextColor(Color.RED)
            buttons[2][2].setTextColor(Color.RED)
            return true
        }

        if (field[0][2] == field[1][1]
            && field[0][2] == field[2][0]
            && field[0][2] != ""
        ) {
            buttons[0][2].setTextColor(Color.RED)
            buttons[1][1].setTextColor(Color.RED)
            buttons[2][0].setTextColor(Color.RED)
            return true
        }

        return false
    }

    private fun setDefaultButtonColor() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j].setTextColor(Color.WHITE)
            }
        }
    }

    private fun player1Wins() {
        player1Score++
        Toast.makeText(this, "Player1 WINS!", Toast.LENGTH_SHORT).show()
        updateScore()
        resetButton.visibility = View.INVISIBLE
        continueButton.visibility = View.VISIBLE
        disableButtons()
    }

    private fun player2Wins() {
        player2Score++
        Toast.makeText(this, "Player2 WINS!", Toast.LENGTH_SHORT).show()
        updateScore()
        resetButton.visibility = View.INVISIBLE
        continueButton.visibility = View.VISIBLE
        disableButtons()
    }

    private fun draw() {
        Toast.makeText(this, "DRAW!", Toast.LENGTH_SHORT).show()
        resetButton.visibility = View.INVISIBLE
        continueButton.visibility = View.VISIBLE
        disableButtons()
    }

    @SuppressLint("SetTextI18n")
    private fun updateScore() {
        textViewPlayer1.text = "$player1Name: $player1Score"
        textViewPlayer2.text = "$player2Name: $player2Score"
    }

    private fun resetBoard() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j].text = ""
            }
        }
        roundCount = 0
        player1Turn = true
    }

    private fun disableButtons() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j].isClickable = false
            }
        }
    }

    private fun enableButtons() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j].isClickable = true
            }
        }
    }

    //save data
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("roundCount", roundCount)
        outState.putInt("player1Score", player1Score)
        outState.putInt("player2Score", player2Score)
        outState.putBoolean("player1Turn", player1Turn)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        roundCount = savedInstanceState.getInt("roundCount")
        player1Score = savedInstanceState.getInt("player1Score")
        player2Score = savedInstanceState.getInt("player2Score")
        player1Turn = savedInstanceState.getBoolean("player1Turn")
    }
}
