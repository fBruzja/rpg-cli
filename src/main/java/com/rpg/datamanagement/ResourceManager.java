package com.rpg.datamanagement;

import com.rpg.characters.Player;
import com.rpg.characters.data.PersonalPlayerInformation;
import com.rpg.characters.data.Position;
import com.rpg.characters.data.Stats;
import com.rpg.datamanagement.data.SaveData;
import com.rpg.game.EnemyManager;
import com.rpg.utils.GameLogger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceManager {

    private ResourceManager() {
    }

    private static final Logger log = LoggerFactory.getLogger(ResourceManager.class);

    public static void save(Serializable data, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(filename)))) {
            oos.writeObject(data);
        } catch (IOException exception) {
            log.info(exception.getMessage());
        }
    }

    public static Object load(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(filename)))) {
            return ois.readObject();
        } catch (IOException exception) {
            GameLogger.print("Could not load save file: " + filename);
        } catch (ClassNotFoundException exception) {
            log.error(exception.getMessage());
        }
        return null;
    }

    public static SaveData createSaveData(Player pl, EnemyManager enemyManager) {
        var playerInfo = pl.getPlayerInformation();
        var playerPosition = pl.getPlayerPosition();

        var playerInformation = new PersonalPlayerInformation(playerInfo.name(), playerInfo.profession());
        var position = new Position(playerPosition.getX(), playerPosition.getY());

        return SaveData.builder()
                .playerInformation(playerInformation)
                .stats(getStats(pl))
                .abilities(pl.abilitiesFacade())
                .level(pl.getLevel())
                .position(position)
                .enemyManager(enemyManager)
                .build();
    }

    private static Stats getStats(Player pl) {
        var stats = pl.getPlayerStats();

        return Stats.builder()
                .agility(stats.getAgility())
                .strength(stats.getStrength())
                .intelligence(stats.getIntelligence())
                .attackPoints(stats.getAttackPoints())
                .defense(stats.getDefense())
                .exp(stats.getExp())
                .manaPoints(stats.getManaPoints())
                .healthPoints(stats.getHealthPoints())
                .build();
    }

    public static void loadTheDataInThePlayer(SaveData s, Player p) {
        var savedStats = s.getStats();

        var stats = Stats.builder()
                .agility(savedStats.getAgility())
                .strength(savedStats.getStrength())
                .intelligence(savedStats.getIntelligence())
                .healthPoints(savedStats.getHealthPoints())
                .defense(savedStats.getDefense())
                .exp(savedStats.getExp())
                .manaPoints(savedStats.getManaPoints())
                .attackPoints(savedStats.getAttackPoints())
                .build();

        var position = new Position(s.getPosition().getX(), s.getPosition().getY());

        p.setPlayerStats(stats);

        if (s.getAbilities() != null) {
            p.setPlayerAbilities(s.getAbilities());
        }

        p.setPlayerPosition(position);
        p.setLevel(s.getLevel());
    }

}