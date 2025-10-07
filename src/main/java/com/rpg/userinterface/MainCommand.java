package com.rpg.userinterface;

import java.util.Optional;

public enum MainCommand {
    MOVE_UP('w'), MOVE_LEFT('a'), MOVE_RIGHT('d'), MOVE_DOWN('s'),
    SHOW_STATS('i'), SAVE('v'), QUIT('q');

    private final char key;

    MainCommand(char key) { this.key = key; }

    public char key() { return key; }

    public static Optional<MainCommand> fromChar(char c) {
        for (MainCommand cmd : values()) {
            if (cmd.key == c) return Optional.of(cmd);
        }
        return Optional.empty();
    }
}
