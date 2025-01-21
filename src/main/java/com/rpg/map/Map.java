package com.rpg.map;

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
		for(int i = 1; i < gameMap[0].length - 1; i++) {
			gameMap[0][i] = '~';
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

	public void putPlayerInGameMap(int x, int y) {
		gameMap[x][y] = 'X';
	}
	
	public void bringMonsterToMap(int x, int y, String monster) {
		switch(monster) {
			case "Goblin" :
				gameMap[x][y] = 'G';
				break;
			case "Skeleton" :
				gameMap[x][y] = 'S';
				break;
			case "Rat-Man" :
				gameMap[x][y] = 'R';
				break;
			case "Salamander" :
				gameMap[x][y] = 'D';
				break;
			case "Kobold" :
				gameMap[x][y] = 'K';
				break;
			case "Spectre" :
				gameMap[x][y] = 'T';
				break;
			case "Zoram" :
				gameMap[x][y] = 'Z';
				break;
		}
	}
	
	public void printMap() {
		for(int i=0; i < gameMap[0].length; i++) {
			for(int j=0; j < gameMap[i].length; j++) {
				System.out.print(gameMap[i][j]);
			}
			System.out.println();
		}
	}
	
	public boolean checkIfOutOfBoundaries(int x, int y) {
        return gameMap[x][y] != '|' && gameMap[x][y] != '~' && gameMap[x][y] != '_';
    }
	
	public void updateMap(int newX, int newY, char movement) {
		switch(movement) {
			case 'w': 
				gameMap[newX + 1][newY] = ' ';
				break;
			case 'a':
				gameMap[newX][newY + 1] = ' ';
				break;
			case 'd': 
				gameMap[newX][newY - 1] = ' ';
				break;
			case 's':
				gameMap[newX - 1][newY] = ' ';
				break;
			default: break;
		}
		gameMap[newX][newY] = 'X';
	}

	// TODO: encounters feel wonky. Perhaps handle them differently
	public char checkForEncounter(int x, int y) {
        return switch (gameMap[x][y]) {
            case 'G' -> 'G';
            case 'S' -> 'S';
            case 'R' -> 'R';
            case 'K' -> 'K';
            case 'D' -> 'D';
            case 'Z' -> 'Z';
            case 'T' -> 'T';
            default -> ' ';
        };
	}
}
