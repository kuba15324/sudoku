package com.example.sudoku.model

class Field(private val correctValue: Int) {
    var value: Int? = null

    fun setCorrectValue() {
        value = correctValue
    }

    fun setValue(newValue: Int): Boolean {
        if (value != null)
            value = newValue
        return isCorrect()
    }

    fun isCorrect(): Boolean {
        return correctValue == value
    }
}