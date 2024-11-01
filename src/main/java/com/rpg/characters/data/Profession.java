package com.rpg.characters.data;

import lombok.Getter;

@Getter
public enum Profession {
    WARRIOR("Warrior"),
    THIEF("Thief"),
    MAGE("Mage");

    private final String displayName;

    Profession(String displayName) {
        this.displayName = displayName;
    }

    // TODO: do i need this?
    @Override
    public String toString() {
        return displayName;
    }
}
