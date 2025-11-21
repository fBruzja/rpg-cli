package com.rpg.map;

import com.rpg.characters.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Map {
	private char[][] gameMap = new char[40][40];

	public Map() {
		generateGameMapLayout();
	}

	public void generateGameMapLayout() {
		fillUpGameMapWithSpace();
		createGameMapBorders();
	}

	private void createGameMapBorders() {
		for(int i = 1; i < gameMap[0].length; i++) {
			gameMap[0][i] = '_';
			gameMap[39][i] = '_';
			gameMap[i][0] = '|';
			gameMap[i][39] = '|';
		}
	}

	private void fillUpGameMapWithSpace() {
		for(int i = 0; i < gameMap[0].length - 1; i++) {
			for(int j = 0; j < gameMap.length - 1; j++) {
				gameMap[j][i] = ' ';
			}
		}
	}

	public void putEntityInMap(int x, int y, char entity) {
		gameMap[x][y] = entity;
	}
	
	public boolean checkIfOutOfBoundaries(int x, int y) {
        char tile = checkTile(x, y);
        boolean coordinatesOutOfBounds = x < 0 || x > 39 || y < 0 || y > 39;
        boolean isCharacterTryingToHitWalls = tile == '|' && tile == '~' && tile == '_';
        return coordinatesOutOfBounds || isCharacterTryingToHitWalls;
    }
	
	public void updateMap(int newX, int newY, char movement) {
		switch(movement) {
			case 'w':
                putEntityInMap(newX + 1, newY, ' ');
				break;
			case 'a':
                putEntityInMap(newX, newY + 1, ' ');
				break;
			case 'd':
                putEntityInMap(newX, newY - 1, ' ');
				break;
			case 's':
                putEntityInMap(newX - 1, newY, ' ');
				break;
			default: break;
		}
        putEntityInMap(newX, newY, Player.PLAYER_SYMBOL);
	}

	public char checkTile(int x, int y) {
        return gameMap[x][y];
	}
}
