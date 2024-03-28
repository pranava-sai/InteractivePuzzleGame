package hw3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import api.Tile;

/**
 * Utility class with static methods for saving and loading game files.
 */
/**
 * 
 * @author SubrahmanyaSreePranavaSaiMaganti
 *
 */
public class GameFileUtil {
	/**
	 * Saves the current game state to a file at the given file path.
	 * <p>
	 * The format of the file is one line of game data followed by multiple lines of
	 * game grid. The first line contains the: width, height, minimum tile level,
	 * maximum tile level, and score. The grid is represented by tile levels. The
	 * conversion to tile values is 2^level, for example, 1 is 2, 2 is 4, 3 is 8, 4
	 * is 16, etc. The following is an example:
	 * 
	 * <pre>
	 * 5 8 1 4 100
	 * 1 1 2 3 1
	 * 2 3 3 1 3
	 * 3 3 1 2 2
	 * 3 1 1 3 1
	 * 2 1 3 1 2
	 * 2 1 1 3 1
	 * 4 1 3 1 1
	 * 1 3 3 3 3
	 * </pre>
	 * 
	 * @param filePath the path of the file to save
	 * @param game     the game to save
	 */
	public static void save(String filePath, ConnectGame game) {
		try {
			// BufferedWriter is used to write into a new file
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
			String[] firstParts = new String[5];
			Grid g = game.getGrid();
			// The following sets the elements to the correct index
			firstParts[0] = g.getWidth() + "";
			firstParts[1] = g.getHeight() + "";
			firstParts[2] = game.getMinTileLevel() + "";
			firstParts[3] = game.getMaxTileLevel() + "";
			firstParts[4] = game.getScore() + "";
			String firstLine = String.join(" ", firstParts);
			writer.write(firstLine);
			writer.newLine();
			// loops through the height and width of the grid
			for (int i = 0; i < g.getHeight(); i++) {
				String[] parts = new String[g.getWidth()];
				for (int j = 0; j < g.getWidth(); j++) {
					parts[j] = g.getTile(j, i).getLevel() + "";
				}
				String line = String.join(" ", parts);
				// writes to a new file
				writer.write(line);
				if (i != g.getHeight() - 1) {
					writer.newLine();
				}

			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the file at the given file path into the given game object. When the
	 * method returns the game object has been modified to represent the loaded
	 * game.
	 * <p>
	 * See the save() method for the specification of the file format.
	 * 
	 * @param filePath the path of the file to load
	 * @param game     the game to modify
	 */
	public static void load(String filePath, ConnectGame game) {
		File f = new File(filePath);
		try {
			Scanner scnr = new Scanner(f);
			String firstLine = scnr.nextLine();
			firstLine = firstLine.trim();
			String[] first = firstLine.split("\\s+");
			int width = Integer.parseInt(first[0]);
			int height = Integer.parseInt(first[1]);
			int minTileLevel = Integer.parseInt(first[2]);
			int maxTileLevel = Integer.parseInt(first[3]);
			long score = Long.parseLong(first[4]);
			Grid g = new Grid(width, height);
			game.setMinTileLevel(minTileLevel);
			game.setMaxTileLevel(maxTileLevel);
			game.setScore(score);
			int j = 0;
			// reads through each lines
			while (scnr.hasNextLine()) {
				String line = scnr.nextLine();
				line = line.trim();
				String[] parts = line.split("\\s+");
				for (int i = 0; i < parts.length; i++) {
					int tileLevel = Integer.parseInt(parts[i]);
					Tile tile = new Tile(tileLevel);
					g.setTile(tile, i, j);
				}
				j++;
			}
			game.setGrid(g);
			scnr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
