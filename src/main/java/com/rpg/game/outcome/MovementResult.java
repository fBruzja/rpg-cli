package com.rpg.game.outcome;

import com.rpg.map.Coordinates;
import lombok.Getter;

@Getter
public class MovementResult {

    private final MovementStatus status;
    private final Coordinates targetCoordinates;
    private final char encounteredTile;
    private final String message;

    private MovementResult(MovementStatus status, Coordinates targetCoordinates,
            char encounteredTile, String message) {
        this.status = status;
        this.targetCoordinates = targetCoordinates;
        this.encounteredTile = encounteredTile;
        this.message = message;
    }

    public static MovementResult success(Coordinates target, char encounter) {
        return new MovementResult(MovementStatus.SUCCESS, target, encounter, null);
    }

    public static MovementResult invalidDirection() {
        return new MovementResult(MovementStatus.INVALID_DIRECTION, null, ' ',
                "Unknown movement. Use w/a/s/d.");
    }

    public static MovementResult outOfBounds() {
        return new MovementResult(MovementStatus.OUT_OF_BOUNDS, null, ' ',
                "You cannot move there.");
    }

    public boolean isSuccessful() {
        return status == MovementStatus.SUCCESS;
    }

    public boolean hasEncounter() {
        return isSuccessful() && encounteredTile != ' ';
    }

    public enum MovementStatus {
        SUCCESS,
        INVALID_DIRECTION,
        OUT_OF_BOUNDS
    }
}
