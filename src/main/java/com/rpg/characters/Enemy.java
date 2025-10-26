package com.rpg.characters;

import com.rpg.characters.data.Stats;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Enemy{

    /* Monsters have only stats */
    int healthPoints;
    int expAmountWhenKilled;
    int attackPower;
    int defense;
    int xPosition;
    int yPosition;
    String name;
    boolean isBoss;
    char icon;

    public Enemy(
            int newHealthPoints,
            int newExpAmountWhenKilled,
            int newAttackPower,
            int newDefense,
            char newIcon,
            String newName
    ) {
        this.healthPoints = newHealthPoints;
        this.expAmountWhenKilled = newExpAmountWhenKilled;
        this.attackPower = newAttackPower;
        this.defense = newDefense;
        this.name = newName;
        this.icon = newIcon;

        // generating random positions for the monster
        Random rand = new Random();
        this.xPosition = rand.nextInt((37 - 1) + 1) + 1;
        this.yPosition = rand.nextInt((37 - 1) + 1) + 1;
    }

    public Enemy(
            int healthPoints,
            int expAmountWhenKilled,
            int attackPower,
            int defense,
            int xPosition,
            int yPosition,
            char icon,
            String name,
            boolean isBoss
    ) {
        this.healthPoints = healthPoints;
        this.expAmountWhenKilled = expAmountWhenKilled;
        this.attackPower = attackPower;
        this.defense = defense;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.icon = icon;
        this.name = name;
        this.isBoss = isBoss;
    }

    public int calculateAndApplyDamage(Stats playerStats, int specialFactor) {
        int playerDefense = playerStats.getDefense();
        int damage = attackPower - (playerDefense / 2) + specialFactor;

        damage = Math.max(0, damage);

        playerStats.setHealthPoints(playerStats.getHealthPoints() - damage);

        return damage;
    }

    public void reduceHealth(int damage) {
        healthPoints = Math.max(0, healthPoints - damage); // Prevent negative health
    }

}
