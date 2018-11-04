package com.rpg.characters;

import static org.junit.Assert.assertEquals;
import org.junit.*;

public class PlayerTest {
	
	@Test
	public void testPhysicalAttackWithPoison() {
		Player player = new Player("MainTestPlayer", "Female", 't');
		Enemy e = new Enemy(10, 10, 10, 10, "TestMonster", false);
		assertEquals(false, player.physicalAtatck(player, e, true));
	}
	
	@Test
	public void testPhysicalAttackWithoutPoison() {
		Player player = new Player("MainTestPlayer", "Female", 't');
		Enemy e = new Enemy(10, 10, 10, 10, "TestMonster", false);
		assertEquals(false, player.physicalAtatck(player, e, false));
	}
	
	@Test
	public void testFirstAbilityUsageThatPoisonsTheAttack() {
		/* Creating a thief character that uses Poison Dagger */
		Player player = new Player("MainTestPlayer", "Female", 't');
		Enemy e = new Enemy(10, 10, 10, 10, "TestMonster", false);
		assertEquals(true, player.firstAbilityUsage(player, e));
	}
	
	@Test 
	public void testFirstAbilityUsageThatDoesNotPoisonTheAttack() {
		/* Creating a character that does not use Poison Dagger */
		Player player = new Player("MainTestPlayer", "Female", 'w');
		Enemy e = new Enemy(10, 10, 10, 10, "TestMonster", false);
		assertEquals(false, player.firstAbilityUsage(player, e));
	}
	
	@Test
	public void testThirdAbilityUsageThatUsesDisable() {
		/* Creating a character that uses a disabling skill */
		Player player = new Player("MainTestPlayer", "Male", 'w');
		Enemy e = new Enemy(10, 10, 10, 10, "TestMonster", false);
		assertEquals(true, player.thirdAbilityUsage(player, e));
	}
	
	@Test
	public void testThirdAbilityUsageThatDoesNotUseDisable() {
		/* Creating a character that does not use a disabling skill */
		Player player = new Player("MainTestPlayer", "Male", 't');
		Enemy e = new Enemy(10, 10, 10, 10, "TestMonster", false);
		assertEquals(false, player.thirdAbilityUsage(player, e));
	}
	
	@Test
	public void testCalculateDamageDoneByPlayerAdditionCaseNotNegative() {
		/* player will do 5 damage to the enemy */
		Player player = new Player("MainTestPlayer", "Male", 't'); 
		Enemy e = new Enemy(10, 10, 10, 0, "TestMonster", false); // defense 0
		assertEquals(5, player.calculateDamageDoneByPlayer(player, e, 0, true));
	}
	
	@Test
	public void testCalculateDamageDoneByPlayerAdditionCaseNegative() {
		/* player will do negative damage to the enemy, therefore returning 0 */
		Player player = new Player("MainTestPlayer", "Male", 't'); 
		Enemy e = new Enemy(10, 10, 10, 10, "TestMonster", false); 
		assertEquals(0, player.calculateDamageDoneByPlayer(player, e, 0, true));
	}
	
	@Test
	public void testCalculateDamageDoneByPlayerMultiplyingCaseNotNegative() {
		/* player will do 5 damage to the enemy */
		Player player = new Player("MainTestPlayer", "Male", 't'); 
		Enemy e = new Enemy(10, 10, 10, 0, "TestMonster", false); // defense 0
		assertEquals(5, player.calculateDamageDoneByPlayer(player, e, 1, false));
	}
	
	@Test
	public void testCalculateDamageDoneByPlayerMultiplyingCaseNegative() {
		/* player will do negative damage to the enemy, therefore returning 0 */
		Player player = new Player("MainTestPlayer", "Male", 't'); 
		Enemy e = new Enemy(10, 10, 10, 10, "TestMonster", false);
		assertEquals(0, player.calculateDamageDoneByPlayer(player, e, 1, false));
	}
}
