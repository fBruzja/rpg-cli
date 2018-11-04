package com.rpg.map;

import static org.junit.Assert.assertEquals;
import org.junit.*;

import com.rpg.map.Map;
import com.rpg.characters.Enemy;

public class MapTest {
	
	@Test
	public void testBringPlayerToMap() {
		Map map = new Map();
		map.bringPlayerIntoMap(20, 20);
		assertEquals('X', map.map[20][20]);
	}
	
	@Test
	public void testBringMonsterToMap() {
		Map map = new Map();
		map.bringMonsterToMap(20, 20, "Goblin");
		assertEquals('G', map.map[20][20]);
	}
	
	@Test
	public void testCheckIfOutOfBoundariesTrue() {
		Map map = new Map();
		assertEquals(true, map.checkIfOutOfBoundaries(20, 20));
	}
	
	@Test
	public void testCheckIfOutOfBoundariesFalse() {
		Map map = new Map();
		assertEquals(true, map.checkIfOutOfBoundaries(39, 39));
	}
	
	@Test
	public void testUpdateMap() {
		Map map = new Map();
		
		map.updateMap(20, 20, 'w');
		assertEquals(' ', map.map[21][20]);
		assertEquals('X', map.map[20][20]);
	}
	
	@Test
	public void testCheckForEncounter() {
		Map map = new Map();
		Enemy e = new Enemy(10, 10, 10, 10, "Goblin", false);
		map.bringMonsterToMap(e.getxPosition(), e.getyPosition(), e.getName());
		assertEquals('G', map.checkForEncounter(e.getxPosition(), e.getyPosition()));
	}
}
