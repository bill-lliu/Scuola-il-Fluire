
/* [Skull.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * melee enemy subclass (the skull enemy)
 * Date Completed: Jan 19, 2018
 */

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

//skull subclass
public class Skull extends Enemy {

	// variables used by the skull
	protected long lastAttack, attackSpeed, startAttack;
	protected boolean isAttacking;
	Rectangle range;

	// constructor of the skull
	Skull(int x, int y) {
		super(3, 10, x, y);
		attackSpeed = 1000000000;
		lastAttack = System.nanoTime() - attackSpeed;
		isAttacking = false;
		startAttack = Long.MAX_VALUE;
		loadSprites();
	}

	// method for the skull to perform action
	public void act() {
		// checks attack delay time
		if (System.nanoTime() - startAttack >= 0 && System.nanoTime() - startAttack <= 800000000) {
			return;
		} else if (System.nanoTime() - startAttack > 800000000) {
			// if the player intersects with the range of the skull's attack and takes
			// damage
			if (range.intersects(Game.player.getHitbox())) {
				Game.player.damage();
			}
			// skull performs an attack
			isAttacking = true;
			lastAttack = System.nanoTime();
			startAttack = Long.MAX_VALUE;
			lastMove = System.nanoTime();
			lastTurn.x = pos.x;
			lastTurn.y = pos.y;
		} else {
			if (System.nanoTime() - lastAttack >= 1500000000) {
				int y = pos.y, x = pos.x;
				int pixelsPerBlock = Game.map.getPixelsPerBlock();

				// movement of the skull
				if (sideFace) {
					if (direction.x == -1) {
						x -= pixelsPerBlock * 1 / 5;
					} else if (direction.x == 1) {
						x += pixelsPerBlock;
					} else if (direction.y == -1) {
						y -= pixelsPerBlock * 2 / 5;
					} else if (direction.y == 1) {
						y += pixelsPerBlock * 3 / 5;
					}
				} else {
					if (direction.y == -1) {
						y -= pixelsPerBlock * 2 / 5;
					} else if (direction.y == 1) {
						y += pixelsPerBlock * 3 / 5;
					} else if (direction.x == -1) {
						x -= pixelsPerBlock * 1 / 5;
					} else if (direction.x == 1) {
						x += pixelsPerBlock;
					}
				}

				// movement of the skull
				if (sideFace) {
					if (direction.x != 0) {
						range = new Rectangle(x, y, pixelsPerBlock / 3, pixelsPerBlock);
					} else {
						range = new Rectangle(x, y, pixelsPerBlock, pixelsPerBlock / 3);
					}
				} else {
					if (direction.y != 0) {
						range = new Rectangle(x, y, pixelsPerBlock, pixelsPerBlock / 3);
					} else {
						range = new Rectangle(x, y, pixelsPerBlock / 3, pixelsPerBlock);
					}
				}

				// if the range of the attack of the skull overlap with the hitboxes of the
				// player
				if (range.intersects(Game.player.getHitbox())) {
					// attacks in a direction
					if (System.nanoTime() - startAttack < 0) {
						startAttack = System.nanoTime();
					}
					lastMove = System.nanoTime();
					lastTurn.x = pos.x;
					lastTurn.y = pos.y;
				} else if (System.nanoTime() - startAttack < 0) {
					move();
				} else {
					lastMove = System.nanoTime();
					lastTurn.x = pos.x;
					lastTurn.y = pos.y;
				}
			} else if (System.nanoTime() - startAttack < 0) {
				// moves if there is nothing to attack
				move();
			} else {
				updateSprite();
			}
		}
	}

	// loads the sprites of the skull
	public void loadSprites() {
		try {
			sprites = new BufferedImage[21];
			// idle/moving sprites
			sprites[0] = ImageIO.read(new File("img/enemy/move/ObskullFrame1.png"));
			sprites[1] = ImageIO.read(new File("img/enemy/move/ObskullFrame2.png"));
			sprites[2] = ImageIO.read(new File("img/enemy/move/ObskullFrame3.png"));
			int width = sprites[0].getWidth(), height = sprites[0].getHeight();

			// reverses the images for the backwards sprites
			for (int i = 0; i < 3; ++i) {
				sprites[i + 3] = new BufferedImage(width, height, 2);
				for (int j = 0; j < width; ++j) {
					for (int k = 0; k < height; ++k) {
						sprites[i + 3].setRGB(j, k, sprites[i].getRGB(width - j - 1, k));
					}
				}
			}

			// load attack animation of the skull
			for (int i = 1; i <= 5; ++i) {
				sprites[i + 5] = ImageIO.read(new File("img/enemy/attack/ObskullAttackFrame" + i + ".png"));
			}
			for (int i = 1; i <= 5; ++i) {
				sprites[i + 10] = ImageIO.read(new File("img/enemy/attack/ObskullSideAttackFrame" + i + ".png"));
			}

			// backwards animation of attacking for skull
			width = sprites[11].getWidth();
			height = sprites[11].getHeight();
			for (int i = 0; i < 5; ++i) {
				sprites[i + 16] = new BufferedImage(width, height, 2);
				for (int j = 0; j < width; ++j) {
					for (int k = 0; k < height; ++k) {
						sprites[i + 16].setRGB(j, k, sprites[i + 11].getRGB(width - j - 1, k));
					}
				}
			}
		} catch (Exception e) {
			System.out.println("error loading sheet");
		}
	}

	// changes directions of the sprite
	protected void changeSprite() {
		if (direction.x == -1) {
			currentSprite = 3;
		} else if (direction.x == 1) {
			currentSprite = 0;
		}
	}

	// updates the sprite
	protected void updateSprite() {
		if (isAttacking && System.nanoTime() - lastAttack >= 299000000) {
			// resets sprite if finished attack
			if (currentSprite >= 16) {
				currentSprite = 3;
			} else {
				// starts an action
				currentSprite = 0;
			}
			startAttack = Long.MAX_VALUE;
			isAttacking = false;
			lastMove = System.nanoTime();
			lastTurn.x = pos.x;
			lastTurn.y = pos.y;
		}

		// the skull performs and attackin a direction
		if (isAttacking) {
			currentStep = (int) ((System.nanoTime() - lastAttack) / 60000000);
			if (sideFace) {
				if (direction.x == -1) {
					currentSprite = 16 + currentStep;
				} else if (direction.x == 1) {
					currentSprite = 11 + currentStep;
				} else if (direction.y == -1) {
					currentSprite = 11 + currentStep;
				} else if (direction.y == 1) {
					currentSprite = 6 + currentStep;
				}
			} else {
				if (direction.y == -1) {
					currentSprite = 11 + currentStep;
				} else if (direction.y == 1) {
					currentSprite = 6 + currentStep;
				} else if (direction.x == -1) {
					currentSprite = 16 + currentStep;
				} else if (direction.x == 1) {
					currentSprite = 11 + currentStep;
				}
			}
			lastMove = System.nanoTime();
			lastTurn.x = pos.x;
			lastTurn.y = pos.y;
		} else {
			currentStep = (int) ((System.nanoTime() - lastChange) / 25000000) % 24;
			if (direction.x == -1) {
				currentSprite = 3 + currentStep / 8;
			} else if (direction.x == 1) {
				currentSprite = 0 + currentStep / 8;
			}
		}
	}

	// resizes the skull
	public void setDimensions() {
		int pixelsPerBlock = Game.map.getPixelsPerBlock();
		dimensions = new Point(pixelsPerBlock, pixelsPerBlock * 3 / 4);
		hitbox = new Rectangle(pos.x, pos.y, dimensions.x, dimensions.y);
	}

	// draws the skull into the game window
	public void draw(Graphics g) {
		if (currentSprite >= 16) {
			int pixelsPerBlock = Game.map.getPixelsPerBlock();
			g.drawImage(sprites[currentSprite],
					pos.x - Game.player.pos.x + (int) Game.screenSize.getWidth() / 2 - (int) (pixelsPerBlock * 1.5),
					pos.y - Game.player.pos.y + (int) Game.screenSize.getHeight() / 2 - pixelsPerBlock,
					(int) (pixelsPerBlock * 1.5), (int) (pixelsPerBlock * 0.75), null);
		} else if (currentSprite > 10) {
			int pixelsPerBlock = Game.map.getPixelsPerBlock();
			g.drawImage(sprites[currentSprite],
					pos.x - Game.player.pos.x + (int) Game.screenSize.getWidth() / 2 - pixelsPerBlock,
					pos.y - Game.player.pos.y + (int) Game.screenSize.getHeight() / 2 - pixelsPerBlock,
					(int) (pixelsPerBlock * 1.5), (int) (pixelsPerBlock * 0.75), null);
		} else {
			int pixelsPerBlock = Game.map.getPixelsPerBlock();
			g.drawImage(sprites[currentSprite],
					pos.x - Game.player.pos.x + (int) Game.screenSize.getWidth() / 2 - pixelsPerBlock,
					pos.y - Game.player.pos.y + (int) Game.screenSize.getHeight() / 2 - pixelsPerBlock, pixelsPerBlock,
					pixelsPerBlock, null);
		}
	}
}
