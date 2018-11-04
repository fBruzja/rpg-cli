package com.rpg.datamanagement;

import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.rpg.characters.Player;

public class ResourceManager {
	public static void save(Serializable data, String filename) throws Exception {
		try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(filename)))) {
			oos.writeObject(data);
		}
	}
	
	public static Object load(String filename) throws Exception {
		try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(filename)))) {
			return ois.readObject();
		}
	}
	
	public static SaveData createSaveData(Player pl) {
		SaveData data = new SaveData();
		
		data.name = pl.getName();
		data.gender = pl.getGender();
		data.profession = pl.getProfession();
		data.level = pl.getLevel();
		data.xPosition = pl.getxPosition();
		data.yPosition = pl.getyPosition();
		data.strength = pl.getStrength();
		data.agility = pl.getAgility();
		data.magicka = pl.getMagicka();
		data.healthPoints = pl.getHealthPoints();
		data.manaPoints = pl.getManaPoints();
		data.attackPoints = pl.getAttackPoints();
		data.defense = pl.getDefense();
		data.abilities = pl.getAbilities();
		data.exp = pl.getExp();
		
		return data;
	}
	
	public static void loadTheDataInThePlayer(SaveData s, Player p) {
		p.setAbilities(s.abilities);
		p.setAgility(s.agility);
		p.setAttackPoints(s.attackPoints);
		p.setDefense(s.defense);
		p.setHealthPoints(s.healthPoints);
		p.setManaPoints(s.manaPoints);
		p.setStrength(s.strength);
		p.setMagicka(s.magicka);
		p.setxPosition(s.xPosition);
		p.setyPosition(s.yPosition);
		p.setLevel(s.level);
		p.setExp(s.exp);
	}
}
