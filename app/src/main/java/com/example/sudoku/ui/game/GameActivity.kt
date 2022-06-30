package com.example.sudoku.ui.game

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.sudoku.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    private lateinit var gameBinding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameBinding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(gameBinding.root)
        val gameViewModel: GameViewModel by viewModels()
        val difficulty = intent.extras?.get("difficulty") as Int
        gameViewModel.generateBoard(difficulty)
        gameViewModel.getGameBoard().observe(this) {
            gameBinding.sudokuField.adapter = FieldsRowRecyclerViewAdapter(it.getFields())
        }
    }
}