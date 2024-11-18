package com.rpg.characters;

import static org.junit.Assert.assertEquals;
import org.junit.*;

public class EnemyTest {
	@Test
	public void testCalculateAndApplyDamageNotNegative() {
		/* Monster will do 5 damage to the enemy */
		Player player = new Player("MainTestPlayer", "Male", 't'); // defense 5
		Enemy e = new Enemy(10, 10, 10, 10, "TestMonster", false);
		assertEquals(8, e.calculateAndApplyDamage(player, e, 0));
	}
	
	@Test
	public void testCalculateAndApplyDamageNegative() {
		/* Monster will do 0 damage to the enemy since the damage would be negative */
		Player player = new Player("MainTestPlayer", "Male", 't'); // defense 5
		Enemy e = new Enemy(10, 10, 1, 10, "TestMonster", false);
		assertEquals(0, e.calculateAndApplyDamage(player, e, 0));
	}
}
