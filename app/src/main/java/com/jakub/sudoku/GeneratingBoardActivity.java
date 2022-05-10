package com.jakub.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GeneratingBoardActivity extends AppCompatActivity {
    private int difficulty = 0;
    private final Thread generateAndRun = new Thread(() -> {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
        int[][] board = Solver.generateSudokuBoard(difficulty);
        if (board != null) {
            startGame(board);
        }
        else {
            ((TextView) findViewById(R.id.loadingTextView)).setText("Wystąpił błąd podczas generowania planszy");
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating_board);
        difficulty = getIntent().getIntExtra("difficulty", 1);
        generateAndRun.start();
    }

    private void startGame(int[][] board) {
        Intent gameIntent = new Intent(this, GameActivity.class);
        for (int i = 0; i < GameActivity.SUDOKU_SIZE; i++) {
            gameIntent.putExtra("board".concat(Integer.toString(i)), board[i]);
        }
        startActivity(gameIntent);
        finish();
    }
}
