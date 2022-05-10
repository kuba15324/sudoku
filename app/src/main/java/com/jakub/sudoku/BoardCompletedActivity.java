package com.jakub.sudoku;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BoardCompletedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_completed);
        String text = getString(R.string.winning_message) + "\n"
                + getString(R.string.mistakes) + " "
                + getIntent().getStringExtra("mistakes") + "\n"
                + getString(R.string.time) + " "
                + getIntent().getStringExtra("time");
        ((TextView)findViewById(R.id.winning_message)).setText(text);
    }

    public void backToMenuButtonOnClick(View v) {
        finish();
    }
}
