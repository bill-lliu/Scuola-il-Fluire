
/* [Game.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * Overarching Game Superclass containing the main game loop run function
 * Date Completed: Jan 19, 2018
 */

import java.awt.Dimension;
import java.util.ArrayList;

//Class to start game and manage windows
class Game {

	// subclasses and variables used by the rest of the game
	public static GameWindow game;
	public static Map map;
	public static Player player;
	public static Dimension screenSize;
	public static ArrayList<Enemy> enemies;
	public static ArrayList<Projectile> projectiles;
	public static Inventory backpack;
	public static ArrayList<Item> groundItems;
	public static ArrayList<Paper> papers;

	public static boolean restartGame = false;

	public static void main(String[] args) {
		initGame();
	}

	// main game loop
	public static void initGame() {
		// arraylists to be used for game
		enemies = new ArrayList<Enemy>();
		projectiles = new ArrayList<Projectile>();
		backpack = new Inventory();
		groundItems = new ArrayList<Item>();
		papers = new ArrayList<Paper>();

		// starts the game
		game = new GameWindow();
	}
}