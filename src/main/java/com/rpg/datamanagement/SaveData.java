package com.rpg.datamanagement;

import java.io.Serial;
import java.io.Serializable;

public class SaveData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public String name, gender, profession;
    public String[] abilities = new String[4];
    public int level, xPosition, yPosition, strength, agility, magicka, healthPoints, manaPoints, attackPoints, defense, exp;

}
