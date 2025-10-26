package com.rpg.game;

import com.rpg.characters.Enemy;
import java.util.ArrayList;
import java.util.List;

public class EnemyFactory {

    public static final String ZORAM = "Zoram";

    private EnemyFactory() {
    }

    public static List<Enemy> createDefaultEnemies() {
        List<Enemy> enemies = new ArrayList<>();

        // Regular enemies (random positions)
        enemies.add(new Enemy(20, 20, 5, 2, 'G', "Goblin"));
        enemies.add(new Enemy(20, 20, 5, 2, 'G', "Goblin"));
        enemies.add(new Enemy(25, 20, 6, 3, 'S', "Skeleton"));
        enemies.add(new Enemy(25, 20, 6, 3, 'S', "Skeleton"));
        enemies.add(new Enemy(23, 30, 5, 4, 'R', "Rat-Man"));
        enemies.add(new Enemy(23, 30, 5, 4, 'R', "Rat-Man"));
        enemies.add(new Enemy(35, 40, 6, 5, 'D', "Salamander"));
        enemies.add(new Enemy(35, 40, 6, 5, 'D', "Salamander"));
        enemies.add(new Enemy(30, 35, 6, 5, 'K', "Kobold"));
        enemies.add(new Enemy(30, 35, 6, 5, 'K', "Kobold"));
        enemies.add(new Enemy(35, 100, 7, 5, 'P', "Spectre"));

        // Boss (random position)
        enemies.add(new Enemy(70, 100, 10, 10, 'Z', ZORAM));

        return enemies;
    }

    public static Enemy createEnemy(EnemyType type) {
        return switch (type) {
            case GOBLIN -> new Enemy(20, 20, 5, 2, 'G', "Goblin");
            case SKELETON -> new Enemy(25, 20, 6, 3, 'S', "Skeleton");
            case RAT_MAN -> new Enemy(23, 30, 5, 4, 'R', "Rat-Man");
            case SALAMANDER -> new Enemy(35, 40, 6, 5, 'D', "Salamander");
            case KOBOLD -> new Enemy(30, 35, 6, 5, 'K', "Kobold");
            case SPECTRE -> new Enemy(35, 100, 7, 5, 'P', "Spectre");
            case ZORAM -> new Enemy(70, 100, 10, 10, 'Z', ZORAM);
        };
    }

    public enum EnemyType {
        GOBLIN,
        SKELETON,
        RAT_MAN,
        SALAMANDER,
        KOBOLD,
        SPECTRE,
        ZORAM
    }
}
