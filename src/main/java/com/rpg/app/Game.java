package com.rpg.app;

import com.rpg.characters.Enemy;
import com.rpg.characters.Player;
import com.rpg.datamanagement.ResourceManager;
import com.rpg.datamanagement.data.SaveData;
import com.rpg.game.outcome.AttackOutcome;
import com.rpg.map.Map;
import com.rpg.userinterface.UserInterface;
import java.util.List;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game {

    private static final Logger log = LoggerFactory.getLogger(Game.class);
    public static final String ZORAM = "Zoram";
    public static Scanner userInput = new Scanner(System.in);
    public static boolean zoramUndefeated = true;

    void startGame() {

        String name;
        char profession, playerChoice = ' ';
        Player player = null;
        SaveData data;
        profession = '0';

        List<Enemy> enemiesList = getEnemiesList();

        UserInterface.showIntro();

        while (playerChoice != '1' && playerChoice != '2' && playerChoice != '3') {
            playerChoice = userInput.next().charAt(0);
            if (playerChoice != '1' && playerChoice != '2' && playerChoice != '3') {
                System.out.println("\nPlease enter '1', '2' or '3'");
            }
        }

        switch (playerChoice) {
            case '1': {
                playerChoice = ' ';
                System.out.print("\nTell us about yourself...\nWhat is your name traveler?\n");
                name = userInput.next();
                userInput.reset();

                System.out.print("\n\nWhat do you consider yourself?\nA fighter, a rogue or a magical user?");
                System.out.print("\n'w' for fighter\n't' for an agility dependant user\n'm' for a magic user\n");
                userInput.reset();
                while (profession != 'w' && profession != 't' && profession != 'm') {
                    profession = userInput.next().charAt(0);
                    if (profession != 'w' && profession != 't' && profession != 'm') {
                        System.out.println("Please enter 'w', 't' or 'm'");
                    }
                }

                switch (profession) {
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

                player = new Player(name, profession);
            }
            break;
            case '2': {
                data = (SaveData) ResourceManager.load("../saves/character.save");
                if (data == null) {
                    log.info("Character load failed");
                } else {
                    var playerInfo = data.getPlayerInformation();
                    player = new Player(playerInfo.name(), profession);
                    ResourceManager.loadTheDataInThePlayer(data, player);
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
        // generate map

        Map map = new Map();

        assert player != null; // TODO: deal with it differently than just an assertion

        map.putPlayerInGameMap(player.getPlayerPosition().getX(), player.getPlayerPosition().getY());
        for (Enemy enemy : enemiesList) {
            map.bringMonsterToMap(enemy.getXPosition(), enemy.getYPosition(), enemy.getIcon());
        }

        // the game begins
        while (Game.zoramUndefeated) {
            map.printMap();
            UserInterface.showMenu();
            userInput.reset();

            while (playerChoice != 'w'
                    && playerChoice != 'a'
                    && playerChoice != 'd'
                    && playerChoice != 's'
                    && playerChoice != 'i'
                    && playerChoice != 'v'
                    && playerChoice != 'q') {
                playerChoice = userInput.next().charAt(0);
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
                    manageMovement(player, map, enemiesList, 'w');
                }
                break;
                case 'a': {
                    manageMovement(player, map, enemiesList, 'a');
                }
                break;
                case 'd': {
                    manageMovement(player, map, enemiesList, 'd');
                }
                break;
                case 's': {
                    manageMovement(player, map, enemiesList, 's');
                }
                break;
                case 'i':
                    UserInterface.showStats(player);
                    break;
                case 'q': {
                    System.out.println("Until next time!");
                    System.exit(0);
                }
                break;
                case 'v': {
                    data = ResourceManager.createSaveData(player);
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

    private static List<Enemy> getEnemiesList() {
        return List.of(
                new Enemy(20, 20, 5, 2,'G', "Goblin"),
                new Enemy(20, 20, 5, 2,'G', "Goblin"),
                new Enemy(25, 20, 6, 3,'S', "Skeleton"),
                new Enemy(25, 20, 6, 3,'S', "Skeleton"),
                new Enemy(23, 30, 5, 4,'R', "Rat-Man"),
                new Enemy(23, 30, 5, 4,'R', "Rat-Man"),
                new Enemy(35, 40, 6, 5,'D', "Salamander"),
                new Enemy(35, 40, 6, 5,'D', "Salamander"),
                new Enemy(30, 35, 6, 5,'K', "Kobold"),
                new Enemy(30, 35, 6, 5,'K', "Kobold"),
                new Enemy(35, 100, 7, 5,'P', "Spectre"),
                new Enemy(70, 100, 10, 10,'Z', ZORAM)
        );
    }

    public Enemy checkWhichEnemy(int x, int y, List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (x == enemy.getXPosition() && y == enemy.getYPosition()) {
                return enemy;
            }
        }
        return null;
    }


    void manageMovement(Player p, Map map, List<Enemy> enemies, char movement) {
        var xPosition = p.getPlayerPosition().getX();
        var yPosition = p.getPlayerPosition().getY();

        switch (movement) {
            case 'w': {
                if (map.checkIfOutOfBoundaries(xPosition - 1, yPosition)) {
                    char encounter = map.checkForEncounter(xPosition - 1, yPosition);
                    checkIfMonsterEncounter(encounter, p, enemies, xPosition - 1, yPosition);
                    map.updateMap(xPosition - 1, yPosition, movement);
                    p.move(movement);
                } else {
                    // TODO: maybe not needed. Just let them hit a wall come on.
                    System.out.println("We cannot move out of the boundaries of our little universe, can we?");
                }
            }
            break;
            case 'a': {
                if (map.checkIfOutOfBoundaries(xPosition, yPosition - 1)) {
                    char encounter = map.checkForEncounter(xPosition, yPosition - 1);
                    checkIfMonsterEncounter(encounter, p, enemies, xPosition, yPosition - 1);
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
                    checkIfMonsterEncounter(encounter, p, enemies, xPosition, yPosition + 1);
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
                    checkIfMonsterEncounter(encounter, p, enemies, xPosition + 1, yPosition);
                    map.updateMap(xPosition + 1, yPosition, movement);
                    p.move(movement);
                } else {
                    System.out.println("We cannot move out of the boundaries of our little universe, can we?");
                }
            }
            break;
        }
    }

    public void checkIfMonsterEncounter(char encounter, Player p, List<Enemy> enemies, int enemyPositionX, int enemyPositionY) {
        if (encounter != ' ') {
            fight(p, checkWhichEnemy(enemyPositionX, enemyPositionY, enemies));
        }
    }

    void fight(Player p, Enemy e) {
        char choice = ' ';
        boolean poison = false;
        boolean disabledByPlayer = false;
        int turns = 0;

        if (e.getName().equals(ZORAM)) {
            System.out.println("You face the mighty and evil Zoram.\nPrepare yourself!");
        } else {
            System.out.println("You have stumbled upon a " + e.getName() + ", prepare yourself!");
        }

        var stats = p.getPlayerStats();

        while (stats.getHealthPoints() > 0 && e.getHealthPoints() > 0) {

            UserInterface.printPlayerHUD(p, e);

            while (choice != 'a' && choice != 'b') {
                choice = userInput.next().charAt(0);
                if (choice != 'a' && choice != 'b') {
                    System.out.println("Please choose the appropriate choice.");
                }
            }

            switch (choice) {
                case 'a':
                    AttackOutcome outcome = p.physicalAttack(e, poison);
                    break;
                case 'b': {
                    switch (p.showAndSelectAbilityDuringBattle(p, userInput)) {
                        case '0': {
                            poison = p.useFirstSlotAbility(e);
                        }
                        break;
                        case '1': {
                            p.useSecondSlotAbility(p, e);
                        }
                        break;
                        case '2': {
                            disabledByPlayer = p.useThirdSlotAbility(p, e);
                        }
                        break;
                        case '3': {
                            p.useFourthSlotAbility(p, e);
                        }
                        break;
                    }
                }
            }

            // enemy's turn
            System.out.println("It is " + e.getName() + "'s turn!");
            if (!disabledByPlayer) {
                if (e.isBoss()) {
                    switch (e.getName()) {
                        case "Spectre": {
                            if (turns == 0) {
                                System.out.println("Spectre throws demonic flames from his sickle\nAnd does: "
                                        + e.calculateAndApplyDamage(p.getPlayerStats(), 15)
                                        + " damage.");
                                turns = 5;
                            } else {
                                System.out.println("Spectre attacks you and does: " + e.calculateAndApplyDamage(p.getPlayerStats(), 0) + " damage.");
                            }
                        }
                        break;
                        case ZORAM: {
                            if (turns == 0) {
                                System.out.println("The sorceror Zoram uses his healing abilities. His health increases by 50.");
                                e.setHealthPoints(e.getHealthPoints() + 50);
                            } else {
                                System.out.println("The sorceror Zoram attacks you and does: " + e.calculateAndApplyDamage(p.getPlayerStats(), 0) + " damage.");
                            }
                        }
                        break;
                    }
                } else {
                    System.out.println(e.getName() + " attacks you and does " + e.calculateAndApplyDamage(p.getPlayerStats(), 0) + " damage\n");
                }

                if (turns >= 0) {
                    turns--;
                }
            } else {
                System.out.println("Enemy is disabled for this turn.");
                disabledByPlayer = false;
            }
            choice = ' ';
        }
        if (stats.getHealthPoints() <= 0) {
            System.out.println("Battle ended! You died.");
            System.out.println("GAME OVER");
            System.exit(0);
        }
        if (e.getName().equals(ZORAM)) {
            System.out.println("You defeated the evil" + e.getName() + " and won " + e.getExpAmountWhenKilled() + " EXP");
            System.out.println("His reign ends here!\nCongratulations " + p.getPlayerInformation().name() + "!\nYou truly are remarkable!");
            Game.zoramUndefeated = false;
        } else {
            System.out.println("You defeated " + e.getName() + " and won " + e.getExpAmountWhenKilled() + " EXP");
        }
        stats.setHealthPoints(stats.getHealthPoints() + 10);
        stats.setManaPoints(stats.getManaPoints() + 10);
        p.getBattleRewards(e.getExpAmountWhenKilled());
    }

}
