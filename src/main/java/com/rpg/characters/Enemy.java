package com.rpg.characters;

import java.util.Random;

public class Enemy {
	int healthPoints;
	int expAmountWhenKilled;
	int attackPower;
	int defense;
	int xPosition;
	int yPosition;
	String name;
	boolean isBoss;
	
	public Enemy(int newHealthPoints, int newExpAmountWhenKilled, int newAttackPower, int newDefense, String newName, boolean newIsBoss) {
		this.healthPoints = newHealthPoints;
		this.expAmountWhenKilled = newExpAmountWhenKilled;
		this.attackPower = newAttackPower;
		this.defense = newDefense;
		this.name = newName;
		
		// generating random positions for the monster
		Random rand = new Random();
		this.xPosition = rand.nextInt((37 - 1) + 1) + 1;
		this.yPosition = rand.nextInt((37 - 1) + 1) + 1;
	}
	
	public int calculateDamageDoneByEnemyAndRemoveHealth(Player p, Enemy e, int specialFactor) {
		if((e.getAttackPower() - (p.getDefense() / 2) + specialFactor) < 0)
				return 0;
		p.setHealthPoints(p.getHealthPoints() - (e.getAttackPower() - (p.getDefense() / 2) + specialFactor));
		return (e.getAttackPower() - (p.getDefense() / 2) + specialFactor);
	}
	
	public String getName() {
		return name;
	}

	public int getxPosition() {
		return xPosition;
	}

	public int getyPosition() {
		return yPosition;
	}
	
	public int getHealthPoints() {
		return healthPoints;
	}
	
	public int getDefense() {
		return defense;
	}
	
	public boolean getIsBoss() {
		return isBoss;
	}
	
	public int getExpAmountWhenKilled() {
		return expAmountWhenKilled;
	}

	public int getAttackPower() {
		return attackPower;
	}
	
	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
	}
}
