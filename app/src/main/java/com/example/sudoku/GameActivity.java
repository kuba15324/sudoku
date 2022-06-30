/*
package com.jakub.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import com.example.sudoku.Solver;

public class GameActivity extends AppCompatActivity {
    public static final int SUDOKU_SIZE = 9;

    private int difficulty;
    private int screenWidth;
    private int selectedNumber = 0;
    private int mistakesCounter;
    private boolean pencilSelected = false;

    private SudokuField[][] sudokuFields;
    private NumberButton[] numberButtons;
    private int[] counters;
    private TextView[] counterTextViews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        difficulty = getIntent().getIntExtra("difficulty", 1);
        int[][] board = new int[SUDOKU_SIZE][];
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            board[i] = getIntent().getIntArrayExtra("board".concat(Integer.toString(i)));
        }
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        mistakesCounter = 0;
        setFields(board);
        setNumbers();
        ((Chronometer)findViewById(R.id.timerTextView)).start();
    }

    private boolean isBoardCompleted() {
        for (SudokuField[] fields : sudokuFields)
            for (SudokuField field : fields)
                if (!field.isCorrect())
                    return false;
        return true;
    }

    private void sudokuButtonsOnClick(View v) {
        if (v instanceof SudokuField) {
            if (pencilSelected)
                ((SudokuField)v).addCandidate(selectedNumber);
            else
                ((SudokuField)v).inputNumber(selectedNumber, this);
        }
        if (isBoardCompleted()) {
            Intent completedIntent = new Intent(this, BoardCompletedActivity.class);
            completedIntent.putExtra("time", ((Chronometer)(findViewById(R.id.timerTextView))).getText());
            completedIntent.putExtra("mistakes", mistakesCounter + "");
            completedIntent.putExtra("difficulty", difficulty);
            startActivity(completedIntent);
            finish();
        }
    }

    private void numberButtonsOnClick(View v) {
        if (v instanceof TextView){
            TextView view = (TextView) v;
            int number = Integer.parseInt(view.getText().toString());
            if (number == selectedNumber) {
                selectedNumber = 0;
                view.setSelected(false);
                lightNumbersDown();
            }
            else {
                for (int i = 0; i < SUDOKU_SIZE; i++) {
                    numberButtons[i].setSelected(false);
                }
                selectedNumber = number;
                view.setSelected(true);
                lightSelectedNumberUp();
            }
        }
    }

    public void addMistake() {
        mistakesCounter++;
        TextView mistakes = findViewById(R.id.mistakesTextView);
        mistakes.setText(R.string.mistakes);
        String text = mistakes.getText() + " " + mistakesCounter;
        mistakes.setText(text);
    }

    public void addCorrect(int number) {
        counters[number - 1]++;
        if (counters[number - 1] == SUDOKU_SIZE) {
            numberButtons[number - 1].setEnabled(false);
            for (SudokuField[] fields : sudokuFields)
                for (SudokuField field : fields)
                    field.removeCandidate(number);
            new Thread(() -> {
                try {
                    Thread.sleep(100);
                    lightNumbersDown();
                } catch (InterruptedException e) {
                    lightNumbersDown();
                }
            }).start();
            selectedNumber = 0;
        }
        updateCounters();
    }

    public void pencilButtonOnClick(View v) {
        pencilSelected = !pencilSelected;
        v.setBackgroundColor(getResources().getColor(pencilSelected ? R.color.selectedBackgroundFieldColor : R.color.backgroundFieldColor, getTheme()));
    }

    private void lightNumbersDown() {
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            for (int j = 0; j < SUDOKU_SIZE; j++)
                sudokuFields[i][j].lightDown();
            counterTextViews[i].setVisibility(View.INVISIBLE);
        }
    }

    private void lightSelectedNumberUp() {
        lightNumbersDown();
        if (selectedNumber == 0)
            return;
        for (int i = 0; i < SUDOKU_SIZE; i++)
            for (int j = 0; j < SUDOKU_SIZE; j++)
                sudokuFields[i][j].lightUp(selectedNumber);
        counterTextViews[selectedNumber - 1].setVisibility(View.VISIBLE);
    }

    private void setFields(int[][] board) {
        LinearLayout sudokuField = findViewById(R.id.sudokuField);
        sudokuFields = new SudokuField[SUDOKU_SIZE][SUDOKU_SIZE];
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.VERTICAL);
            row.setDividerDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.sudoku_fields_divider, getTheme()));
            row.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            row.setGravity(Gravity.CENTER);
            sudokuField.addView(row);
            for (int j = 0; j < SUDOKU_SIZE; j++) {
                SudokuField button = new SudokuField(this);
                sudokuFields[i][j] = button;
                row.addView(button);
                if (j % 3 == 2 && j != SUDOKU_SIZE - 1) {
                    AppCompatButton btn = new AppCompatButton(this);
                    btn.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.sudoku_fields_divider));
                    btn.setText("");
                    row.addView(btn);
                    ViewGroup.LayoutParams params = btn.getLayoutParams();
                    params.height = 10;
                    params.width = 10;
                    btn.setLayoutParams(params);
                }
            }
            if (i % 3 == 2 && i != SUDOKU_SIZE - 1) {
                LinearLayout dividersRow = new LinearLayout(this);
                dividersRow.setOrientation(LinearLayout.VERTICAL);
                dividersRow.setGravity(Gravity.CENTER);
                sudokuField.addView(dividersRow);
                for (int j = 0; j < SUDOKU_SIZE; j++) {
                    AppCompatButton button = new AppCompatButton(this);
                    button.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.sudoku_fields_divider));
                    button.setText("");
                    dividersRow.addView(button);
                    ViewGroup.LayoutParams params = button.getLayoutParams();
                    params.height = 10;
                    params.width = 10;
                    button.setLayoutParams(params);
                }
            }
        }
        if (board == null) {
            Toast.makeText(this, "Błąd generowania planszy", Toast.LENGTH_LONG).show();
            return;
        }
        int[][] solvedBoard = new int[SUDOKU_SIZE][SUDOKU_SIZE];
        for (int i = 0; i < SUDOKU_SIZE; i++)
            System.arraycopy(board[i], 0, solvedBoard[i], 0, SUDOKU_SIZE);
        solvedBoard = Solver.solveBoard(solvedBoard);
        if (solvedBoard == null) {
            Toast.makeText(this, "Błąd tworzenia planszy", Toast.LENGTH_LONG).show();
            return;
        }
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            for (int j = 0; j < SUDOKU_SIZE; j++) {
                sudokuFields[i][j].setCorrectNumber(solvedBoard[i][j]);
                if (board[i][j] != 0)
                    sudokuFields[i][j].setNumber(board[i][j]);
                sudokuFields[i][j].setOnClickListener(this::sudokuButtonsOnClick);
                sudokuFields[i][j].setWidth(screenWidth);
            }
        }
    }

    private void setNumbers() {
        numberButtons = new NumberButton[SUDOKU_SIZE];
        counters = new int[SUDOKU_SIZE];
        LinearLayout numbers = findViewById(R.id.numbers);
        numbers.setGravity(Gravity.CENTER);
        numbers.setDividerDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.sudoku_fields_divider, getTheme()));
        numbers.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        for (int j = 0; j < SUDOKU_SIZE; j++) {
            counters[j] = 0;
            for (SudokuField[] fields : sudokuFields)
                for (SudokuField field : fields)
                    if (field.getNumber() == j + 1)
                        counters[j] ++;
            NumberButton button = new NumberButton(this);
            numbers.addView(button);
            button.setNumber(j + 1);
            button.setWidth(screenWidth);
            button.setOnClickListener(this::numberButtonsOnClick);
            numberButtons[j] = button;
        }
        counterTextViews = new TextView[SUDOKU_SIZE];
        counterTextViews[0] = findViewById(R.id.counter1);
        counterTextViews[1] = findViewById(R.id.counter2);
        counterTextViews[2] = findViewById(R.id.counter3);
        counterTextViews[3] = findViewById(R.id.counter4);
        counterTextViews[4] = findViewById(R.id.counter5);
        counterTextViews[5] = findViewById(R.id.counter6);
        counterTextViews[6] = findViewById(R.id.counter7);
        counterTextViews[7] = findViewById(R.id.counter8);
        counterTextViews[8] = findViewById(R.id.counter9);
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            counterTextViews[i].setVisibility(View.INVISIBLE);
        }
        updateCounters();
    }

    private void updateCounters() {
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            counterTextViews[i].setText(((SUDOKU_SIZE - counters[i]) + ""));
        }
    }

    public void backToMenu(View v) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((Chronometer)findViewById(R.id.timerTextView)).stop();
    }
}
*/
