package com.rpg.userinterface;

import java.util.Optional;

public enum PlayerChoiceCommand {
    NEW_GAME('1'), LOAD('2'), QUIT('3');

    private final char key;

    PlayerChoiceCommand(char key) { this.key = key; }

    public char key() { return key; }

    public static Optional<PlayerChoiceCommand> fromChar(char c) {
        for (PlayerChoiceCommand cmd : values()) {
            if (cmd.key == c) return Optional.of(cmd);
        }
        return Optional.empty();
    }
}
