package com.jakub.sudoku;

import android.content.Context;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;

public class SudokuField extends AppCompatButton {
    private Integer number = null, correctNumber;
    private final ArrayList<Integer> candidates = new ArrayList<>();

    public SudokuField(Context context) {
        super(context);
        setBackgroundColor(getResources().getColor(R.color.backgroundFieldColor, getContext().getTheme()));
        setTextColor(getResources().getColor(R.color.inputNumberColor, getContext().getTheme()));
        setPadding(0, 0, 0, 0);
    }

    private void showText() {
        if (number != null) {
            setTextSize(18);
            setText(String.valueOf(number));
        }
        else {
            setTextSize(7);
            if (candidates.size() > 0) {
                StringBuilder candidatesText = new StringBuilder();
                for (int i = 0; i < GameActivity.SUDOKU_SIZE; i++) {
                    if (candidates.contains((i + 1)))
                        candidatesText.append(i + 1);
                    else
                        candidatesText.append(" ");
                    candidatesText.append(i % 3 == 2 ? "\n" : "  ");
                }
                setText(candidatesText.toString());
            }
            else setText("");
        }
    }

    public void lightUp(Integer number) {
        if (number == null)
            lightDown();
        if (this.number != null) {
            if (this.number.equals(number)){
                if (number.equals(correctNumber))
                    setBackgroundColor(getResources().getColor(R.color.lightBackgroundFieldColor, getContext().getTheme()));
                else
                    setBackgroundColor(getResources().getColor(R.color.invalidBackgroundFieldColor, getContext().getTheme()));
            }
        }
        else if (candidates.contains(number))
            setBackgroundColor(getResources().getColor(R.color.lightBackgroundFieldColor, getContext().getTheme()));
        else
            setBackgroundColor(getResources().getColor(R.color.backgroundFieldColor, getContext().getTheme()));
    }

    public void lightDown() {
        setBackgroundColor(getResources().getColor(R.color.backgroundFieldColor, getContext().getTheme()));
    }

    public void setWidth(int screenWidth) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = screenWidth / 10 - 20;
        params.height = screenWidth / 10 - 20;
        setLayoutParams(params);
    }

    public void setNumber(Integer number) {
        setTextColor(getResources().getColor(R.color.numberColor, getContext().getTheme()));
        this.number = number;
        showText();
    }

    public int getNumber() {
        return number == null ? 0 : number;
    }

    public void inputNumber(Integer number, GameActivity gameActivity) {
        if (number == 0)
            return;
        if (this.number == null) {
            this.number = number;
            if (correctNumber.equals(number))
                gameActivity.addCorrect(number);
            else
                gameActivity.addMistake();
        }
        else if (this.number.equals(number) && !this.number.equals(correctNumber))
                this.number = null;
        lightUp(number);
        showText();
    }

    public void setCorrectNumber(int number) {
        correctNumber = number;
    }

    public void addCandidate(int number) {
        if (number <= 0)
            return;
        if (candidates.contains(number))
            candidates.remove((Integer) number);
        else
            candidates.add(number);
        lightUp(number);
        showText();
    }

    public void removeCandidate(int number) {
        candidates.remove((Integer) number);
        showText();
    }

    public boolean isCorrect() {
        if (number == null || correctNumber == null)
            return false;
        return number.equals(correctNumber);
    }

    /*public void calculateCandidates(int i, int j, int[][] board) {
        if (number != null)
            return;
        candidates = new ArrayList<>();
        for (int k = 1; k < 10; k++)
            candidates.add(k);
        for (int k = 0; k < 9; k++)
            if (board[i][k] != 0)
                candidates.remove((Integer)board[i][k]);
        for (int k = 0; k < 9; k++)
            if (board[k][j] != 0)
                candidates.remove((Integer)board[k][j]);
        for (int k = 0; k < 9; k++)
            for (int l = 0; l < 9; l++)
                if (board[k][j] != 0 && 3 * (i / 3) + j / 3 == 3 * (k / 3) + l / 3)
                    candidates.remove((Integer)board[k][l]);
        showText();
    }*/
}
