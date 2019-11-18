package com.example.tictactoe

import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var regularSound: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR

        regularSound = MediaPlayer.create(this, R.raw.regular_sound)
        regularSound.setOnCompletionListener {
            regularSound.release()
        }

        playButton.setOnClickListener{
            val myIntent = Intent(this, MainActivity::class.java)

            myIntent.putExtra("player1Name", player1Name.text.toString())
            myIntent.putExtra("player2Name", player2Name.text.toString())

            startActivity(myIntent)
            regularSound.start()
            finish()
        }
    }
}
