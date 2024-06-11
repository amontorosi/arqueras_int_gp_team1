package com.gp.arqueras_javafx;

/**
 * Clase para almacenar información general sobre la partida, como la dificultad, el idioma y la ronda
 *
 * @author Manuel Valdepeñas
 * @version 3.0 05/05/2024
 */
public class Game {
    private String difficulty;
    private static String language;
    private int currentRound;
    public static final String[] difficulties = {"easy", "normal", "hard"};

    public Game(String difficulty) {
        this.setDifficulty(difficulty);
        this.setCurrentRound(1);
    }

    public String getDifficulty() {
        return difficulty;
    }

    public static String getLanguage() {
        return language;
    }

    public static void setLanguage(String language) {
        Game.language = language;
    }

    public void setDifficulty(String difficulty) {
        boolean theSame = false;
        for (int i = 0; i < difficulties.length; i++) {
            if (difficulty.equalsIgnoreCase(difficulties[i])) {
                theSame = true;
                break;
            }
        }
        if (theSame) {
            this.difficulty = difficulty;
        } else {
            this.difficulty = "normal"; //si la dificultad no es válida, por defecto se pone en normal
        }
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }
}
