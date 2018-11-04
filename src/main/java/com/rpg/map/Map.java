package com.rpg.map;

public class Map {
	public char[][] map = new char[40][40];
	int i,j;
	
	public void generateMapLayout() {
		for(i=1; i < map[0].length - 1; i++) {
			map[0][i] = '~';
			map[39][i] = '_';
			map[i][0] = '|';
			map[i][39] = '|';
		}
	}
	
	public void bringPlayerIntoMap(int x, int y) {
		map[x][y] = 'X';
	}
	
	public void bringMonsterToMap(int x, int y, String monster) {
		switch(monster) {
			case "Goblin" :
				map[x][y] = 'G';
				break;
			case "Skeleton" :
				map[x][y] = 'S';
				break;
			case "Rat-Man" :
				map[x][y] = 'R';
				break;
			case "Salamander" :
				map[x][y] = 'D';
				break;
			case "Kobold" :
				map[x][y] = 'K';
				break;
			case "Spectre" :
				map[x][y] = 'T';
				break;
			case "Zoram" :
				map[x][y] = 'Z';
				break;
		}
	}
	
	public void printMap() {
		for(i=0; i<map[0].length; i++) {
			for(j=0; j<map[i].length; j++) {
				System.out.print(map[i][j]);
			}
			System.out.println();
		}
	}
	
	public boolean checkIfOutOfBoundaries(int x, int y) {
		if(map[x][y] == '|' || map[x][y] == '~' || map[x][y] == '_')
			return false;
		return true;
	}
	
	public void updateMap(int newX, int newY, char movement) {
		switch(movement) {
			case 'w': 
				map[newX + 1][newY] = ' ';
				break;
			case 'a':
				map[newX][newY + 1] = ' ';
				break;
			case 'd': 
				map[newX][newY - 1] = ' ';
				break;
			case 's':
				map[newX - 1][newY] = ' ';
				break;
		}
		map[newX][newY] = 'X';
	}
	
	public char checkForEncounter(int x, int y) {
		switch(map[x][y]) {
			case 'G':
				return 'G';
			case 'S':
				return 'S';
			case 'R':
				return 'R';
			case 'K':
				return 'K';
			case 'D':
				return 'D';
			case 'Z':
				return 'Z';
			case 'T':
				return 'T';
		}

		return ' ';
	}
}
