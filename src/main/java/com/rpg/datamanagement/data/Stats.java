package com.rpg.datamanagement.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Stats {
    private int strength;
    private int agility;
    private int magicka;
    private int healthPoints;
    private int manaPoints;
    private int attackPoints;
    private int defense;
    private int exp;
}
