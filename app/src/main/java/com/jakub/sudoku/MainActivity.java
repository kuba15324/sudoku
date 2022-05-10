package com.jakub.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Application;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void newGame(int difficulty) {
        Intent gameIntent = new Intent(this, GeneratingBoardActivity.class);
        gameIntent.putExtra("difficulty", difficulty);
        startActivity(gameIntent);
    }

    public void newEasyGame(View v) { newGame(0); }
    public void newMediumGame(View v) { newGame(5); }
    public void newHardGame(View v) { newGame(10); }
    public void newExpertGame(View v) { newGame(20); }
    public void newImpossibleGame(View v) { newGame(30); }
}