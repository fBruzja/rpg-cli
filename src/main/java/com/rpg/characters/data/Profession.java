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

    public int getStrengthBonus() {
        return this == WARRIOR ? 3 : 1;
    }

    public int getAgilityBonus() {
        return this == THIEF ? 3 : 1;
    }

    public int getIntelligenceBonus() {
        return this == MAGE ? 3 : 1;
    }

}
