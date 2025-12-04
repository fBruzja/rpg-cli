package com.rpg.characters;

import com.rpg.characters.data.Stats;
import com.rpg.map.Map;
import java.io.Serial;
import java.io.Serializable;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Enemy implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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

        Random rand = new Random();
        this.xPosition = getRandomEnemyInitialPosition(rand, Map.mapWidth, Player.STARTING_X_COORDINATE);
        this.yPosition = getRandomEnemyInitialPosition(rand, Map.mapHeight, Player.STARTING_Y_COORDINATE);
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

    private int getRandomEnemyInitialPosition(Random rand, int dimension, int exception) {
        int positionToBeGenerated = exception;
        while (positionToBeGenerated == exception) {
            positionToBeGenerated = rand.nextInt(dimension - 3) + 1;
        }
        return positionToBeGenerated;
    }

}
