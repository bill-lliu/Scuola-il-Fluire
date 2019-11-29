
/* [Projectile.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * class controlling projectile movement
 * Date Completed: Jan 19, 2018
 */

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

//projectile class for all projectiles
public abstract class Projectile {

//variables for each individual projectile
	protected Point pos, startPos;
	protected Point2D.Double diff;
	protected boolean destroyed;
	protected long startTime;
	protected int speed;
	protected Rectangle hitbox;

	// method to update the projectile
	public abstract void update();

	// constructor for the projectile
	Projectile(int x, int y, int xDiff, int yDiff, int s) {
		// moves the projectile in accordance to its firing speed, keeping a costant
		// velocity
		if (xDiff == 0) {
			diff = new Point2D.Double(0.0, yDiff > 0 ? 1.0 : -1.0);
		} else {
			double hypo = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
			diff = new Point2D.Double((double) xDiff / hypo, (double) yDiff / hypo);
		}

		// if it did not hit anything, it will continue to move in that direction
		destroyed = false;
		pos = new Point(x, y);
		startPos = new Point(x, y);
		startTime = System.nanoTime();
		speed = s;

		// might change
		hitbox = new Rectangle(x, y, 20, 20);
	}

	// if the projectile hits an obstacle
	public boolean isDestroyed() {
		return destroyed;
	}

	// method to draw the projectile
	public abstract void draw(Graphics g);
}
