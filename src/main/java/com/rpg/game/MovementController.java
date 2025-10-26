package com.rpg.game;

import com.rpg.characters.Enemy;
import com.rpg.characters.Player;
import com.rpg.game.outcome.MovementResult;
import com.rpg.map.Coordinates;
import com.rpg.map.Map;
import java.util.List;

public class MovementController {

    private final Map map;

    public MovementController(Map map) {
        this.map = map;
    }

    public MovementResult attemptMove(Player player, char movement) {
        int x = player.getPlayerPosition().getX();
        int y = player.getPlayerPosition().getY();

        Coordinates target = computeTargetCoordinates(x, y, movement);
        if (target == null) {
            return MovementResult.invalidDirection();
        }

        if (!map.checkIfOutOfBoundaries(target.x(), target.y())) {
            return MovementResult.outOfBounds();
        }

        char encounter = map.checkTile(target.x(), target.y());

        map.updateMap(target.x(), target.y(), movement);
        player.move(movement);

        return MovementResult.success(target, encounter);
    }

    private Coordinates computeTargetCoordinates(int x, int y, char movement) {
        return switch (movement) {
            case 'w' -> new Coordinates(x - 1, y);
            case 'a' -> new Coordinates(x, y - 1);
            case 'd' -> new Coordinates(x, y + 1);
            case 's' -> new Coordinates(x + 1, y);
            default -> null;
        };
    }
}
