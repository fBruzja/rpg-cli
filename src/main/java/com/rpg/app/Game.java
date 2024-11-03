package com.rpg.app;

import com.rpg.characters.Enemy;
import com.rpg.characters.Player;
import com.rpg.datamanagement.ResourceManager;
import com.rpg.datamanagement.data.SaveData;
import com.rpg.map.Map;
import com.rpg.utils.GameLogger;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game {

    private static final Logger log = LoggerFactory.getLogger(Game.class);
    public static Scanner userInput = new Scanner(System.in);
    public static boolean zoramUndefeated = true;

    void startGame() {

        String name;
        char profession, playerChoice = ' ';
        Player mainCharacter = null;
        SaveData data;
        profession = '0';
        Enemy enemies[] = new Enemy[14];
        int i;

        intro();

        while (playerChoice != '1' && playerChoice != '2' && playerChoice != '3') {
            playerChoice = userInput.next()
                    .charAt(0);
            if (playerChoice != '1' && playerChoice != '2' && playerChoice != '3') {
                System.out.println("\nPlease enter '1', '2' or '3'");
            }
        }

        switch (playerChoice) {
            case '1': {
                playerChoice = ' ';
                System.out.print("\nTell us about yourself...\nWhat is your name traveler?\n");
                name = userInput.next();
                System.out.print("\nSuch an outlandish name dear " + name);
                System.out.print("\nYou are fully cloaked and we want to know if you are a man or a woman?"
                        + "\n'm' for Male\n'f' for Female\n");
                userInput.reset();

                System.out.print(
                        "\n\nAnd lastly what do you consider yourself?\nA fighter, an agile dependant or a magical user?");
                System.out.print("\n'w' for fighter\n't' for an agility dependant user\n'm' for a magic user\n");
                userInput.reset();
                while (profession != 'w' && profession != 't' && profession != 'm') {
                    profession = userInput.next()
                            .charAt(0);
                    if (profession != 'w' && profession != 't' && profession != 'm') {
                        System.out.println("Please enter 'w', 't' or 'm'");
                    }
                }

                switch (profession) {
                    case 'w':
                        System.out.print(
                                "\nAh so you are a warrior! You will find the fights you desire here then.\n");
                        break;
                    case 't':
                        System.out.print("\nA thief i see... May the shadows hide you.\n");
                        break;
                    case 'm':
                        System.out.print(
                                "\nA pupil of the arcane arts... You will find Zoram rather intriguing then!\n");
                        break;
                }

                mainCharacter = new Player(name, profession);
            }
            break;
            case '2': {
                data = (SaveData) ResourceManager.load("../saves/character.save");
                if (data == null) {
                    log.info("Character load failed");
                } else {
                    var playerInfo = data.getPlayerInformation();
                    mainCharacter = new Player(playerInfo.name(), profession);
                    ResourceManager.loadTheDataInThePlayer(data, mainCharacter);
                    System.out.println("\n\tGAME LOADED");
                }
            }
            break;
            case '3': {
                System.out.println("Until next time!");
                System.exit(0);
            }
            break;
        }

        userInput.reset();
        // generate map and monsters

        for (i = 0; i < 3; i++) {
            enemies[i] = new Enemy(20, 20, 5, 2, "Goblin", false);
        }
        for (i = 3; i < 6; i++) {
            enemies[i] = new Enemy(25, 20, 6, 3, "Skeleton", false);
        }
        for (i = 6; i < 8; i++) {
            enemies[i] = new Enemy(23, 30, 5, 4, "Rat-Man", false);
        }
        for (i = 8; i < 10; i++) {
            enemies[i] = new Enemy(35, 40, 6, 5, "Salamander", false);
        }
        for (i = 10; i < 12; i++) {
            enemies[i] = new Enemy(30, 35, 6, 5, "Kobold", false);
        }

        // creating the two bosses
        enemies[12] = new Enemy(35, 100, 7, 5, "Spectre", true);
        enemies[13] = new Enemy(70, 100, 10, 10, "Zoram", true);

        Map map = new Map();
        map.generateMapLayout();
        assert mainCharacter != null; // TODO: deal with it differently than just an assertion
        map.bringPlayerIntoMap(mainCharacter.getPlayerPosition().getX(), mainCharacter.getPlayerPosition().getY());
        for (i = 0; i < 12; i++) {
            map.bringMonsterToMap(enemies[i].getxPosition(), enemies[i].getyPosition(), enemies[i].getName());
        }
        map.bringMonsterToMap(enemies[12].getxPosition(), enemies[12].getyPosition(), enemies[12].getName());
        map.bringMonsterToMap(enemies[13].getxPosition(), enemies[13].getyPosition(), enemies[13].getName());
        // the game begins
        while (Game.zoramUndefeated) {
            map.printMap();
            menu();
            userInput.reset();

            while (playerChoice != 'w'
                    && playerChoice != 'a'
                    && playerChoice != 'd'
                    && playerChoice != 's'
                    && playerChoice != 'i'
                    && playerChoice != 'v'
                    && playerChoice != 'q') {
                playerChoice = userInput.next()
                        .charAt(0);
                if (playerChoice != 'w'
                        && playerChoice != 'a'
                        && playerChoice != 'd'
                        && playerChoice != 's'
                        && playerChoice != 'i'
                        && playerChoice != 'v'
                        && playerChoice != 'q') {
                    System.out.println("Please enter the appropriate choice.");
                }
            }

            switch (playerChoice) {
                case 'w': {
                    manageMovement(mainCharacter, map, enemies, 'w');
                }
                break;
                case 'a': {
                    manageMovement(mainCharacter, map, enemies, 'a');
                }
                break;
                case 'd': {
                    manageMovement(mainCharacter, map, enemies, 'd');
                }
                break;
                case 's': {
                    manageMovement(mainCharacter, map, enemies, 's');
                }
                break;
                case 'i':
                    showStats(mainCharacter);
                    break;
                case 'q': {
                    System.out.println("Until next time!");
                    System.exit(0);
                }
                break;
                case 'v': {
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

        if (!Game.zoramUndefeated) {
            System.out.println("You Win!");
            System.out.println("Hopefully you will retake this adventure again!\nExiting...");
            System.exit(0);
        }
        userInput.close();
    }

    void intro() {
        GameLogger.print("""

                Welcome traveler to this mysterious trial you will be facing!
                This is the magical land of Marghor, ruled by the tyrannical sorcerer Zoram.
                We bid you welcome!
                        
                Since you accept the challenge to defeat Zoram (which main character doesn't?)
                You must choose...
                    1) New Game
                    2) Continue
                    3) Exit
                """);
    }

    void menu() {
        System.out.print("\nPress 'w' to walk forward\nPress 'a' to move left\nPress 'd' to move right"
                + "\nPress 's' to move down\nPress 'i' to see your attributes\nPress 'v' to save your character\n"
                + "Press'q' to exit \n\n");
    }

    void showStats(Player p) {
        var playerStats = p.getPlayerStats();
        var playerInformation = p.getPlayerInformation();

        System.out.println("------------GENERAL INFORMATION----------");
        System.out.print("\tName: " + playerInformation.name());
        System.out.print("\n\tLevel: " + p.getLevel());
        System.out.print("\n\tProfession: " + playerInformation.profession()
                .getDisplayName());
        System.out.println("\n--------------BASIC ATTRIBUTES----------");
        System.out.print("\tStrength: " + playerStats.getStrength());
        System.out.print("\n\tAgility: " + playerStats.getAgility());
        System.out.print("\n\tIntelligence: " + playerStats.getIntelligence());
        System.out.println("\n-------------------STATS----------------");
        System.out.print("\tHP: " + playerStats.getHealthPoints());
        System.out.print("\n\tMP: " + playerStats.getManaPoints());
        System.out.print("\n\tAttack: " + playerStats.getAttackPoints());
        System.out.print("\n\tDefense: " + playerStats.getDefense());
        System.out.println("\n\tEXP: " + playerStats.getExp());
    }

    public Enemy checkWhichEnemy(int x, int y, Enemy[] enemies) {
        for (int i = 0; i < enemies.length; i++) {
            if (x == enemies[i].getxPosition() && y == enemies[i].getyPosition()) {
                return enemies[i];
            }
        }
        return null;
    }


    void manageMovement(Player p, Map map, Enemy[] enemies, char movement) {
        var xPosition = p.getPlayerPosition().getX();
        var yPosition = p.getPlayerPosition().getY();
        
        switch (movement) {
            case 'w': {
                if (map.checkIfOutOfBoundaries(xPosition - 1, yPosition)) {
                    char encounter = map.checkForEncounter(xPosition - 1, yPosition);
                    fightOrFindNothing(encounter, p, enemies, xPosition - 1, yPosition);
                    map.updateMap(xPosition - 1, yPosition, movement);
                    p.move(movement);
                } else {
                    // TODO: maybe not needed. Just let them hit a wall.
                    System.out.println("We cannot move out of the boundaries of our little universe, can we?");
                }
            }
            break;
            case 'a': {
                if (map.checkIfOutOfBoundaries(xPosition, yPosition - 1)) {
                    char encounter = map.checkForEncounter(xPosition, yPosition - 1);
                    fightOrFindNothing(encounter, p, enemies, xPosition, yPosition - 1);
                    map.updateMap(xPosition, yPosition - 1, movement);
                    p.move(movement);
                } else {
                    System.out.println("We cannot move out of the boundaries of our little universe, can we?");
                }
            }
            break;
            case 'd': {
                if (map.checkIfOutOfBoundaries(xPosition, yPosition + 1)) {
                    char encounter = map.checkForEncounter(xPosition, yPosition + 1);
                    fightOrFindNothing(encounter, p, enemies, xPosition, yPosition + 1);
                    map.updateMap(xPosition, yPosition + 1, movement);
                    p.move(movement);
                } else {
                    System.out.println("We cannot move out of the boundaries of our little universe, can we?");
                }
            }
            break;
            case 's': {
                if (map.checkIfOutOfBoundaries(xPosition + 1, yPosition)) {
                    char encounter = map.checkForEncounter(xPosition + 1, yPosition);
                    fightOrFindNothing(encounter, p, enemies, xPosition + 1, yPosition);
                    map.updateMap(xPosition + 1, yPosition, movement);
                    p.move(movement);
                } else {
                    System.out.println("We cannot move out of the boundaries of our little universe, can we?");
                }
            }
            break;
        }
    }

    public boolean fightOrFindNothing(
            char encounter, Player p, Enemy[] enemies, int enemyPositionX, int enemyPositionY
    ) {
        if (encounter == ' ') {
            System.out.println("You found nothing of interest here...");
        } else {
            return fight(p, checkWhichEnemy(enemyPositionX, enemyPositionY, enemies));
        }
        return false; // return false if there was no fight
    }

    boolean fight(Player p, Enemy e) {
        char choice = ' ';
        boolean poison = false;
        boolean disabledByPlayer = false;
        int turns = 0;

        if (e.getName()
                .equals("Zoram")) {
            System.out.println("You face the mighty and evil Zoram.\nPrepare yourself!");
        } else {
            System.out.println("You have stumbled upon " + e.getName() + ", prepare yourself!");
        }

        var stats = p.getPlayerStats();

        while (stats.getHealthPoints() > 0 && e.getHealthPoints() > 0) {

            // player's turn
            System.out.println(p.getPlayerInformation().name() + " HP: " + stats.getHealthPoints() + " | MP: " + stats.getManaPoints());
            System.out.println(e.getName() + " HP: " + e.getHealthPoints());
            System.out.println("It is your turn");
            System.out.println("What will your move be?");
            System.out.print("'a' for attack\n'b' for abilities\n");

            while (choice != 'a' && choice != 'b') {
                choice = userInput.next()
                        .charAt(0);
                if (choice != 'a' && choice != 'b') {
                    System.out.println("Please choose the appropriate choice.");
                }
            }

            switch (choice) {
                case 'a':
                    poison = p.physicalAtatck(p, e, poison);
                    break;
                case 'b': {
                    switch (p.showAndSelectAbilityDuringBattle(p, userInput)) {
                        case '0': {
                            poison = p.firstAbilityUsage(p, e);
                        }
                        break;
                        case '1': {
                            p.secondAbilityUsage(p, e);
                        }
                        break;
                        case '2': {
                            disabledByPlayer = p.thirdAbilityUsage(p, e);
                        }
                        break;
                        case '3': {
                            p.fourthAbilityUsage(p, e);
                        }
                        break;
                    }
                }
            }

            // enemy's turn
            System.out.println("It is " + e.getName() + "'s turn!");
            if (!disabledByPlayer) {
                if (e.getIsBoss()) {
                    switch (e.getName()) {
                        case "Spectre": {
                            if (turns == 0) {
                                System.out.println("Spectre throws demonic flames from his sickle\nAnd does: "
                                        + e.calculateDamageDoneByEnemyAndRemoveHealth(p, e, 15)
                                        + " damage.");
                                turns = 5;
                            } else {
                                System.out.println("Spectre attacks you and does: "
                                        + e.calculateDamageDoneByEnemyAndRemoveHealth(p, e, 0)
                                        + " damage.");
                            }
                        }
                        break;
                        case "Zoram": {
                            if (turns == 0) {
                                System.out.println(
                                        "The sorceror Zoram uses his healing abilities. His health increases by 50.");
                                e.setHealthPoints(e.getHealthPoints() + 50);
                            } else {
                                System.out.println("The sorceror Zoram attacks you and does: "
                                        + e.calculateDamageDoneByEnemyAndRemoveHealth(p, e, 0)
                                        + " damage.");
                            }
                        }
                        break;
                    }
                } else {
                    System.out.println(e.getName()
                            + " attacks you and does "
                            + e.calculateDamageDoneByEnemyAndRemoveHealth(p, e, 0)
                            + " damage\n");
                }

                if (turns > 0 || turns - 1 == 0) {
                    turns--;
                }
            } else {
                System.out.println("Enemy is disabled for this turn.");
                disabledByPlayer = false;
            }
            choice = ' ';
        }
        if (stats.getHealthPoints() <= 0) {
            System.out.println("Battle ended! You died...");
            System.out.println("GAME OVER");
            System.exit(0);
        }
        if (e.getName()
                .equals("Zoram")) {
            System.out.println("You defeated the evil"
                    + e.getName()
                    + " and won "
                    + e.getExpAmountWhenKilled()
                    + " EXP");
            System.out.println("His reign ends here!\nCongratulations " + p.getPlayerInformation().name() + "!\nYou truly are remarkable!");
            Game.zoramUndefeated = false;
        } else {
            System.out.println("You defeated " + e.getName() + " and won " + e.getExpAmountWhenKilled() + " EXP");
        }
        stats.setHealthPoints(stats.getHealthPoints() + 10);
        stats.setManaPoints(stats.getManaPoints() + 10);
        p.addExpAndCheckIfLeveledUp(p, e.getExpAmountWhenKilled());
        return true; // return true if fight is over and player is still alive
    }

}
