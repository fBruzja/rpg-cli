package com.rpg.game;

import com.rpg.characters.Enemy;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

/**
 * Manages the lifecycle of enemies in the game world.
 * Handles spawning, tracking, and persistence of enemy state.
 */
public class EnemyManager implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<EnemyInstance> activeEnemies;

    public EnemyManager() {
        this.activeEnemies = new ArrayList<>();
    }

    public void spawnEnemies(List<Enemy> enemyTemplates) {
        for (Enemy enemy : enemyTemplates) {
            String uniqueId = UUID.randomUUID().toString();
            activeEnemies.add(new EnemyInstance(uniqueId, enemy));
        }
    }

    public Enemy findEnemyAt(int x, int y) {
        for (EnemyInstance instance : activeEnemies) {
            Enemy enemy = instance.getEnemy();
            if (enemy.getXPosition() == x && enemy.getYPosition() == y) {
                return enemy;
            }
        }
        return null;
    }

    public void removeEnemy(Enemy enemy) {
        activeEnemies.removeIf(instance -> instance.getEnemy() == enemy);
    }

    public List<Enemy> getActiveEnemies() {
        return activeEnemies.stream()
                .map(EnemyInstance::getEnemy)
                .toList();
    }

    @Getter
    private static class EnemyInstance implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private final String id;
        private final Enemy enemy;

        public EnemyInstance(String id, Enemy enemy) {
            this.id = id;
            this.enemy = enemy;
        }

    }
}