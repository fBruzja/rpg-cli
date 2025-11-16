package com.rpg.characters.data;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Stats implements Serializable {
    private int strength;
    private int agility;
    private int intelligence;
    private int healthPoints;
    private int manaPoints;
    private int attackPoints;
    private int defense;
    private int exp;
}
