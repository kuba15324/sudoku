package com.jakub;

import static org.junit.Assert.assertTrue;

import com.jakub.sudoku.Solver;

import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SolverTest {

    @Test
    public void generateEasySudokuBoardsTest() {
        generateSudokuBoards(0, 1000);
    }
    @Test
    public void generateMediumSudokuBoardsTest() {
        generateSudokuBoards(5, 100);
    }
    @Test
    public void generateHardSudokuBoardsTest() {
        generateSudokuBoards(10, 100);
    }
    @Test
    public void generateExpertSudokuBoardsTest() {
        generateSudokuBoards(20, 10);
    }
    @Test
    public void generateImpossibleSudokuBoardsTest() {
        generateSudokuBoards(30, 10);
    }

    private void generateSudokuBoards(int difficulty, int iterations) {
        long t_min = Long.MAX_VALUE, t_max = 0, t_avg = 0, n_min = Long.MAX_VALUE, n_max = 0, n_avg = 0;
        for (int i = 0; i < iterations; i++) {
            Long[] stats = new Long[2];
            try {
                Thread thread = new Thread(() -> {
                    Long[] l = generateSudokuBoardTest(difficulty);
                    stats[0] = l[0];
                    stats[1] = l[1];
                });
                thread.start();
                long t = System.currentTimeMillis();
                while (thread.isAlive()){
                    if (t + 10000 > System.currentTimeMillis()) {
                        throw new InterruptedException();
                    }
                    Thread.sleep(100);
                }
                thread.interrupt();
                if (stats[0] == null) {
                    System.err.println("stats[0]=null, stats[1]=" + stats[1] + " i=" + i);
                    throw new InterruptedException();
                }
                if (stats[0] >= 0) {
                    if (stats[0] > t_max)
                        t_max = stats[0];
                    if (stats[0] < t_min)
                        t_min = stats[0];
                    t_avg += stats[0];
                }
                if (stats[1] >= 0) {
                    if (stats[1] > n_max)
                        n_max = stats[1];
                    if (stats[1] < n_min)
                        n_min = stats[1];
                    n_avg += stats[1];
                }
            } catch (InterruptedException e) {
                stats[0] = -1L;
                stats[1] = -1L;
                i--;
            }
        }
        t_avg /= iterations * 1.0;
        n_avg /= iterations * 1.0;
        System.out.println("Czas:             min=" + t_min + ", max=" + t_max + ", avg=" + t_avg);
        System.out.println("Liczba elementów: min=" + n_min + ", max=" + n_max + ", avg=" + n_avg);
    }

    private Long[] generateSudokuBoardTest(int difficulty) {
        long startTime = System.currentTimeMillis();
        int[][] board = Solver.generateSudokuBoard(difficulty);
        long time = System.currentTimeMillis() - startTime;
        if (board == null)
            System.err.println("Wygenerowano pustą planszę!!!");
        int numberCount = 0;
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (board[i][j] > 0)
                    numberCount++;
        return new Long[]{time, (long) numberCount};
    }
}
