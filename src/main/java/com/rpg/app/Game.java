package com.rpg.app;

import java.util.Scanner;

import com.rpg.map.Map;

import com.rpg.characters.Enemy;
import com.rpg.characters.Player;
import com.rpg.datamanagement.*;

public class Game {
	public static Scanner userInput = new Scanner(System.in);
	public static boolean zoramUndefeated = true;
	
	void startGame() {
		
		String name; 
		char gender, profession, playerChoice = ' ';
		Player mainCharacter = null;
		SaveData data = null;
		gender = '0';
		profession = '0';
		Enemy enemies[] = new Enemy[14];
		int i;
		
		intro();
		
		while(playerChoice != '1' && playerChoice != '2' && playerChoice != '3') {
			playerChoice = userInput.next().charAt(0);
			if(playerChoice != '1' && playerChoice != '2' && playerChoice != '3')
				System.out.println("\nPlease enter '1', '2' or '3'");
		}
		
		switch(playerChoice) {
			case '1': {
				playerChoice = ' ';
				System.out.print("\nTell us about yourself...\nWhat is your name traveler?\n");
				name = userInput.next();
				System.out.print("\nSuch an outlandish name dear " + name);
				System.out.print("\nYou are fully cloaked and we want to know if you are a man or a woman?"
						+ "\n'm' for Male\n'f' for Female\n");
				userInput.reset();
				while(gender != 'm' && gender != 'f') {
					gender = userInput.next().charAt(0);
					if(gender != 'm' && gender != 'f')
						System.out.println("Please enter 'm' or 'f'");
				}
					
				System.out.print("\n\nAnd lastly what do you consider yourself?\nA fighter, an agile dependant or a magical user?");
				System.out.print("\n'w' for fighter\n't' for an agility dependant user\n'm' for a magic user\n");
				userInput.reset();
				while(profession != 'w' && profession != 't' && profession != 'm') {
					profession = userInput.next().charAt(0);
					if(profession != 'w' && profession != 't' && profession != 'm')
						System.out.println("Please enter 'w', 't' or 'm'");
				}
				
				if(gender == 'm') {
					switch(profession) {
					case 'w':
						System.out.print("\nAh so you are a warrior! You will find the fights you desire here then.\n");
						break;
					case 't':
						System.out.print("\nA thief i see... May the shadows hide you.\n");
						break;
					case 'm':
						System.out.print("\nA pupil of the arcane arts... You will find Zoram rather intriguing then!\n");
						break;
					}
				} else {
					switch(profession) {
					case 'w':
						System.out.print("\nAh so you are an amazon! You will find the challenges you desire here then.\n");
						break;
					case 't':
						System.out.print("\nA rogue i see... May your blade be swift as you are!\n");
						break;
					case 'm':
						System.out.print("\nAh, a sorceress... I expect quite the match for Zoram!\n");
						break;
					}
				}
				mainCharacter = new Player(name, (gender == 'm') ? "Male" : "Female", profession);
			} break;
			case '2': {
				try {
					data = (SaveData) ResourceManager.load("../saves/character.save");
				} catch (Exception e) {
					System.out.println("Could not load saved data: " + e.getMessage());
				}
				mainCharacter = new Player(data.name, data.gender, (data.profession == "Male") ? 'm' : 'f');
				ResourceManager.loadTheDataInThePlayer(data, mainCharacter);
				System.out.println("\n\tGAME LOADED");
			} break;
			case '3': {
				System.out.println("Until next time!");
				System.exit(0);
			} break;
		}
		
		
		userInput.reset();
		// generate map and monsters
		
		for(i=0; i<3; i++)
			enemies[i] = new Enemy(20, 20, 5, 2, "Goblin", false);
		for(i=3; i<6; i++)
			enemies[i] = new Enemy(25, 20, 6, 3, "Skeleton", false);
		for(i=6; i<8; i++)
			enemies[i] = new Enemy(23, 30, 5, 4, "Rat-Man", false);
		for(i=8; i<10; i++)
			enemies[i] = new Enemy(35, 40, 6, 5, "Salamander", false);
		for(i=10; i<12; i++)
			enemies[i] = new Enemy(30, 35, 6, 5, "Kobold", false);
		
		// creating the two bosses
		enemies[12] = new Enemy(35, 100, 7, 5, "Spectre", true);
		enemies[13] = new Enemy(70, 100, 10, 10, "Zoram", true);
		
		Map map = new Map();
		map.generateMapLayout();
		map.bringPlayerIntoMap(mainCharacter.getxPosition(), mainCharacter.getyPosition());
		for(i=0; i<12; i++)
			map.bringMonsterToMap(enemies[i].getxPosition(), enemies[i].getyPosition(), enemies[i].getName());
		map.bringMonsterToMap(enemies[12].getxPosition(), enemies[12].getyPosition(), enemies[12].getName());
		map.bringMonsterToMap(enemies[13].getxPosition(), enemies[13].getyPosition(), enemies[13].getName());
		// the game begins
		while(Game.zoramUndefeated) {
			map.printMap();
			menu();
			userInput.reset();
			
			while(playerChoice != 'w' && playerChoice != 'a' && playerChoice != 'd' && playerChoice != 's' && playerChoice != 'i' && playerChoice != 'v' && playerChoice != 'q') {
				playerChoice = userInput.next().charAt(0);
				if(playerChoice != 'w' && playerChoice != 'a' && playerChoice != 'd' && playerChoice != 's' && playerChoice != 'i' && playerChoice != 'v' && playerChoice != 'q')
					System.out.println("Please enter the appropriate choice.");
			}
			
			switch(playerChoice) {
				case 'w': {
					manageMovement(mainCharacter, map, enemies, 'w');
				} break;
				case 'a' : {
					manageMovement(mainCharacter, map, enemies, 'a');
				} break;
				case 'd' : {
					manageMovement(mainCharacter, map, enemies, 'd');
				} break;
				case 's' : {
					manageMovement(mainCharacter, map, enemies, 's');
				} break;
				case 'i' : 
					showStats(mainCharacter);
				break;
				case 'q' : {
					System.out.println("Until next time!");
					System.exit(0);
				} break;
				case 'v' : {
					data = ResourceManager.createSaveData(mainCharacter);
					try {
						ResourceManager.save(data, "../saves/character.save");
						System.out.println("Data saved successfully!");
					} catch (Exception e) {
						System.out.println("Could not save: " + e.getMessage());
					}
				}
			}

			playerChoice = ' ';
		}

		if(!Game.zoramUndefeated) {
			System.out.println("You Win!");
			System.out.println("Hopefully you will retake this adventure again!\nExiting...");
			System.exit(0);
		}
		userInput.close();
	}
	
	void intro() {
		System.out.println("Welcome traveler to this mysterious trial you will be facing!");
		System.out.println("This is the magical land of Marghor, ruled by the tyrannical sorceror Zoram.");
		System.out.println("We bid you welcome!");
		System.out.println("Since you accept the challenge to defeat Zoram (which main character doesn't?)");
		System.out.println("You must choose...");
		System.out.println("\t1) New Game");
		System.out.println("\t2) Continue");
		System.out.println("\t3) Exit");
	}
	
	void menu() {
		System.out.print("\nPress 'w' to walk forward\nPress 'a' to move left\nPress 'd' to move right"
				+ "\nPress 's' to move down\nPress 'i' to see your attributes\nPress 'v' to save your character\n"
				+ "Press'q' to exit \n\n");
	}
	
	void showStats(Player p) {
		System.out.println("------------GENERAL INFORMATION----------");
		System.out.print("\tName: " + p.getName());
		System.out.print("\n\tLevel: " + p.getLevel());
		System.out.print("\n\tProfession: " + p.getProfession());
		System.out.print("\n\tGender: " + p.getGender());
		System.out.println("\n--------------BASIC ATTRIBUTES----------");
		System.out.print("\tStrength: " + p.getStrength());
		System.out.print("\n\tAgility: " + p.getAgility());
		System.out.print("\n\tMagicka: " + p.getMagicka());
		System.out.println("\n-------------------STATS----------------");
		System.out.print("\tHP: " + p.getHealthPoints());
		System.out.print("\n\tMP: " + p.getManaPoints());
		System.out.print("\n\tAttack: " + p.getAttackPoints());
		System.out.print("\n\tDefense: " + p.getDefense());
		System.out.println("\n\tEXP: " + p.getExp());
	}
	
	public Enemy checkWhichEnemy(int x, int y, Enemy[] enemies) {
		for(int i = 0; i < enemies.length; i++) {
			if(x == enemies[i].getxPosition() && y == enemies[i].getyPosition())
				return enemies[i];
		}
		return null;
	}
	
	
	void manageMovement(Player p, Map map, Enemy[] enemies, char movement) {
		switch(movement) {
			case 'w' : {
				if(map.checkIfOutOfBoundaries(p.getxPosition() - 1, p.getyPosition())) {
					char encounter = map.checkForEncounter(p.getxPosition() - 1, p.getyPosition());
					fightOrFindNothing(encounter, p, enemies, p.getxPosition() - 1, p.getyPosition());
					map.updateMap(p.getxPosition() - 1, p.getyPosition(), movement);
					p.move(movement);
				} else 
					System.out.println("We cannot move out of the boundaries of our little universe, can we?");
			} break;
			case 'a' : {
				if(map.checkIfOutOfBoundaries(p.getxPosition(), p.getyPosition() - 1)) {
					char encounter = map.checkForEncounter(p.getxPosition(), p.getyPosition() - 1);
					fightOrFindNothing(encounter, p, enemies, p.getxPosition(), p.getyPosition() - 1);
					map.updateMap(p.getxPosition(), p.getyPosition() - 1, movement);
					p.move(movement);
				} else 
					System.out.println("We cannot move out of the boundaries of our little universe, can we?");
			} break;
			case 'd' : {
				if(map.checkIfOutOfBoundaries(p.getxPosition(), p.getyPosition() + 1)) {
					char encounter = map.checkForEncounter(p.getxPosition(), p.getyPosition() + 1);
					fightOrFindNothing(encounter, p, enemies, p.getxPosition(), p.getyPosition() + 1);
					map.updateMap(p.getxPosition(), p.getyPosition() + 1, movement);
					p.move(movement);
				} else 
					System.out.println("We cannot move out of the boundaries of our little universe, can we?");
			} break;
			case 's' : {
				if(map.checkIfOutOfBoundaries(p.getxPosition() + 1, p.getyPosition())) {
					char encounter = map.checkForEncounter(p.getxPosition() + 1, p.getyPosition());
					fightOrFindNothing(encounter, p, enemies, p.getxPosition() + 1, p.getyPosition());
					map.updateMap(p.getxPosition() + 1, p.getyPosition(), movement);
					p.move(movement);
				} else 
					System.out.println("We cannot move out of the boundaries of our little universe, can we?");
			} break;
		}
	}
	
	public boolean fightOrFindNothing(char encounter, Player p, Enemy[] enemies, int enemyPositionX, int enemyPositionY) {
		if(encounter == ' ')
			System.out.println("You found nothing of interest here...");
		else 
			return fight(p, checkWhichEnemy(enemyPositionX, enemyPositionY, enemies));
		return false; // return false if there was no fight
	}
	
	boolean fight(Player p, Enemy e) {
		char choice = ' ';
		boolean poison = false;
		boolean disabledByPlayer = false;
		int turns = 0;
		
		if(e.getName().equals("Zoram")) 
			System.out.println("You face the mighty and evil Zoram.\nPrepare yourself!");
		else
			System.out.println("You have stumbled upon " + e.getName() + ", prepare yourself!");
		
		while(p.getHealthPoints() > 0 && e.getHealthPoints() > 0) {
			
			// player's turn
			System.out.println(p.getName() + " HP: " + p.getHealthPoints() + " | MP: " + p.getManaPoints());
			System.out.println(e.getName() + " HP: " + e.getHealthPoints());
			System.out.println("It is your turn");
			System.out.println("What will your move be?");
			System.out.print("'a' for attack\n'b' for abilities\n");
			
			while(choice != 'a' && choice != 'b') {
				choice = userInput.next().charAt(0);
				if(choice != 'a' && choice != 'b')
					System.out.println("Please choose the appropriate choice.");
			}
			
			switch(choice) {
				case 'a':
						poison = p.physicalAtatck(p, e, poison);
						break;
				case 'b' : {
					switch(p.showAndSelectAbilityDuringBattle(p, userInput)) {
						case '0': {
							poison = p.firstAbilityUsage(p, e);
						} break;
						case '1': {
							p.secondAbilityUsage(p, e);
						} break;
						case '2': {
							disabledByPlayer = p.thirdAbilityUsage(p, e);
						} break;
						case '3': {
							p.fourthAbilityUsage(p, e);
						} break;
					}
				}
			}
			
			// enemy's turn
			System.out.println("It is " + e.getName() + "'s turn!");
			if(!disabledByPlayer) {
				if(e.getIsBoss()) {
					switch(e.getName()) {
						case "Spectre" : {
							if(turns == 0) {
								System.out.println("Spectre throws demonic flames from his sickle\nAnd does: " + e.calculateDamageDoneByEnemyAndRemoveHealth(p, e, 15) + " damage.");
								turns = 5;
							} else 
								System.out.println("Spectre attacks you and does: " + e.calculateDamageDoneByEnemyAndRemoveHealth(p, e, 0) + " damage.");
						} break;
						case "Zoram" : {
							if(turns == 0) {
								System.out.println("The sorceror Zoram uses his healing abilities. His health increases by 50.");
								e.setHealthPoints(e.getHealthPoints() + 50);
							} else 
								System.out.println("The sorceror Zoram attacks you and does: " + e.calculateDamageDoneByEnemyAndRemoveHealth(p, e, 0) + " damage.");
						} break;
					}
				} else 
					System.out.println(e.getName() + " attacks you and does " + e.calculateDamageDoneByEnemyAndRemoveHealth(p, e, 0) + " damage\n");
				
				if(turns > 0 || turns - 1 == 0) 
					turns--;
			} else {
				System.out.println("Enemy is disabled for this turn.");
				disabledByPlayer = false;
			}
			choice = ' ';
		}
		if(p.getHealthPoints() <= 0) {
			System.out.println("Battle ended! You died...");
			System.out.println("GAME OVER");
			System.exit(0);
		}
		if(e.getName().equals("Zoram")) {
			System.out.println("You defeated the evil" + e.getName() + " and won " + e.getExpAmountWhenKilled() + " EXP");
			System.out.println("His reign ends here!\nCongratulations " + p.getName() + "!\nYou truly are remarkable!");
			Game.zoramUndefeated = false;
		} else
			System.out.println("You defeated " + e.getName() + " and won " + e.getExpAmountWhenKilled() + " EXP");
		p.setHealthPoints(p.getHealthPoints() + 10);
		p.setManaPoints(p.getManaPoints() + 10);
		p.addExpAndCheckIfLeveledUp(p, e.getExpAmountWhenKilled());
		return true; // return true if fight is over and player is still alive
	}
}
