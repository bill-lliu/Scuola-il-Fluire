
/* [Enemy.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * Enemy class 
 * Date Completed: Jan 19, 2018
 */

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.lang.Math;
import java.util.Random;

//enemy class for enemies
public abstract class Enemy {
	// veriables for each individual enemy
	protected int health, vision;
	protected Point pos, lastTurn;
	protected Point direction;
	protected Rectangle hitbox;
	protected long lastMove, lastChange, lastMoving;
	protected BufferedImage[] sprites;
	protected int currentSprite, currentStep;
	protected boolean sideFace;
	private static Random rng = new Random();
	protected Point dimensions;

	// constructor for the enemy
	Enemy(int h, int v, int x, int y) {
		// starting perameters for the enemy
		sideFace = false;
		currentSprite = 0;
		currentStep = 0;
		health = h;
		vision = v;
		direction = new Point(0, 1);
		pos = new Point(x, y);
		lastTurn = new Point();
	}

	// abract methods for each individual enemy
	protected abstract void loadSprites();

	public abstract void setDimensions();

	protected abstract void act();

	protected abstract void changeSprite();

	protected abstract void updateSprite();

	public abstract void draw(Graphics g);

	// method to move the enemy towards the player if it is within the enemy's
	// vision
	// also attacks the player if the player is within range
	public void chase() {

		// checks if the player is within enemy vision for this action
		if (see()) {
			int pixelsPerBlock = Game.map.getPixelsPerBlock();
			// position of the palyer
			Point playerPos = new Point(Game.player.pos.x / pixelsPerBlock, Game.player.pos.y / pixelsPerBlock);
			// position of the enemy
			Point enemyPos = new Point(pos.x / pixelsPerBlock, pos.y / pixelsPerBlock);

			// faces the player
			if (Game.player.pos.x != pos.x) {
				double slope = (double) (Game.player.pos.y - pos.y) / (double) (Game.player.pos.x - pos.x);
				sideFace = Math.abs(slope) < 1.0;
			} else {
				sideFace = false;
			}

			// checks if enemy is positioned left or right of player
			int tmp = (playerPos.x - enemyPos.x) / Math.max(Math.abs(playerPos.x - enemyPos.x), 1);
			if (tmp != direction.x) {
				direction.x = tmp;
				lastMove = System.nanoTime();
				lastTurn.x = pos.x;
				lastTurn.y = pos.y;
			}

			// checks if enemy is positioned up or down of player
			tmp = (playerPos.y - enemyPos.y) / Math.max(Math.abs(playerPos.y - enemyPos.y), 1);
			if (tmp != direction.y) {
				direction.y = tmp;
				lastMove = System.nanoTime();
				lastTurn.x = pos.x;
				lastTurn.y = pos.y;
			}

			// changes sprite if necessary
			changeSprite();
			// attacks if in range
			act();

		} else {
			// if the enemy does not see the player, randomly generates a direction to
			// see/move
			if (System.nanoTime() - lastChange >= 1000000000) {
				direction.x = rng.nextInt(3) - 1;
				direction.y = rng.nextInt(3) - 1;
				changeSprite();
				lastChange = System.nanoTime();
				lastMove = System.nanoTime();
				lastTurn.x = pos.x;
				lastTurn.y = pos.y;
			}
			move();
		}
	}

	// method to move the enemy
	public void move() {
		// updates the enemy if necessary
		updateSprite();
		// move speed based on time elapsed
		long timeElapsed = (System.nanoTime() - lastMove) / 6000000;
		currentStep = (int) (System.nanoTime() / 25000000) % 24;
		Point toMove = new Point(pos.x, pos.y);

		// checks move direction of up or down
		if (direction.y == -1) {
			toMove.y = lastTurn.y - (int) timeElapsed;
		} else if (direction.y == 1) {
			toMove.y = lastTurn.y + (int) timeElapsed;
		}
		// checks move direction of left and right
		if (direction.x == -1) {
			toMove.x = lastTurn.x - (int) timeElapsed;
		} else if (direction.x == 1) {
			toMove.x = lastTurn.x + (int) timeElapsed;
		}

		int pixelsPerBlock = Game.map.getPixelsPerBlock();
		// moves the enemy to the new position
		Point start = new Point(toMove.x / pixelsPerBlock, toMove.y / pixelsPerBlock);
		Point end = new Point((toMove.x + dimensions.x) / pixelsPerBlock, (toMove.y + dimensions.y) / pixelsPerBlock);
		boolean canMove = true;
		// stops moving the enemy if there is an obstacle in the way
		for (int i = start.y; i <= end.y; ++i) {
			for (int j = start.x; j <= end.x; ++j) {
				if (Game.map.obstacleExists(i, j)) {
					canMove = false;
				}
			}
		}

		// moves the hitbox of the enemy to the new position
		hitbox.x = toMove.x;
		hitbox.y = toMove.y;

		// the enemy cannot move into the player
		if (hitbox.intersects(Game.player.getHitbox())) {
			canMove = false;
		}

		// if no obstacles
		if (canMove) {
			// moves enemy to new position
			pos.x = toMove.x;
			pos.y = toMove.y;
		} else {
			// keeps the enemy where it is
			lastTurn.x = pos.x;
			lastTurn.y = pos.y;
			hitbox.x = pos.x;
			hitbox.y = pos.y;
			lastMove = System.nanoTime();
		}
		lastMoving = System.nanoTime();
	}

	// getters
	public Point getPos() {
		return pos;
	}

	public Point getDirection() {
		return direction;
	}

	public Point getDimensions() {
		return dimensions;
	}

	public int getHealth() {
		return health;
	}

	// method to check if the player is within the enemy's "vision"
	public boolean see() {
		int pixelsPerBlock = Game.map.getPixelsPerBlock();
		// position of the player
		Point playerPos = new Point(Game.player.pos.x / pixelsPerBlock, Game.player.pos.y / pixelsPerBlock);
		// position of the enemy
		Point enemyPos = new Point(pos.x / pixelsPerBlock, pos.y / pixelsPerBlock);

		// checks if player slope within enemy vision
		if (playerPos.x != enemyPos.x) {
			double seeX = enemyPos.x;
			// draws a line from the enemy to the player
			double slope = (double) (playerPos.y - enemyPos.y) / (double) (playerPos.x - enemyPos.x), seeY = enemyPos.y;
			double change = Math.sqrt(1 + slope * slope) / 4.0, dist = 0;
			double changeX = playerPos.x > enemyPos.x ? 1 : -1;

			// ensures the line is within the triangle of the enemy's "field of vision"
			if (sideFace) {
				if (direction.x == 1) {
					if (Math.abs(slope) > 2.0 || changeX < 0.0) {
						return false;
					}
				} else if (direction.x == -1) {
					if (Math.abs(slope) > 2.0 || changeX > 0.0) {
						return false;
					}
				} else if (direction.y == 1) {
					if (Math.abs(slope) < 0.5 || playerPos.y < enemyPos.y) {
						return false;
					}
				} else if (direction.y == -1) {
					if (Math.abs(slope) < 0.5 || playerPos.y > enemyPos.y) {
						return false;
					}
				}
			} else {
				if (direction.y == 1) {
					if (Math.abs(slope) < 0.5 || playerPos.y < enemyPos.y) {
						return false;
					}
				} else if (direction.y == -1) {
					if (Math.abs(slope) < 0.5 || playerPos.y > enemyPos.y) {
						return false;
					}
				} else if (direction.x == 1) {
					if (Math.abs(slope) > 2.0 || changeX < 0.0) {
						return false;
					}
				} else {
					if (Math.abs(slope) > 2.0 || changeX > 0.0) {
						return false;
					}
				}
			}

			// checks if there is anything obstructing the line between the enemy and the
			// player
			changeX /= 4.0;
			for (; ((int) seeX != playerPos.x || (int) seeY != playerPos.y)
					&& Math.ceil(dist) <= vision; seeX += (double) changeX, dist += change, seeY += slope
							* (double) changeX) {
				if (Game.map.cannotShoot((int) seeY, (int) seeX)) {
					return false;
				}
			}
			return (int) seeX == playerPos.x && (int) seeY == playerPos.y;
			// makes sure they do not have the same y value (or else slope = undefined)
		} else if (playerPos.y != enemyPos.y) {
			if (Math.abs(enemyPos.y - playerPos.y) > vision) {
				return false;
			}
			if ((playerPos.y - enemyPos.y) / Math.abs(playerPos.y - enemyPos.y) != direction.y) {
				return false;
			}
			int changeY = playerPos.y > enemyPos.y ? 1 : -1;
			for (int seeY = enemyPos.y; seeY != playerPos.y; seeY += changeY) {
				if (Game.map.cannotShoot(seeY, enemyPos.x)) {
					return false;
				}
			}
			// if player did not move out of vision, returns true
			return true;
		} else {
			return true;
		}
	}

	// getters
	public Rectangle getHitbox() {
		return hitbox;
	}

	public void damage() {
		--health;
	}
}
