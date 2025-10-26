package com.rpg.app;

import com.rpg.characters.Enemy;
import com.rpg.characters.Player;
import com.rpg.datamanagement.ResourceManager;
import com.rpg.datamanagement.data.SaveData;
import com.rpg.game.BattleController;
import com.rpg.game.EnemyFactory;
import com.rpg.game.EnemyManager;
import com.rpg.game.GameState;
import com.rpg.game.MovementController;
import com.rpg.game.outcome.BattleResult;
import com.rpg.game.outcome.MovementResult;
import com.rpg.map.Map;
import com.rpg.userinterface.MainCommand;
import com.rpg.userinterface.PlayerChoiceCommand;
import com.rpg.userinterface.UserInterface;
import java.util.List;

public class Game {

    public static final String ZORAM = "Zoram";
    public static final char PLAYER_CHARACTER = 'X';

    private final GameState gameState;
    private final MovementController movementController;
    private final BattleController battleController;
    private final Map map;
    private final EnemyManager enemyManager;

    public Game() {
        map = new Map();
        this.gameState = new GameState();
        battleController = new BattleController();
        this.movementController = new MovementController(map);
        this.enemyManager = new EnemyManager();
    }

    void startGame() {

        Player player = null;
        SaveData data;

        UserInterface.showIntro();

        PlayerChoiceCommand playerChoice = UserInterface.readMainMenuChoice();

        switch (playerChoice) {
            case NEW_GAME: {
                player = Player.createNewPlayer();
                enemyManager.spawnEnemies(EnemyFactory.createDefaultEnemies());
            }
            break;
            case LOAD: {
                data = (SaveData) ResourceManager.load("../saves/character.save");
                if (data == null) {
                    UserInterface.renderMessages(List.of("Character load failed"));
                } else {
                    var playerInfo = data.getPlayerInformation();
                    player = new Player(playerInfo.name(), playerInfo.profession().name().charAt(0));
                    ResourceManager.loadTheDataInThePlayer(data, player);

                    // Load saved enemy manager or create new if missing (backward compatibility)
                    if (data.getEnemyManager() != null) {
                        copyEnemyManagerState(data.getEnemyManager());
                    } else {
                        // Old save without enemy data - initialize defaults
                        enemyManager.spawnEnemies(EnemyFactory.createDefaultEnemies());
                    }
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
                    data = ResourceManager.createSaveData(player, enemyManager);
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
        Enemy enemy = enemyManager.findEnemyAt(coordinates.x(), coordinates.y());

        if (enemy == null) {
            return; // No enemy found
        }

        BattleResult battleResult = battleController.runBattle(player, enemy);

        if (battleResult.playerDied()) {
            gameState.triggerGameOver();
        }

        if (battleResult.enemyDied()) {
            enemyManager.removeEnemy(enemy);

            if (ZORAM.equals(enemy.getName())) {
                gameState.defeatZoram();
            }

            UserInterface.renderMessages(battleResult.messages());
        }
    }

    private void putCharacterAndNpcsIntoMap(Player player) {
        map.putEntityInMap(player.getPlayerPosition().getX(), player.getPlayerPosition().getY(), PLAYER_CHARACTER);
        for (Enemy enemy : enemyManager.getActiveEnemies()) {
            map.putEntityInMap(enemy.getXPosition(), enemy.getYPosition(), enemy.getIcon());
        }
    }

    private static void exitGame() {
        UserInterface.renderMessages(List.of("Until next time!"));
        System.exit(0);
    }

    private void copyEnemyManagerState(EnemyManager loadedManager) {
        enemyManager.spawnEnemies(loadedManager.getActiveEnemies());
    }

}
