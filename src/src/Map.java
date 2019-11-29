
/* [Map.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * Grid for background and rendering space
 * Date Completed: Jan 19, 2018
 */

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import javax.imageio.ImageIO;

//map class
class Map {

	// variables for the map
	private int xRes, yRes;
	private int pixelsPerBlock;
	private BufferedImage floor = null;
	private int map[][];

	// constructor for the map
	public Map(int xResolution, int yResolution) {
		xRes = xResolution;
		yRes = yResolution;

		// loads walkable/shootable space to a 2d array
		map = loadMapData("img/background/map.txt");

		// loads the background image
		try {
			floor = ImageIO.read(new File("img/background/SchoolRoomBackdrop.png"));
			// place the player in the center of the map
			Game.player.pos = new Point(1335, 356);// starting location of the room
			Game.player.loc = new Point(xRes / 2, yRes / 2);

			// number may need to change depending on map dimensions
			pixelsPerBlock = (int) (floor.getWidth() / 35.5);
		} catch (Exception E) {
			System.out.println("error loading map!");
		}
	}

	// method for checking if an obstacle exists
	public boolean obstacleExists(int i, int j) {
		try {
			return map[i][j] != 0;
		} catch (ArrayIndexOutOfBoundsException e) {
			return true;
		}
	}

	// method for stopping enemies from shooting
	public boolean cannotShoot(int i, int j) {
		try {
			return map[i][j] == 1;
		} catch (ArrayIndexOutOfBoundsException e) {
			return true;
		}
	}

	// getter
	public int getPixelsPerBlock() {
		return pixelsPerBlock;
	}

	// loads a map.txt file corresponding to what each tile should show (obstacle or
	// empty space)
	public int[][] loadMapData(String filename) {
		// 2d array
		int data[][] = null;

		// inputs the file
		try {
			File f = new File(filename);
			Scanner input = new Scanner(f);
			// first 2 integers are map height then map width
			data = new int[input.nextInt()][input.nextInt()];

			// loads each integer into the 2d array
			for (int j = 0; j < data.length; j++) {
				for (int i = 0; i < data[0].length; i++) {
					data[j][i] = input.nextInt();
				}
			}
			input.close();
		} catch (Exception E) {
		}
		return data;
	}

	// Altered graphics display
	public void draw(Graphics g) {
		int pixelsPerBlock = Game.map.getPixelsPerBlock();
		g.drawImage(floor, xRes / 2 - Game.player.pos.x - pixelsPerBlock - Game.player.screenshake,
				yRes / 2 - Game.player.pos.y - pixelsPerBlock - Game.player.screenshake, 6340, 3840, null);
		Game.player.shakeReset();
	}
}