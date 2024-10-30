package com.rpg.characters;

import java.util.Scanner;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {
	/* All stats are based upon the basic attributes */

	// Player information
	String name;
	String gender;
	String profession = "";
	int level = 1;
	int xPosition = 38;
	int yPosition = 19;
	final static int MAX_LEVEL = 10;

	// Basic attributes
	int strength = 10;
	int agility = 10;
	int magicka = 10;

	// Stats and abilities
	int healthPoints = strength * 5;
	int manaPoints = magicka * 5;
	int attackPoints = strength / 2;
	int defense = agility / 2;
	int exp = 0;
	String[] abilities = new String[4];
	boolean warriorUsedLastSkill = false;

	public Player(String newName, String newGender, char newProfession) {
		this.name  = newName;
		this.gender = newGender;
		
		switch(newProfession) {
				case 'w': {
					this.profession = (newGender.equals("Male")) ? "Warrior" : "Amazon";
					abilities[0] = "Power Attack";
				} break;
				case 't': {
					this.profession = (newGender.equals("Male")) ? "Thief" : "Rogue";
					abilities[0] = "Poisoned Dagger";
				} break;
				case 'm': {
					this.profession = (newGender.equals("Male")) ? "Mage" : "Sorceress";
					abilities[0] = "Fireball";
				} break;
		}
		
		for(int i=1; i<4;i++)
			abilities[i] = "---Empty---";
	}
	
	public void move(char movement) {
		switch (movement) {
			case 'w':
				this.setXPosition(this.getXPosition() - 1);
				break;
			case 'a':
				this.setYPosition(this.getYPosition() - 1);
				break;
			case 'd':
				this.setYPosition(this.getYPosition() + 1);
				break;
			case 's':
				this.setXPosition(this.getXPosition() + 1);
				break;
			default:
				System.out.println("Please enter one of the directions!");
		}
	}
	
	public boolean physicalAtatck(Player p, Enemy e, boolean poison) {
		if(poison) {
			System.out.println("You attack with your poisoned blade! You made: " + p.calculateDamageDoneByPlayer(p, e, 1, true) + " damage!");
			p.consumeManaAndRemoveEnemyHealth(p, e, p.calculateDamageDoneByPlayer(p, e, 1, true), 0);
			poison = false;
		} else {
			System.out.println("You attack! You made: " + p.calculateDamageDoneByPlayer(p, e, 0, true) + " damage!");
			p.consumeManaAndRemoveEnemyHealth(p, e, p.calculateDamageDoneByPlayer(p, e, 0, true), 0);
		}
		return poison;
	}
	
	public char showAndSelectAbilityDuringBattle(Player p, Scanner userInput) {
		boolean emptySpellChoice = false;
		char choice = ' ';
		
		System.out.println("Your abilities are: ");
		String[] abilities = p.getAbilities();
		
		for(int i=0; i<abilities.length; i++) 
			System.out.println(i + ") " + abilities[i]);
			
		System.out.println("\nChoose according to number");
		userInput.reset();
		
		while(!emptySpellChoice) {
			choice = userInput.next().charAt(0);
			if(choice != '0' && choice != '1' && choice != '2' && choice != '3') 
				System.out.println("Please choose the appropriate number.");
			else {
				if(abilities[Character.getNumericValue(choice)].equals("---Empty---")) 
					System.out.println("Please choose a spell slot that it is not empty.");
				else
					emptySpellChoice = true;
			}
		}
		
		return choice;
	}
	
	public boolean firstAbilityUsage(Player p, Enemy e) {
		boolean poison = false;
		if(p.getProfession() == "Warrior" || p.getProfession() == "Amazon") {
			if(p.getManaPoints() >= 20) {
				System.out.println("You use " + abilities[0] + "! You made: " + p.calculateDamageDoneByPlayer(p, e, 2, false) + " damage!");
				p.consumeManaAndRemoveEnemyHealth(p, e, p.calculateDamageDoneByPlayer(p, e, 2, false), 20);
			} else 
				System.out.println("You do not have enough mana. You need 20 for this skill.");
		} else if(p.getProfession() == "Thief" || p.getProfession() == "Rogue") {
			if(p.getManaPoints() >= 15) {
				System.out.println("You use "+ abilities[0] + "! Your next attack will do more damage.");
				poison = true;
				p.setManaPoints(p.getManaPoints() - 15);
			} else
				System.out.println("You do not have enough mana. You need 15 for this skill.");
		} else {
			if(p.getManaPoints() >= 15) {
				System.out.println("You use! "+ abilities[0] + " You made " + p.calculateDamageDoneByPlayer(p, e, 3, true) + " damage.");
				p.consumeManaAndRemoveEnemyHealth(p, e, p.calculateDamageDoneByPlayer(p, e, 3, true), 15);
			} else 
				System.out.println("You do not have enough mana. You need 15 for this skill.");
		}
		return poison;
	}
	
	public void secondAbilityUsage(Player p, Enemy e) {
		if(p.getProfession() == "Warrior" || p.getProfession() == "Amazon") {
			if(p.getManaPoints() >= 40) {
				System.out.println("You use " + abilities[1] + ". Your health is permanently regenerated +50");
				p.setHealthPoints(p.getHealthPoints() + 50);
			} else
				System.out.println("You do not have enough mana.");
		} else if(p.getProfession() == "Thief" || p.getProfession() == "Rogue") {
			if(p.getManaPoints() >= 50) {
				System.out.println("You use " + abilities[1] + "! You made " + p.calculateDamageDoneByPlayer(p, e, 3, false) + " damage");
				p.consumeManaAndRemoveEnemyHealth(p, e, p.calculateDamageDoneByPlayer(p, e, 3, false), 50);
			} else 
				System.out.println("You do not have enough mana. You need 50 for this skill");
		} else {
			if(p.getManaPoints() >= 20) {
				System.out.println("You use " + abilities[1] +"! You made " + p.calculateDamageDoneByPlayer(p, e, 10, true) + " damage");
				p.consumeManaAndRemoveEnemyHealth(p, e, p.calculateDamageDoneByPlayer(p, e, 10, true), 20);
			} else
				System.out.println("You do not have enough mana. You need 20 for this skill.");
		}
	}
	
	public boolean thirdAbilityUsage(Player p, Enemy e) {
		boolean disable = false;
		
		if(p.getProfession() == "Warrior" || p.getProfession() == "Amazon") {
				System.out.println("You use " + abilities[2] + "! Your opponent is disabled for the next turn");
				disable = true;
		} else if(p.getProfession() == "Thief" || p.getProfession() == "Rogue") {
			if(p.getManaPoints() >= 30) {
				System.out.println("You use " + abilities[2] + " to ignore the opponent's defense! \nYou made " + p.calculateDamageDoneByPlayer(p, e, (1 + e.getDefense() / 2), true) + " damage");
				p.consumeManaAndRemoveEnemyHealth(p, e, p.calculateDamageDoneByPlayer(p, e, (1 + e.getDefense() / 2), true), 30);
			} else 
				System.out.println("You do not have enough mana. You need 30 mana for this skill.");
		} else {
			if(p.getManaPoints() >= 35) {
				System.out.println("You use " + abilities[2] + "! Opponent is disabled for the next turn!");
				disable = true;
			} else
				System.out.println("You do not have enough mana. You need 35 for this skill.");
		}
		
		return disable;
	}
	
	public void fourthAbilityUsage(Player p, Enemy e) {
		if(p.getProfession().equals("Warrior") || p.getProfession().equals("Amazon")) {
			if(p.isWarriorUsedLastSkill()) {
				System.out.println("You used " + abilities[3] + "! Your defense is increased by 5 permanently.");
				p.setWarriorUsedLastSkill(false);
			} else
				System.out.println("That skill can be used only once.");
		} else if(p.getProfession() == "Thief" || p.getProfession() == "Rogue") {
			if(p.getManaPoints() >= 60) {
				System.out.println("You used " + abilities[3] + ". Opponent is in near death.");
				p.consumeManaAndRemoveEnemyHealth(p, e, e.getHealthPoints() - 5, 60);
			} else 
				System.out.println("You do not have enough mana. You need 60 mana for this skill.");
		} else {
			if(p.getManaPoints() >= 30) {
				System.out.println("You use " + abilities[3] + "! You made " + p.calculateDamageDoneByPlayer(p, e, 25, true) + " damage.");
				p.consumeManaAndRemoveEnemyHealth(p, e, p.calculateDamageDoneByPlayer(p, e, 25, true), 30);
			} else
				System.out.println("You do not have enough mana. You need 30 for this skill.");
		}
	}
	
	public int calculateDamageDoneByPlayer(Player p, Enemy e, int bonusFactor, boolean bonusType) {
		if(bonusType) {
			// addition if true
			if((p.getAttackPoints() + bonusFactor) - (e.getDefense() / 2) < 0)
				return 0;
			return ((p.getAttackPoints() + bonusFactor) - (e.getDefense() / 2));
		} else {
			if((p.getAttackPoints() * bonusFactor) - (e.getDefense() / 2) < 0)
				return 0;
			return ((p.getAttackPoints() * bonusFactor) - (e.getDefense() / 2));
		}
	}
 	
	public void consumeManaAndRemoveEnemyHealth(Player p, Enemy e, int damage, int manaToRemove) {
		e.setHealthPoints(e.getHealthPoints() - damage);
		p.setManaPoints(p.getManaPoints() - manaToRemove);
	}
	
	public void refreshSkills(Player p) {
		p.setAttackPoints(p.getStrength() / 2);
		p.setManaPoints(p.getMagicka() * 5);
		p.setHealthPoints(p.getStrength() * 5);
		p.setDefense(p.getAgility() / 2);
	}
	
	public void levelUp(Player p) {
		p.setLevel(p.getLevel() + 1);
		p.setExp(0);
		
		if(p.getProfession().equals("Warrior") || p.getProfession().equals("Amazon")) {
			p.setStrength(p.getStrength() + 3);
			p.setAgility(p.getAgility() + 1);
			p.setMagicka(p.getMagicka() + 1);
			
			refreshSkills(p);
			addNewAbilities(p, 'w', p.getLevel());
		} else if(p.getProfession().equals("Thief") || p.getProfession().equals("Rogue")) {
			p.setStrength(p.getStrength() + 1);
			p.setAgility(p.getAgility() + 3);
			p.setMagicka(p.getMagicka() + 1);
			
			refreshSkills(p);
			addNewAbilities(p, 't', p.getLevel());
		} else {
			p.setStrength(p.getStrength() + 1);
			p.setAgility(p.getAgility() + 1);
			p.setMagicka(p.getMagicka() + 3);
			
			refreshSkills(p);
			addNewAbilities(p, 'm', p.getLevel());
		}
	}
	
	public void addNewAbilities(Player p, char profession, int level) {
		switch(profession) {
			case 'w': {
				switch(level) {
					case 3 : {
						System.out.println("You have learned a new ability: Healing Salve. \n It heals 50 HP");
						p.modifyAbility(1, "Healing Salve");
					} break;
					case 6 : {
						System.out.println("You have learned a new ability: Charge\nOpponent gets disabled for the next turn!");
						p.modifyAbility(2, "Charge");
					} break;
					case 10: {
						System.out.println("You have learned a new ability: Best Defense\nYour defense permanently increases by 5");
						p.modifyAbility(3, "Best Defense");
					} break;
				}
			} break;
			case 't': {
					switch(level) {
						case 3 : {
							System.out.println("You have learned a new ability: Critical Strike. \n Your next attack does triple damage");
							p.modifyAbility(1, "Critical Strike");
						} break;
						case 6 : {
							System.out.println("You have learned a new ability: Wither\nOpponent's defense is ignored!");
							p.modifyAbility(2, "Wither");
						} break;
						case 10: {
							System.out.println("You have learned a new ability: Coup De Grace\nYour attack brings your enemy near death!");
							p.modifyAbility(3, "Coup De Grace");
						} break;
					}
				} break;
			case 'm': {
				switch(level) {
					case 3 : {
						System.out.println("You have learned a new ability: Electrocute. \n Deals 10+ damage!");
						p.modifyAbility(1, "Electrocute");
					} break;
					case 6 : {
						System.out.println("You have learned a new ability: Degen Aura\nEnemy is disabled the next turn!");
						p.modifyAbility(2, "Degen Aura");
					} break;
					case 10: {
						System.out.println("You have learned a new ability: Death Dome\nDeals 25+ damage");
						p.modifyAbility(3, "Best Defense");
					} break;
				}
			} break;
		}
	}
	
	public void addExpAndCheckIfLeveledUp(Player p, int exp) {
		p.setExp(p.getExp() + exp);
		if(p.getExp() >= 50) {
			if(p.getLevel() == Player.MAX_LEVEL)
				System.out.println("You have achieved the maximum level of your abilities!");
			else {
				System.out.println("You have leveled up! Your basic attributes have grown stronger!");
				p.levelUp(p);
			}
		}
	}
	
	public void modifyAbility(int index, String ability) {
		abilities[index] = ability;
	}
}