package com.example.speedgame;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private GridLayout gameGrid;
    private TextView tvPattern, tvScore, tvTime;
    private List<Button> gameButtons;
    private GameLogic gameLogic;

    private int difficulty;
    private CountDownTimer gameTimer, patternTimer;
    private long gameTimeLeft = 60000; // 60 sekund gry
    private int currentPattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        difficulty = getIntent().getIntExtra("difficulty", 3);

        initViews();
        setupGame();
        startGame();
    }

    private void initViews() {
        gameGrid = findViewById(R.id.game_grid);
        tvPattern = findViewById(R.id.tv_pattern);
        tvScore = findViewById(R.id.tv_score);
        tvTime = findViewById(R.id.tv_time);
        gameButtons = new ArrayList<>();
        gameLogic = new GameLogic();
    }

    private void setupGame() {
        // Ustaw siatkę w zależności od trudności
        if (difficulty == 3) {
            gameGrid.setColumnCount(3);
            gameGrid.setRowCount(1);
        } else if (difficulty == 6) {
            gameGrid.setColumnCount(3);
            gameGrid.setRowCount(2);
        } else {
            gameGrid.setColumnCount(3);
            gameGrid.setRowCount(3);
        }

        // Stwórz przyciski
        for (int i = 0; i < difficulty; i++) {
            Button btn = new Button(this);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();

            params.width = 0;
            params.height = 0;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);

            int margin = 8; // 8dp margines
            params.setMargins(margin, margin, margin, margin);

            btn.setLayoutParams(new GridLayout.LayoutParams());
            btn.setText("?");
            btn.setTextSize(20);

            final int index = i;
            btn.setOnClickListener(v -> onButtonClick(index));

            gameButtons.add(btn);
            gameGrid.addView(btn);
        }

        generateRandomColors();
    }

    private void generateRandomColors() {
        Random random = new Random();
        int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.CYAN};

        for (int i = 0; i < gameButtons.size(); i++) {
            int colorIndex = random.nextInt(colors.length);
            gameButtons.get(i).setBackgroundColor(colors[colorIndex]);
            gameButtons.get(i).setTag(colorIndex); // Zapisz indeks koloru
        }
    }

    private void startGame() {
        generateNewPattern();
        startGameTimer();
        startPatternTimer();
    }

    private void generateNewPattern() {
        Random random = new Random();
        currentPattern = random.nextInt(6); // 6 możliwych kolorów

        String[] colorNames = {"Czerwony", "Niebieski", "Zielony", "Żółty", "Fioletowy", "Cyan"};
        tvPattern.setText("Znajdź: " + colorNames[currentPattern]);

        // Ustaw kolor tła wzorca
        int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.CYAN};
        tvPattern.setBackgroundColor(colors[currentPattern]);
    }

    private void onButtonClick(int buttonIndex) {
        Button clickedButton = gameButtons.get(buttonIndex);
        int buttonColor = (Integer) clickedButton.getTag();

        if (buttonColor == currentPattern) {
            // Poprawna odpowiedź
            gameLogic.addScore(10);
            generateNewPattern();
            generateRandomColors();
        } else {
            // Błędna odpowiedź
            gameLogic.subtractScore(5);
        }

        updateScore();
    }

    private void startGameTimer() {
        gameTimer = new CountDownTimer(gameTimeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                gameTimeLeft = millisUntilFinished;
                tvTime.setText("Czas: " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                endGame();
            }
        }.start();
    }

    private void startPatternTimer() {
        long interval = difficulty == 3 ? 3000 : (difficulty == 6 ? 2000 : 1500);

        patternTimer = new CountDownTimer(Long.MAX_VALUE, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                generateNewPattern();
                generateRandomColors();
            }

            @Override
            public void onFinish() {}
        }.start();
    }

    private void updateScore() {
        tvScore.setText("Punkty: " + gameLogic.getScore());
    }

    private void endGame() {
        if (gameTimer != null) gameTimer.cancel();
        if (patternTimer != null) patternTimer.cancel();

        // Wyświetl wynik końcowy
        tvPattern.setText("KONIEC GRY! Wynik: " + gameLogic.getScore());

        // Wyłącz wszystkie przyciski
        for (Button btn : gameButtons) {
            btn.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameTimer != null) gameTimer.cancel();
        if (patternTimer != null) patternTimer.cancel();
    }
}