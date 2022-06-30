package com.example.sudoku.model

import com.example.sudoku.Solver

class Board(intBoard: Array<IntArray>) {
    private var board: Array<Array<Field>>

    init {
        val solved = Solver.solveBoard(intBoard)
        board = Array(solved.size) { i ->
            Array(solved[i].size) { j ->
                Field(intBoard[i][j])
            }
        }
        for (row in board.indices)
            for (field in board[row].indices) {
                if (intBoard[row][field] > 0)
                    board[row][field].setCorrectValue()
            }
    }

    fun getFields(): List<List<Field>> {
        return List(board.size) {i ->
            board[i].toList()
        }
    }

    fun move(x: Int, y: Int, value: Int, pencil: Boolean) {
        // TODO
    }

    fun isBoardCompleted(): Boolean {
        for (fields in board)
            for (field in fields)
                if (!field.isCorrect())
                    return false
        return true
    }
}