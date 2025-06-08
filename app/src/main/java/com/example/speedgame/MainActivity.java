package com.example.speedgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnEasy, btnMedium, btnHard;
    private int selectedDifficulty = 3; // domyślnie łatwy

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnEasy = findViewById(R.id.btn_easy);
        btnMedium = findViewById(R.id.btn_medium);
        btnHard = findViewById(R.id.btn_hard);
    }

    private void setupClickListeners() {
        btnEasy.setOnClickListener(v -> {
            selectedDifficulty = 3;
            updateButtonSelection();
        });

        btnMedium.setOnClickListener(v -> {
            selectedDifficulty = 6;
            updateButtonSelection();
        });

        btnHard.setOnClickListener(v -> {
            selectedDifficulty = 9;
            updateButtonSelection();
        });

        findViewById(R.id.btn_start).setOnClickListener(v -> startGame());
    }

    private void updateButtonSelection() {
        // Reset wszystkie przyciski
        btnEasy.setSelected(false);
        btnMedium.setSelected(false);
        btnHard.setSelected(false);

        // Zaznacz wybrany
        switch(selectedDifficulty) {
            case 3: btnEasy.setSelected(true); break;
            case 6: btnMedium.setSelected(true); break;
            case 9: btnHard.setSelected(true); break;
        }
    }

    private void startGame() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("difficulty", selectedDifficulty);
        startActivity(intent);
    }
}