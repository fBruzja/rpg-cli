package com.rpg.datamanagement;

import com.rpg.characters.Player;
import com.rpg.datamanagement.data.PersonalPlayerInformation;
import com.rpg.datamanagement.data.Position;
import com.rpg.datamanagement.data.SaveData;
import com.rpg.datamanagement.data.Stats;
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

    public static SaveData createSaveData(Player pl) {
        var playerInformation = new PersonalPlayerInformation(pl.getName(), pl.getGender(), pl.getProfession());
        var position = new Position(pl.getXPosition(), pl.getYPosition());

        return SaveData.builder()
                .playerInformation(playerInformation)
                .stats(getStats(pl))
                .abilities(pl.getAbilities())
                .level(pl.getLevel())
                .position(position)
                .build();
    }

    private static Stats getStats(Player pl) {
        return Stats.builder()
                .agility(pl.getAgility())
                .strength(pl.getStrength())
                .magicka(pl.getMagicka())
                .attackPoints(pl.getAttackPoints())
                .defense(pl.getDefense())
                .exp(pl.getExp())
                .manaPoints(pl.getManaPoints())
                .healthPoints(pl.getHealthPoints())
                .build();
    }

    public static void loadTheDataInThePlayer(SaveData s, Player p) {
        p.setAbilities(s.getAbilities());
        p.setAgility(s.getStats().getAgility());
        p.setAttackPoints(s.getStats().getAttackPoints());
        p.setDefense(s.getStats().getDefense());
        p.setHealthPoints(s.getStats().getHealthPoints());
        p.setManaPoints(s.getStats().getManaPoints());
        p.setStrength(s.getStats().getStrength());
        p.setMagicka(s.getStats().getMagicka());
        p.setXPosition(s.getPosition().x());
        p.setYPosition(s.getPosition().y());
        p.setLevel(s.getLevel());
        p.setExp(s.getStats().getExp());
    }

}
