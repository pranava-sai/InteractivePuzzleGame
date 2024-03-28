package hw3;

import java.util.ArrayList;
import java.util.Random;

import api.ScoreUpdateListener;
import api.ShowDialogListener;
import api.Tile;

/**
 * Class that models a game.
 */
/**
 * 
 * @author SubrahmanyaSreePranavaSaiMaganti
 *
 */
public class ConnectGame {
	/**
	 * dialogListener variable of type ShowDialogListener
	 */
	private ShowDialogListener dialogListener;
	/**
	 * scoreListener variable of type ScoreUpdateListener for updating the score
	 */
	private ScoreUpdateListener scoreListener;
	/**
	 * grid variable of type Grid to create a new Grid
	 */
	private Grid grid;
	/**
	 * minTileLevel variable to keep track of the tile at the minimum level
	 */
	private int minTileLevel;
	/**
	 * maxTileLevel variable to keep track of the tile at the maximum level
	 */
	private int maxTileLevel;
	/**
	 * score variable to keep track of the score
	 */
	private long score;
	/**
	 * a random variable of type Random to generate numbers randomly
	 */
	private Random rand;
	/**
	 * An ArrayList selected of type Tile is to keep track of the tils that are
	 * selected
	 */
	private ArrayList<Tile> selected;

	/**
	 * Constructs a new ConnectGame object with given grid dimensions and minimum
	 * and maximum tile levels.
	 * 
	 * @param width  grid width
	 * @param height grid height
	 * @param min    minimum tile level
	 * @param max    maximum tile level
	 * @param rand   random number generator
	 */
	public ConnectGame(int width, int height, int min, int max, Random rand) {
		grid = new Grid(width, height);
		minTileLevel = min;
		maxTileLevel = max;
		score = 0;
		this.rand = rand;
		selected = new ArrayList<Tile>();
	}

	/**
	 * Gets a random tile with level between minimum tile level inclusive and
	 * maximum tile level exclusive. For example, if minimum is 1 and maximum is 4,
	 * the random tile can be either 1, 2, or 3.
	 * <p>
	 * DO NOT RETURN TILES WITH MAXIMUM LEVEL
	 * 
	 * @return a tile with random level between minimum inclusive and maximum
	 *         exclusive
	 */
	public Tile getRandomTile() {
		int level = rand.nextInt(maxTileLevel - minTileLevel) + minTileLevel;
		return new Tile(level);
	}

	/**
	 * Regenerates the grid with all random tiles produced by getRandomTile().
	 */
	public void radomizeTiles() {
		// these nested for loops are for getting the lengths
		for (int i = 0; i < grid.getWidth(); i++) {
			for (int j = 0; j < grid.getHeight(); j++) {
				grid.setTile(getRandomTile(), i, j);
			}
		}
	}

	/**
	 * Determines if two tiles are adjacent to each other. The may be next to each
	 * other horizontally, vertically, or diagonally.
	 * 
	 * @param t1 one of the two tiles
	 * @param t2 one of the two tiles
	 * @return true if they are next to each other horizontally, vertically, or
	 *         diagonally on the grid, false otherwise
	 */
	public boolean isAdjacent(Tile t1, Tile t2) {
		// the following four are the coordinates
		int x1 = t1.getX();
		int y1 = t1.getY();
		int x2 = t2.getX();
		int y2 = t2.getY();
		// this if condition covers all the possible adjacent blocks
		if ((isValidPosition(x1 - 1, y1) && x2 == x1 - 1 && y1 == y2)
				|| (isValidPosition(x1 + 1, y1) && x2 == x1 + 1 && y1 == y2)
				|| (isValidPosition(x1, y1 - 1) && x2 == x1 && y2 == y1 - 1)
				|| (isValidPosition(x1, y1 + 1) && x2 == x1 && y2 == y1 + 1)
				|| (isValidPosition(x1 + 1, y1 - 1) && x2 == x1 + 1 && y2 == y1 - 1)
				|| (isValidPosition(x1 + 1, y1 + 1) && x2 == x1 + 1 && y2 == y1 + 1)
				|| (isValidPosition(x1 - 1, y1 + 1) && x2 == x1 - 1 && y2 == y1 + 1)
				|| (isValidPosition(x1 - 1, y1 - 1) && x2 == x1 - 1 && y2 == y1 - 1)) {
			return true;
		}
		return false;
	}

	/**
	 * isValidPosition is a method of type boolean used to check is the coordinates
	 * of the tile are from valid position or not
	 * 
	 * @param x -> x-coordinate
	 * @param y -> y-coordinate
	 * @return of boolean type either true or false
	 */
	private boolean isValidPosition(int x, int y) {
		int cols = grid.getWidth();
		int rows = grid.getHeight();
		if (x < 0 || y < 0 || x > cols - 1 || y > rows - 1) {
			return false;
		}
		return true;
	}

	/**
	 * Indicates the user is trying to select (clicked on) a tile to start a new
	 * selection of tiles.
	 * <p>
	 * If a selection of tiles is already in progress, the method should do nothing
	 * and return false.
	 * <p>
	 * If a selection is not already in progress (this is the first tile selected),
	 * then start a new selection of tiles and return true.
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 * @return true if this is the first tile selected, otherwise false
	 */
	public boolean tryFirstSelect(int x, int y) {
		if (selected.size() > 0) {
			return false;
		}
		Tile tile = grid.getTile(x, y);
		tile.setSelect(true);
		grid.setTile(tile, x, y);
		selected.clear();
		selected.add(tile);
		return true;
	}

	/**
	 * indexOf method to check for the index of the tile
	 * 
	 * @param x -> x-coordinate
	 * @param y -> y-coordinate
	 * @return integer i or -1
	 */
	private int indexOf(int x, int y) {
		// this loop checks for the index of the tile
		for (int i = 0; i < selected.size(); i++) {
			Tile tileSelect = selected.get(i);
			if (x == tileSelect.getX() && y == tileSelect.getY()) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Indicates the user is trying to select (mouse over) a tile to add to the
	 * selected sequence of tiles. The rules of a sequence of tiles are:
	 * 
	 * <pre>
	 * 1. The first two tiles must have the same level.
	 * 2. After the first two, each tile must have the same level or one greater than the level of the previous tile.
	 * </pre>
	 * 
	 * For example, given the sequence: 1, 1, 2, 2, 2, 3. The next selected tile
	 * could be a 3 or a 4. If the use tries to select an invalid tile, the method
	 * should do nothing. If the user selects a valid tile, the tile should be added
	 * to the list of selected tiles.
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 */
	public void tryContinueSelect(int x, int y) {
		if (selected.isEmpty()) {
			return;
		}
		if (indexOf(x, y) != -1) {
			for (int i = indexOf(x, y) + 1; i < selected.size(); i++) {
				Tile tile = selected.get(i);
				unselect(tile.getX(), tile.getY());
			}
		} else if (selected.size() == 1) {
			int currentLevel = selected.get(0).getLevel();
			int nextLevel = grid.getTile(x, y).getLevel();
			if (currentLevel == nextLevel && isAdjacent(selected.get(0), grid.getTile(x, y))) {
				Tile tile = grid.getTile(x, y);
				tile.setSelect(true);
				grid.setTile(tile, x, y);
				selected.add(tile);
			}
		} else if (selected.size() > 1) {
			int lastLevel = selected.get(selected.size() - 1).getLevel();
			int nextLevel = grid.getTile(x, y).getLevel();
			if ((lastLevel == nextLevel || nextLevel == lastLevel + 1)
					&& isAdjacent(selected.get(selected.size() - 1), grid.getTile(x, y))) {
				Tile tile = grid.getTile(x, y);
				tile.setSelect(true);
				grid.setTile(tile, x, y);
				selected.add(tile);
			}
		}
	}

	/**
	 * Indicates the user is trying to finish selecting (click on) a sequence of
	 * tiles. If the method is not called for the last selected tile, it should do
	 * nothing and return false. Otherwise it should do the following:
	 * 
	 * <pre>
	 * 1. When the selection contains only 1 tile reset the selection and make sure all tiles selected is set to false.
	 * 2. When the selection contains more than one block:
	 *     a. Upgrade the last selected tiles with upgradeLastSelectedTile().
	 *     b. Drop all other selected tiles with dropSelected().
	 *     c. Reset the selection and make sure all tiles selected is set to false.
	 * </pre>
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 * @return return false if the tile was not selected, otherwise return true
	 */
	public boolean tryFinishSelection(int x, int y) {
		if (selected.isEmpty()) {
			return false;
		}
		if (selected.size() == 1) {
			unselect(selected.get(0).getX(), selected.get(0).getY());
			unselect(x, y);
			selected.clear();
			return true;
		} else if (selected.size() > 1) {
			Tile tile = selected.get(selected.size() - 1);
			if (indexOf(x, y) != selected.size() - 1) {
				return false;
			}
			long score = this.score;
			// this loop is for getting the correct score based on the selected tiles
			for (Tile t : selected) {
				score += t.getValue();
			}
			upgradeLastSelectedTile();
			dropSelected();
			setScore(score);
			selected.clear();
			return true;
		}
		return false;
	}

	/**
	 * Increases the level of the last selected tile by 1 and removes that tile from
	 * the list of selected tiles. The tile itself should be set to unselected.
	 * <p>
	 * If the upgrade results in a tile that is greater than the current maximum
	 * tile level, both the minimum and maximum tile level are increased by 1. A
	 * message dialog should also be displayed with the message "New block 32,
	 * removing blocks 2". Not that the message shows tile values and not levels.
	 * Display a message is performed with dialogListener.showDialog("Hello,
	 * World!");
	 */
	public void upgradeLastSelectedTile() {
		if (selected == null || selected.size() == 0) {
			return;
		}
		Tile lastTile = selected.get(selected.size() - 1);
		
		int level = lastTile.getLevel();
		level++;
		lastTile.setLevel(level);
		lastTile.setSelect(false);
		grid.setTile(lastTile, lastTile.getX(), lastTile.getY());

		if (level > maxTileLevel) {
			minTileLevel++;
			maxTileLevel++;
			dropLevel(minTileLevel - 1);
			Tile removed = new Tile(minTileLevel - 1);
			dialogListener.showDialog("New block " + lastTile.getValue() + ", removing blocks " + removed.getValue());
		}
	}

	/**
	 * Gets the selected tiles in the form of an array. This does not mean selected
	 * tiles must be stored in this class as a array.
	 * 
	 * @return the selected tiles in the form of an array
	 */
	public Tile[] getSelectedAsArray() {
		if (selected.size() > 0) {
			Tile[] tiles = new Tile[selected.size()];
			// this loop is iterate through all the elements in the array of selected tiles
			for (int i = 0; i < selected.size(); i++) {
				tiles[i] = selected.get(i);
			}
			return tiles;
		} else {
			return new Tile[] {};
		}
	}

	/**
	 * Removes all tiles of a particular level from the grid. When a tile is
	 * removed, the tiles above it drop down one spot and a new random tile is
	 * placed at the top of the grid.
	 * 
	 * @param level the level of tile to remove
	 */
	public void dropLevel(int level) {
		// find all the tiles of this level
		ArrayList<Tile> toRemove = new ArrayList<Tile>();
		// the initial loop will start from 0 to the total number of columns
		// the nested loop will start from back in reverse starting from the number of
		// rows to 0
		for (int i = 0; i < grid.getWidth(); i++) {
			for (int j = grid.getHeight() - 1; j >= 0; j--) {
				Tile tile = grid.getTile(i, j);
				if (tile.getLevel() == level) {
					toRemove.add(tile);
				}
			}
		}

		// for each of these tiles moves all the tiles above it until reaching the top,
		// then create a random tile at the top to replace it
		for (Tile t : toRemove) {
			int col = t.getX();
			int row = t.getY();
			for (int i = row - 1; i >= 0; i--) {
				Tile toMove = grid.getTile(col, i);
				toMove.setLocation(col, i + 1);
				grid.setTile(toMove, col, i + 1);
			}
			Tile randTile = getRandomTile();
			randTile.setLocation(col, 0);
			grid.setTile(randTile, col, 0);
		}
	}

	/**
	 * Removes all selected tiles from the grid. When a tile is removed, the tiles
	 * above it drop down one spot and a new random tile is placed at the top of the
	 * grid.
	 */
	public void dropSelected() {
		// find all the selected tiles
		ArrayList<Tile> toRemove = new ArrayList<Tile>();
		for (int i = 0; i < grid.getWidth(); i++) {
			for (int j = grid.getHeight() - 1; j >= 0; j--) {
				Tile tile = grid.getTile(i, j);
				if (tile.isSelected()) {
					toRemove.add(tile);
				}
			}
		}

		// for each of these tiles moves all the tiles above it until reaching the top,
		// then create a random tile at the top to replace it
		for (Tile t : toRemove) {
			int col = t.getX();
			int row = t.getY();
			for (int i = row - 1; i >= 0; i--) {
				Tile toMove = grid.getTile(col, i);
				toMove.setLocation(col, i + 1);
				grid.setTile(toMove, col, i + 1);
			}
			Tile randTile = getRandomTile();
			randTile.setLocation(col, 0);
			grid.setTile(randTile, col, 0);
		}
	}

	/**
	 * Remove the tile from the selected tiles.
	 * 
	 * @param x column of the tile
	 * @param y row of the tile
	 */
	public void unselect(int x, int y) {
		Tile tile = grid.getTile(x, y);
		tile.setSelect(false);
		grid.setTile(tile, x, y);
		selected.removeIf((tile1) -> tile1.getX() == tile.getX() && tile1.getY() == tile.getY());
	}

	/**
	 * Gets the player's score.
	 * 
	 * @return the score
	 */
	public long getScore() {
		return score;
	}

	/**
	 * Gets the game grid.
	 * 
	 * @return the grid
	 */
	public Grid getGrid() {
		return grid;
	}

	/**
	 * Gets the minimum tile level.
	 * 
	 * @return the minimum tile level
	 */
	public int getMinTileLevel() {
		return minTileLevel;
	}

	/**
	 * Gets the maximum tile level.
	 * 
	 * @return the maximum tile level
	 */
	public int getMaxTileLevel() {
		return maxTileLevel;
	}

	/**
	 * Sets the player's score.
	 * 
	 * @param score number of points
	 */
	public void setScore(long score) {
		this.score = score;
		if (scoreListener != null) {
			scoreListener.updateScore(this.score);
		}
	}

	/**
	 * Sets the game's grid.
	 * 
	 * @param grid game's grid
	 */
	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	/**
	 * Sets the minimum tile level.
	 * 
	 * @param minTileLevel the lowest level tile
	 */
	public void setMinTileLevel(int minTileLevel) {
		this.minTileLevel = minTileLevel;
	}

	/**
	 * Sets the maximum tile level.
	 * 
	 * @param maxTileLevel the highest level tile
	 */
	public void setMaxTileLevel(int maxTileLevel) {
		this.maxTileLevel = maxTileLevel;
	}

	/**
	 * Sets callback listeners for game events.
	 * 
	 * @param dialogListener listener for creating a user dialog
	 * @param scoreListener  listener for updating the player's score
	 */
	public void setListeners(ShowDialogListener dialogListener, ScoreUpdateListener scoreListener) {
		this.dialogListener = dialogListener;
		this.scoreListener = scoreListener;
	}

	/**
	 * Save the game to the given file path.
	 * 
	 * @param filePath location of file to save
	 */
	public void save(String filePath) {
		GameFileUtil.save(filePath, this);
	}

	/**
	 * Load the game from the given file path
	 * 
	 * @param filePath location of file to load
	 */
	public void load(String filePath) {
		GameFileUtil.load(filePath, this);
	}
}
