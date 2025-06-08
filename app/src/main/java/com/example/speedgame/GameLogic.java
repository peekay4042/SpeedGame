package com.example.speedgame;

public class GameLogic {
    private int score;
    private int correctAnswers;
    private int wrongAnswers;

    public GameLogic() {
        this.score = 0;
        this.correctAnswers = 0;
        this.wrongAnswers = 0;
    }

    public void addScore(int points) {
        score += points;
        correctAnswers++;
    }

    public void subtractScore(int points) {
        score -= points;
        if (score < 0) score = 0; // Nie pozwól na ujemne punkty
        wrongAnswers++;
    }

    public int getScore() {
        return score;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public double getAccuracy() {
        int total = correctAnswers + wrongAnswers;
        if (total == 0) return 0;
        return (double) correctAnswers / total * 100;
    }

    public void resetGame() {
        score = 0;
        correctAnswers = 0;
        wrongAnswers = 0;
    }
}