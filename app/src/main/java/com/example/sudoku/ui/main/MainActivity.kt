package com.example.sudoku.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sudoku.R
import com.example.sudoku.databinding.ActivityMainBinding
import com.example.sudoku.ui.game.GameActivity

class MainActivity : AppCompatActivity() {
    private lateinit var gameDifficulties: List<String>

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameDifficulties =
            listOf(
                getString(R.string.easy),
                getString(R.string.medium),
                getString(R.string.hard),
                getString(R.string.expert),
                getString(R.string.impossible)
            )
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        mainBinding.buttonsList.adapter = GameButtonRecyclerViewAdapter(gameDifficulties) { difficulty ->
            val gameIntent = Intent(baseContext, GameActivity::class.java)
            gameIntent.putExtra("difficulty", difficulty)
            startActivity(gameIntent)
        }
    }
}