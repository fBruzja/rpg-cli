package com.rpg.app;

import com.rpg.characters.Enemy;
import com.rpg.characters.Player;
import com.rpg.datamanagement.ResourceManager;
import com.rpg.datamanagement.data.SaveData;
import com.rpg.game.BattleController;
import com.rpg.game.outcome.BattleResult;
import com.rpg.map.Coordinates;
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
    private final BattleController battleController;
    private final List<Enemy> enemiesList;
    public static boolean GAME_OVER = false;
    Map map;

    public Game() {
        battleController = new BattleController();
        enemiesList = getEnemiesList();
        map = new Map();
    }

    void startGame() {

        String name;
        char profession, playerChoice = ' ';
        Player player = null;
        SaveData data;
        profession = '0';


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

        assert player != null; // TODO: deal with it differently than just an assertion

        map.putPlayerInGameMap(player.getPlayerPosition().getX(), player.getPlayerPosition().getY());
        for (Enemy enemy : enemiesList) {
            map.bringMonsterToMap(enemy.getXPosition(), enemy.getYPosition(), enemy.getIcon());
        }

        // the game begins
        while (Game.zoramUndefeated && !GAME_OVER) {
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
                    manageMovement(player, map, 'w');
                }
                break;
                case 'a': {
                    manageMovement(player, map, 'a');
                }
                break;
                case 'd': {
                    manageMovement(player, map, 'd');
                }
                break;
                case 's': {
                    manageMovement(player, map, 's');
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

    void manageMovement(Player p, Map map, char movement) {
        int x = p.getPlayerPosition().getX();
        int y = p.getPlayerPosition().getY();

        Coordinates target = computeTargetCoordinates(x, y, movement);
        if (target == null) {
            UserInterface.renderMessages(java.util.List.of("Unknown movement. Use w/a/s/d."));
            return;
        }

        if (!map.checkIfOutOfBoundaries(target.x(), target.y())) {
            UserInterface.renderMessages(java.util.List.of("You cannot move there."));
            return;
        }

        char encounter = map.checkForEncounter(target.x(), target.y());
        manageEncounter(encounter, p, target.x(), target.y());

        map.updateMap(target.x(), target.y(), movement);
        p.move(movement);
    }

    private Coordinates computeTargetCoordinates(int x, int y, char movement) {
        return switch (movement) {
            case 'w' -> new Coordinates(x - 1, y);
            case 'a' -> new Coordinates(x, y - 1);
            case 'd' -> new Coordinates(x, y + 1);
            case 's' -> new Coordinates(x + 1, y);
            default -> null;
        };
    }

    public void manageEncounter(char encounter, Player player, int enemyPositionX, int enemyPositionY) {
        if (encounter != ' ') {
            Enemy enemyConfronted = checkWhichEnemy(enemyPositionX, enemyPositionY, enemiesList);
            BattleResult battleResult = battleController.runBattle(player, enemyConfronted);

            if(battleResult.playerDied()) {
                GAME_OVER = true;
            }

            if(battleResult.enemyDied()) {
                UserInterface.renderMessages(battleResult.messages());
            }
        }
    }
}
