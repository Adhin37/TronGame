import java.util.*;
import java.io.*;
import java.math.*;

class LightRunner {
	private int id;
	private int movesPossible;
	private int beginningX;
	private int beginningY;
	private final ArrayList<Integer> roadmapX = new ArrayList<Integer>();
	private final ArrayList<Integer> roadmapY = new ArrayList<Integer>();

	public LightRunner(final int id, final int beginningX, final int beginningY, final int currentX,
			final int currentY) {
		this.id = id;
		this.movesPossible = 0;
		this.beginningX = beginningX;
		this.beginningY = beginningY;
		this.setCurrentX(currentX);
		this.setCurrentY(currentY);
	}

	public int getId() {
		return id;
	}

	public int getMovesPossible() {
		return movesPossible;
	}

	public void setMovesPossible(final int movesPossible) {
		this.movesPossible = movesPossible;
	}

	public int getBeginningX() {
		return beginningX;
	}

	public void setBeginningX(final int beginningX) {
		this.beginningX = beginningX;
	}

	public int getBeginningY() {
		return beginningY;
	}

	public void setBeginningY(final int beginningY) {
		this.beginningY = beginningY;
	}

	public ArrayList<Integer> getRoadmapX() {
		return roadmapX;
	}

	public int getCurrentX() {
		return roadmapX.get(roadmapX.size() - 1);
	}

	public void setCurrentX(final int currentX) {
		this.roadmapX.add(currentX);
	}

	public ArrayList<Integer> getRoadmapY() {
		return roadmapY;
	}

	public int getCurrentY() {
		return roadmapY.get(roadmapY.size() - 1);
	}

	public void setCurrentY(final int currentY) {
		this.roadmapY.add(currentY);
	}

}

class GameMap {
	private int[][] map;
	private int maxY;
	private int maxX;

	public GameMap(int maxY, int maxX) {
		this.maxY = maxY;
		this.maxX = maxX;
		this.map = new int[maxY][maxX];
		this.iniMap();
	}

	public int[][] getMap() {
		return map;
	}

	public void editMap(int playerId, int editX, int editY) {
		for (int cptY = 0; cptY < this.maxY; cptY++) {
			for (int cptX = 0; cptX < this.maxX; cptX++) {
				if (editY == cptY && editX == cptX) {
					map[cptY][cptX] = playerId;
				}
			}
		}
	}

	public void showMap() {
		for (int cptY = 0; cptY < this.maxY; cptY++) {
			System.err.print('|');
			for (int cptX = 0; cptX < this.maxX; cptX++) {
				if (map[cptY][cptX] == -2) {
					System.err.print('X');
				} else if (map[cptY][cptX] != -1) {
					System.err.print(map[cptY][cptX]);
				} else {
					System.err.print('-');
				}
			}
			System.err.print('|');
			System.err.println();
		}
		System.err.println();
	}

	public HashMap<Integer, Integer> whatsNear(int posY, int posX) {
		HashMap<Integer, Integer> nearPositions = new HashMap<Integer, Integer>();
		nearPositions.put(0, map[posY - 1][posX]); // Up
		nearPositions.put(1, map[posY + 1][posX]); // Down
		nearPositions.put(2, map[posY][posX - 1]); // Left
		nearPositions.put(3, map[posY][posX + 1]); // Right
		return nearPositions;
	}

	public int calculMoves(int[][] map, final int playerCptY, final int playerCptX) {
		int maxMoves;
		maxMoves = 0;
		if ((playerCptY) >= 0 && (playerCptY) < this.getMaxY() && (playerCptX) >= 0 && (playerCptX) < this.getMaxX()
				&& map[playerCptY][playerCptX] == -1) {
			map[playerCptY][playerCptX] = -3;
			maxMoves += calculMoves(map, playerCptY, playerCptX) + 1;
		}
		if ((playerCptY - 1) >= 0 && map[playerCptY - 1][playerCptX] == -1) {
			map[playerCptY - 1][playerCptX] = -3;
			maxMoves += calculMoves(map, playerCptY - 1, playerCptX) + 1;
		}
		if ((playerCptY + 1) < this.getMaxY() && map[playerCptY + 1][playerCptX] == -1) {
			map[playerCptY + 1][playerCptX] = -3;
			maxMoves += calculMoves(map, playerCptY, playerCptX) + 1;
		}
		if ((playerCptX - 1) >= 0 && map[playerCptY][playerCptX - 1] == -1) {
			map[playerCptY][playerCptX - 1] = -3;
			maxMoves += calculMoves(map, playerCptY, playerCptX - 1) + 1;
		}
		if ((playerCptX + 1) < this.getMaxX() && map[playerCptY][playerCptX + 1] == -1) {
			map[playerCptY][playerCptX + 1] = -3;
			maxMoves += calculMoves(map, playerCptY, playerCptX + 1) + 1;
		}
		return maxMoves;
	}

	private void iniMap() {
		for (int cptY = 0; cptY < this.maxY; cptY++) {
			for (int cptX = 0; cptX < this.maxX; cptX++) {
				if (cptY == 0 || cptY + 1 == this.maxY || cptX == 0 || cptX + 1 == this.maxX) {
					map[cptY][cptX] = -2; // Limit map
				} else {
					map[cptY][cptX] = -1; // No player
				}
			}
		}
	}

	public int[][] multiArrayCopy(int[][] source) {
		int[][] destination = new int[source[0].length][source[1].length];
		for (int i = 0; i < source.length; i++) {
			destination[i] = source[i].clone();
		}
		return destination;
	}

	public int getMaxX() {
		return maxY;
	}

	public int getMaxY() {
		return maxX;
	}

}

class GameOrder {
	private GameMap tronMap;
	private int myLightRunnerID;
	private ArrayList<LightRunner> allPlayers;

	public GameOrder() {
	}

	public void iniOrder(final int myLightRunnerID, final ArrayList<LightRunner> allPlayers, final GameMap tronMap) {
		this.myLightRunnerID = myLightRunnerID;
		this.tronMap = tronMap;
		this.allPlayers = allPlayers;
	}

	public String makeBestMove() {
		return this.followOrder(this.calcBestMove(this.myLightRunnerID));
	}

	// private int score() {
	// 	int score = 0;
	// 	if (game.win?(@player)){
	// 	score= 10;
	// 	}
	// 	else if (game.win?(@opponent)){
	// 	score= -10;
	// 	}
	// 	return score;
	// }

	private int predictFuture(final ArrayList<Integer> bestMoveArray, final int currentY, final int currentX) {
		int bestFuture;
		bestFuture = -1;
		if (bestMoveArray.isEmpty()) {
			return bestFuture;
		}
		int[] possibilities;
		int up, down, left, right;
		int max;
		up = 0;
		down = 0;
		left = 0;
		right = 0;
		max = 0;
		if (bestMoveArray.contains(0)) {
			up = tronMap.calculMoves(tronMap.multiArrayCopy(tronMap.getMap()), currentY - 1, currentX);
		} else {
			up = 0;
		}
		if (bestMoveArray.contains(1)) {
			down = tronMap.calculMoves(tronMap.multiArrayCopy(tronMap.getMap()), currentY + 1, currentX);
		} else {
			down = 0;
		}
		if (bestMoveArray.contains(2)) {
			left = tronMap.calculMoves(tronMap.multiArrayCopy(tronMap.getMap()), currentY, currentX - 1);
		} else {
			left = 0;
		}
		if (bestMoveArray.contains(3)) {
			right = tronMap.calculMoves(tronMap.multiArrayCopy(tronMap.getMap()), currentY, currentX + 1);
		} else {
			right = 0;
		}

		possibilities = new int[] { up, down, left, right };
		for (int counter = 0; counter < possibilities.length; counter++) {
			if (possibilities[counter] > max) {
				System.err.println("possibilities:" + possibilities[counter]);
				max = possibilities[counter];
				bestFuture = counter;
			}
		}
		return bestFuture;
	}

	private int calcBestMove(int lightRunnerID) {
		int bestMove;
		int currentY, currentX;
		int sizeArray;
		bestMove = -1;
		currentY = 0;
		currentX = 0;
		sizeArray = 0;

		HashMap<Integer, Integer> nearPositions;
		ArrayList<Integer> bestMoveArray = new ArrayList<Integer>();
		currentY = this.allPlayers.get(lightRunnerID).getCurrentY();
		currentX = this.allPlayers.get(lightRunnerID).getCurrentX();
		nearPositions = this.tronMap.whatsNear(currentY, currentX);
		sizeArray = nearPositions.size();

		for (int cpt = sizeArray - 1; cpt >= 0; cpt--) {
			if (nearPositions.get(cpt) == -1) {
				bestMoveArray.add(cpt);
			}
		}
		for (int move : bestMoveArray) {
			System.err.println("array:" + move);
		}
		bestMove = predictFuture(bestMoveArray, currentY, currentX);
		System.err.println(bestMove);
		return bestMove;
	}

	private String followOrder(final int choose) {
		String order;
		switch (choose) {
		case 0:
			order = this.GoUp();
			break;
		case 1:
			order = this.GoDown();
			break;
		case 2:
			order = this.GoLeft();
			break;
		case 3:
			order = this.GoRight();
			break;
		default:
			Random rand = new Random();
			order = followOrder(rand.nextInt(4) + 1);
			break;
		}
		return order;
	}

	private String GoUp() {
		return "UP";
	}

	private String GoDown() {
		return "DOWN";
	}

	private String GoLeft() {
		return "LEFT";
	}

	private String GoRight() {
		return "RIGHT";
	}
}

class Player {

	public static void main(String[] args) {
		final int maxY = 20 + 2;
		final int maxX = 30 + 2;
		final GameOrder tronOrder = new GameOrder();
		final GameMap tronMap = new GameMap(maxY, maxX);
		final ArrayList<LightRunner> allPlayers = new ArrayList<LightRunner>();

		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		while (true) {
			int nbLightRunner = in.nextInt(); // total number of players (2 to 4).
			int myLightRunnerID = in.nextInt(); // your player number (0 to 3).
			if (allPlayers.isEmpty()) {
				for (int cptLightRunner = 0; cptLightRunner < nbLightRunner; cptLightRunner++) {
					allPlayers.add(new LightRunner(cptLightRunner, 0, 0, 0, 0));
				}
			}
			for (int cptLightRunner = 0; cptLightRunner < nbLightRunner; cptLightRunner++) {
				int beginningX = in.nextInt() + 1;
				int beginningY = in.nextInt() + 1;
				int currentX = in.nextInt() + 1;
				int currentY = in.nextInt() + 1;
				allPlayers.get(cptLightRunner).setBeginningX(beginningX);
				allPlayers.get(cptLightRunner).setBeginningY(beginningY);
				allPlayers.get(cptLightRunner).setCurrentX(currentX);
				allPlayers.get(cptLightRunner).setCurrentY(currentY);
				tronMap.editMap(cptLightRunner, currentX, currentY);
			}
			tronMap.showMap();
			tronOrder.iniOrder(myLightRunnerID, allPlayers, tronMap);
			System.out.println(tronOrder.makeBestMove());
		}
	}
}