package com.rpg.app;

import com.rpg.characters.Enemy;
import com.rpg.characters.Player;
import com.rpg.datamanagement.ResourceManager;
import com.rpg.datamanagement.data.SaveData;
import com.rpg.game.BattleController;
import com.rpg.game.GameState;
import com.rpg.game.MovementController;
import com.rpg.game.outcome.BattleResult;
import com.rpg.game.outcome.MovementResult;
import com.rpg.map.Map;
import com.rpg.userinterface.MainCommand;
import com.rpg.userinterface.PlayerChoiceCommand;
import com.rpg.userinterface.UserInterface;
import java.util.ArrayList;
import java.util.List;

public class Game {

    public static final String ZORAM = "Zoram";
    public static final char PLAYER_CHARACTER = 'X';

    private final GameState gameState;
    private final MovementController movementController;
    private final BattleController battleController;
    private final List<Enemy> enemiesList;
    Map map;

    public Game() {
        map = new Map();
        this.gameState = new GameState();
        battleController = new BattleController();
        enemiesList = new ArrayList<>(getEnemiesList());
        this.movementController = new MovementController(map);
    }

    void startGame() {

        Player player = null;
        SaveData data;

        UserInterface.showIntro();

        PlayerChoiceCommand playerChoice = UserInterface.readMainMenuChoice();

        switch (playerChoice) {
            case NEW_GAME: {
                player = Player.createNewPlayer();
            }
            break;
            case LOAD: {
                data = (SaveData) ResourceManager.load("../saves/character.save");
                if (data == null) {
                    UserInterface.renderMessages(List.of("Character load failed"));
                } else {
                    var playerInfo = data.getPlayerInformation();
                    // todo: will need to be revamped just a little bit
                    player = new Player(playerInfo.name(), playerInfo.profession().name().charAt(0));
                    ResourceManager.loadTheDataInThePlayer(data, player);
                    System.out.println("\n\tGAME LOADED");
                }
            }
            break;
            case QUIT: {
                exitGame();
            }
            break;
        }

        if (player == null) {
            UserInterface.renderMessages(List.of("Failed to initialize player. Exiting..."));
            return;
        }

        putCharacterAndNpcsIntoMap(player);

        // the game begins
        while (gameState.shouldContinue()) {
            map.printMap();
            UserInterface.showMenu();

            MainCommand cmd = UserInterface.readMainLoopCommand();

            switch (cmd) {
                case SHOW_STATS:
                    UserInterface.showStats(player);
                    break;
                case QUIT: {
                    exitGame();
                }
                break;
                case SAVE: {
                    data = ResourceManager.createSaveData(player);
                    try {
                        ResourceManager.save(data, "../saves/character.save");
                        System.out.println("Data saved successfully!");
                    } catch (Exception e) {
                        System.out.println("Could not save: " + e.getMessage());
                    }
                }
                break;
                case MOVE_UP:
                case MOVE_LEFT:
                case MOVE_RIGHT:
                case MOVE_DOWN: {
                    char moveKey = cmd.key();
                    handleMovement(player, moveKey);
                }
                break;
            }
        }

        if (gameState.isZoramDefeated()) {
            UserInterface.renderMessages(List.of("You have defeated Zoram!", "Hopefully you will retake this adventure again!\nExiting..."));
            exitGame();
        }
    }

    private void handleMovement(Player player, char movement) {
        MovementResult result = movementController.attemptMove(player, movement);

        if (!result.isSuccessful()) {
            UserInterface.renderMessages(List.of(result.getMessage()));
            return;
        }

        if (result.hasEncounter()) {
            handleEncounter(player, result.getTargetCoordinates());
        }
    }

    private void handleEncounter(Player player, com.rpg.map.Coordinates coordinates) {
        Enemy enemy = movementController.findEnemyAt(coordinates.x(), coordinates.y(), enemiesList);

        if (enemy == null) {
            return; // No enemy found
        }

        BattleResult battleResult = battleController.runBattle(player, enemy);

        if (battleResult.playerDied()) {
            gameState.triggerGameOver();
        }

        if (battleResult.enemyDied()) {
            enemiesList.remove(enemy);

            if (ZORAM.equals(enemy.getName())) {
                gameState.defeatZoram();
            }

            UserInterface.renderMessages(battleResult.messages());
        }
    }

    private void putCharacterAndNpcsIntoMap(Player player) {
        map.putEntityInMap(player.getPlayerPosition().getX(), player.getPlayerPosition().getY(), PLAYER_CHARACTER);
        for (Enemy enemy : enemiesList) {
            map.putEntityInMap(enemy.getXPosition(), enemy.getYPosition(), enemy.getIcon());
        }
    }

    private static void exitGame() {
        UserInterface.renderMessages(List.of("Until next time!"));
        System.exit(0);
    }

    private static List<Enemy> getEnemiesList() {
        return List.of(
                new Enemy(20, 20, 5, 2, 'G', "Goblin"),
                new Enemy(20, 20, 5, 2, 'G', "Goblin"),
                new Enemy(25, 20, 6, 3, 'S', "Skeleton"),
                new Enemy(25, 20, 6, 3, 'S', "Skeleton"),
                new Enemy(23, 30, 5, 4, 'R', "Rat-Man"),
                new Enemy(23, 30, 5, 4, 'R', "Rat-Man"),
                new Enemy(35, 40, 6, 5, 'D', "Salamander"),
                new Enemy(35, 40, 6, 5, 'D', "Salamander"),
                new Enemy(30, 35, 6, 5, 'K', "Kobold"),
                new Enemy(30, 35, 6, 5, 'K', "Kobold"),
                new Enemy(35, 100, 7, 5, 'P', "Spectre"),
                new Enemy(70, 100, 10, 10, 'Z', ZORAM)
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

}
