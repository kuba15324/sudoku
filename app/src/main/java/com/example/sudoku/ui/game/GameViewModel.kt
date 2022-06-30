package com.example.sudoku.ui.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sudoku.Solver
import com.example.sudoku.model.Board
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val gameBoard: MutableLiveData<Board> by lazy {
        MutableLiveData<Board>()
    }

    fun getGameBoard(): LiveData<Board> {
        return gameBoard
    }

    fun generateBoard(difficulty: Int) {
        viewModelScope.launch {
            val board = Solver.generateSudokuBoard(difficulty)
            gameBoard.postValue(Board(board))
        }
    }

    fun setNumber(x: Int, y: Int, value: Int, pencil: Boolean) {
        gameBoard.value?.move(x, y, value, pencil)
    }
}