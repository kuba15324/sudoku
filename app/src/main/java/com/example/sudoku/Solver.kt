package com.example.sudoku

import java.util.*

/**
 * Klasa, ktorej statyczne metody pozwalaja na wygenerowanie planszy sudoku lub jej rozwiazanie.
 * Plansza sudoku jest reprezentowana w klasie jako dwuwymiarowa tablica typu `int` o rozmiarze 9x9.
 * @author jakub
 */
object Solver {
    /**
     * Generuje plansze do sudoku o podanej trudnosci.
     * @param difficulty trudnosc - im mniejsza liczba tym latwiejsza plansza (od 0 do 35 włącznie)
     * @return tablica o wymiarach 9x9 zawierajaca cyfry od 0 do 9, gdzie 0 oznacza brak cyfry w danym miejscu, a pozostale to cyfry lamiglowki
     */
    fun generateSudokuBoard(difficulty: Int): Array<IntArray> {
        if (difficulty < 0 || difficulty > 35)
            throw IllegalArgumentException("Dopuszczalny zakres poziomu trudności: [0, 35]")
        val rnd = Random()
        while (true) {
            val board = generateBoard(rnd)
            var i1: Int
            var j1: Int
            var value: Int
            var iteration = 0
            val startTime = System.currentTimeMillis()
            while (startTime > System.currentTimeMillis() - 1000) {
                var i = 0
                do {
                    i1 = rnd.nextInt(9)
                    j1 = rnd.nextInt(9)
                    if (++i > 100) break
                } while (board[i1][j1] == 0)
                value = board[i1][j1]
                board[i1][j1] = 0
                if (!hasUniqueSolution(board)) {
                    board[i1][j1] = value
                    if (iteration < difficulty) {
                        iteration++
                        continue
                    }
                    return board
                }
            }
        }
    }

    /**
     * Rozwiazuje podana plansze. Zwraca pierwsze znalezione rozwiazanie, zatem nie uwzglednia czy lamiglowka posiada inne rozwiazania.
     * @param board tablica reprezentujaca plansze sudoku do rozwiazania
     * @return pierwsze spotkane rozwiazanie planszy lub null gdy rozwiazania nie ma
     */
    fun solveBoard(board: Array<IntArray>): Array<IntArray> {
        var i1 = -1
        var j1 = -1
        // wyszukiwanie pierwszego wolnego miejsca
        for (i in 0..8) {
            for (j in 0..8) {
                if (board[i][j] <= 0) {
                    i1 = i
                    j1 = j
                    break
                }
            }
            if (i1 >= 0) break
        }
        // cala plansza jest zapelniona
        if (i1 == -1) return board
        // pobranie mozliwych wartosci dla tego pola
        val possibilities = getPossibleValues(board, i1, j1)
        // brak wartosci - koniec rozwiazywania
        if (possibilities.size == 0) return arrayOf()
        // sprawdzenie czy kolejne wartosci na tym polu sa poprawne
        for (value in possibilities) {
            board[i1][j1] = value
            val result = solveBoard(board)
            if (result.isNotEmpty()) return result
            board[i1][j1] = 0
        }
        return arrayOf()
    }

    /**
     * Pomocnicza metoda generujaca plansze sudoku poprzez dodanie do pustej tablicy trzech losowych cyfr w losowych miejscach oraz rozwiazaniu jej za pomocja metody [solveBoard][.solveBoard]
     * @param rnd zmienna losowa uzywana w metodzie
     * @return wypelniona tablica sudoku
     */
    private fun generateBoard(rnd: Random): Array<IntArray> {
        while (true) {
            var board: Array<IntArray> = Array(9) { IntArray(9) }
            for (i in 0..8) for (j in 0..8) board[i][j] = 0
            var i1: Int
            var j1: Int
            for (i in 0..2) {
                do {
                    i1 = rnd.nextInt(9)
                    j1 = rnd.nextInt(9)
                } while (board[i1][j1] > 0)
                board[i1][j1] = rnd.nextInt(9) + 1
            }
            board = solveBoard(board)
            if (board.isNotEmpty()) return board
        }
    }

    /**
     * Zmienna pomocnicza inicjowana w metodzie [hasUniqueSolution][.hasUniqueSolution] oraz inkrementowana w metodzie [solveAndCount][.solveAndCount]. Okresla ile rozwiazan zostalo znalezionych podczas rozwiazywania planszy.
     */
    private var solutionCounter = 0

    /**
     * Pozwala sprawdzic czy plansza posiada tylko jedno rozwiazanie
     * @param board sprawdzana tablica sudoku
     * @return true jezeli posiada tylko jedno rozwiazanie, false w przeciwnym wypadku
     */
    private fun hasUniqueSolution(board: Array<IntArray>): Boolean {
        solutionCounter = 0
        solveAndCount(board)
        return solutionCounter == 1
    }

    /**
     * Rozwiazuje plansze jednoczesnie inkrementujac zmienna [.solutionCounter] za kazdym razem gdy spotka rozwiazanie
     * @param board
     * @return zawsze null
     */
    private fun solveAndCount(board: Array<IntArray>): Array<IntArray>? {
        var i1 = -1
        var j1 = -1
        // wyszukiwanie pierwszego wolnego miejsca
        for (i in 0..8) {
            for (j in 0..8) {
                if (board[i][j] <= 0) {
                    i1 = i
                    j1 = j
                    break
                }
            }
            if (i1 >= 0 && j1 >= 0) break
        }
        // ca�a plansza jest zape�niona
        if (i1 == -1 && j1 == -1) {
            solutionCounter++
            return null
        }
        // pobranie mo�liwych warto�ci dla tego pola
        val possibilities = getPossibleValues(board, i1, j1)
        // brak mo�liwej warto�ci - brak rozwi�zania
        if (possibilities.size == 0) return null
        // sprawdzenie czy kolejne warto�ci na tym polu s� poprawne
        for (value in possibilities) {
            board[i1][j1] = value
            val result = solveAndCount(board)
            if (result != null) return result
            board[i1][j1] = 0
        }
        return null
    }

    /**
     * Wyznacza kandydatow na miejsce o indeksach i1 oraz j1 na planszy board.
     * @param board badana plansza
     * @param i1 indeks wiersza
     * @param j1 indeks kolumny
     * @return tablica wartosci, ktore mozna wpisac na rozpatrywane miejsce
     */
    private fun getPossibleValues(board: Array<IntArray>?, i1: Int, j1: Int): ArrayList<Int> {
        val possibilities = ArrayList<Int>()
        for (i in 1..9) possibilities.add(i)
        //sprawdzenie wierszy
        for (i in 0..8) {
            if (board!![i1][i] > 0) possibilities.remove(board[i1][i])
        }
        //sprawdzenie kolumn
        for (i in 0..8) {
            if (board!![i][j1] > 0) possibilities.remove(board[i][j1])
        }
        //sprawdzenie sekcji
        for (i in 0..8) {
            for (j in 0..8) {
                if (3 * (i / 3) + j / 3 == 3 * (i1 / 3) + j1 / 3) {
                    if (board!![i][j] > 0) possibilities.remove(board[i][j])
                }
            }
        }
        return possibilities
    }
}