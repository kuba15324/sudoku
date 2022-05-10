package com.jakub.sudoku;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Klasa, ktorej statyczne metody pozwalaja na wygenerowanie planszy sudoku lub jej rozwiazanie.
 * Plansza sudoku jest reprezentowana w klasie jako dwuwymiarowa tablica typu <code>int</code> o rozmiarze 9x9.
 * @author jakub
 */
public abstract class Solver {
	/**
	 * Generuje plansze do sudoku o podanej trudnosci.
	 * @param difficulty trudnosc - im mniejsza liczba tym latwiejsza plansza (od 0 do 35 włącznie)
	 * @return tablica o wymiarach 9x9 zawierajaca cyfry od 0 do 9, gdzie 0 oznacza brak cyfry w danym miejscu, a pozostale to cyfry lamiglowki
	 */
	public static int[][] generateSudokuBoard(int difficulty) {
		if (difficulty < 0 || difficulty > 35)
			return null;
		Random rnd = new Random();
		while (true) {
			int[][] board = generateBoard(rnd);
			int i1, j1, value, iteration = 0;
			long startTime = System.currentTimeMillis();
			while (startTime > System.currentTimeMillis() - 1000) {
				int i = 0;
				do {
					i1 = rnd.nextInt(9);
					j1 = rnd.nextInt(9);
					if (++i > 100)
						break;
				} while (board[i1][j1] == 0);
				value = board[i1][j1];
				board[i1][j1] = 0;
				if (!hasUniqueSolution(board)) {
					board[i1][j1] = value;
					if (iteration < difficulty) {
						iteration++;
						continue;
					}
					return board;
				}
			}
		}
	}
	/**
	 * Rozwiazuje podana plansze. Zwraca pierwsze znalezione rozwiazanie, zatem nie uwzglednia czy lamiglowka posiada inne rozwiazania.
	 * @param board tablica reprezentujaca plansze sudoku do rozwiazania
	 * @return pierwsze spotkane rozwiazanie planszy lub null gdy rozwiazania nie ma
	 */
	public static int[][] solveBoard(int[][] board) {
		int i1 = -1, j1 = -1;
		// wyszukiwanie pierwszego wolnego miejsca
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (board[i][j] <= 0) {
					i1 = i;
					j1 = j;
					break;
				}
			}
			if (i1 >= 0)
				break;
		}
		// cala plansza jest zapelniona
		if (i1 == -1)
			return board;
		// pobranie mozliwych wartosci dla tego pola
		ArrayList<Integer> possibilities = getPossibleValues(board, i1, j1);
		// brak wartosci - koniec rozwiazywania
		if (possibilities.size() == 0)
			return null;
		// sprawdzenie czy kolejne wartosci na tym polu sa poprawne
		for (int value : possibilities) {
			board[i1][j1] = value;
			int[][] result = solveBoard(board);
			if (result != null)
				return result;
			board[i1][j1] = 0;
		}
		return null;
	}
	/**
	 * Pomocnicza metoda generujaca plansze sudoku poprzez dodanie do pustej tablicy trzech losowych cyfr w losowych miejscach oraz rozwiazaniu jej za pomocja metody {@link #solveBoard(int[][]) solveBoard}
	 * @param rnd zmienna losowa uzywana w metodzie
	 * @return wypelniona tablica sudoku
	 */
	protected static int[][] generateBoard(Random rnd) {
		while (true) {
			int[][] board = new int[9][9];
			for (int i = 0; i < 9; i++)
				for (int j = 0; j < 9; j++)
					board[i][j] = 0;
			int i1, j1;
			for (int i = 0; i < 3; i++) {
				do {
					i1 = rnd.nextInt(9);
					j1 = rnd.nextInt(9);
				} while (board[i1][j1] > 0);
				board[i1][j1] = rnd.nextInt(9) + 1;
			}
			board = solveBoard(board);
			if (board != null)
				return board;
		}
	}
	/**
	 * Zmienna pomocnicza inicjowana w metodzie {@link #hasUniqueSolution(int[][]) hasUniqueSolution} oraz inkrementowana w metodzie {@link #solveAndCount(int[][]) solveAndCount}. Okresla ile rozwiazan zostalo znalezionych podczas rozwiazywania planszy.
	 */
	private static int solutionCounter;
	/**
	 * Pozwala sprawdzic czy plansza posiada tylko jedno rozwiazanie
	 * @param board sprawdzana tablica sudoku
	 * @return true jezeli posiada tylko jedno rozwiazanie, false w przeciwnym wypadku
	 */
	private static boolean hasUniqueSolution(int[][] board) {
		solutionCounter = 0;
		solveAndCount(board);
		return solutionCounter == 1;
	}
	/**
	 * Rozwiazuje plansze jednoczesnie inkrementujac zmienna {@link #solutionCounter} za kazdym razem gdy spotka rozwiazanie
	 * @param board
	 * @return zawsze null
	 */
	private static int[][] solveAndCount(int[][] board) {
		int i1 = -1, j1 = -1;
		// wyszukiwanie pierwszego wolnego miejsca
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (board[i][j] <= 0) {
					i1 = i;
					j1 = j;
					break;
				}
			}
			if (i1 >= 0 && j1 >= 0)
				break;
		}
		// ca�a plansza jest zape�niona
		if (i1 == -1 && j1 == -1) {
			solutionCounter++;
			return null;
		}
		// pobranie mo�liwych warto�ci dla tego pola
		ArrayList<Integer> possibilities = getPossibleValues(board, i1, j1);
		// brak mo�liwej warto�ci - brak rozwi�zania
		if (possibilities.size() == 0)
			return null;
		// sprawdzenie czy kolejne warto�ci na tym polu s� poprawne
		for (int value : possibilities) {
			board[i1][j1] = value;
			int[][] result = solveAndCount(board);
			if (result != null)
				return result;
			board[i1][j1] = 0;
		}
		return null;
	}
	/**
	 * Wyznacza kandydatow na miejsce o indeksach i1 oraz j1 na planszy board.
	 * @param board badana plansza
	 * @param i1 indeks wiersza
	 * @param j1 indeks kolumny
	 * @return tablica wartosci, ktore mozna wpisac na rozpatrywane miejsce
	 */
	private static ArrayList<Integer> getPossibleValues(int[][] board, int i1, int j1) {
		ArrayList<Integer> possibilities = new ArrayList<>();
		for (int i = 1; i < 10; i++)
			possibilities.add(i);
		//sprawdzenie wierszy
		for (int i = 0; i < 9; i++) {
			if (board[i1][i] > 0)
				possibilities.remove((Integer)board[i1][i]);
		}
		//sprawdzenie kolumn
		for (int i = 0; i < 9; i++) {
			if (board[i][j1] > 0)
				possibilities.remove((Integer)board[i][j1]);
		}
		//sprawdzenie sekcji
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (3 * (i / 3) + j / 3 == 3 * (i1 / 3) + j1 / 3) {
					if (board[i][j] > 0)
						possibilities.remove((Integer)board[i][j]);
				}
			}
		}
		return possibilities;
	}
}
