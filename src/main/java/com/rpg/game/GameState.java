package com.rpg.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameState {
    private boolean zoramDefeated;
    private boolean gameOver;

    public GameState() {
        this.zoramDefeated = false;
        this.gameOver = false;
    }

    /**
     * Marks Zoram as defeated and triggers victory condition
     */
    public void defeatZoram() {
        this.zoramDefeated = true;
    }

    /**
     * Marks the game as over (player died)
     */
    public void triggerGameOver() {
        this.gameOver = true;
    }

    /**
     * Checks if the game should continue running
     */
    public boolean shouldContinue() {
        return !gameOver && !zoramDefeated;
    }
}
