
/* [EnemyRanged.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * ranged enemy subclass for all ranged enemies
 * Date Completed: Jan 19, 2018
 */

import java.awt.Graphics;

//ranged enemy subclass
public abstract class EnemyRanged extends Enemy {
	// veriables for each individual enemy
	protected int attackSpeed;
	protected int projectileSpeed;
	protected long lastAttack;

	// constructor for ranged enemy
	EnemyRanged(int h, int v, int x, int y, int as, int ps) {
		super(h, v, x, y);
		attackSpeed = as * 100000000;
		lastAttack = System.nanoTime() - attackSpeed;
		projectileSpeed = ps;
	}

	// abstract methods to be further differentiated by enemy subclasses
	protected abstract void loadSprites();

	public abstract void setDimensions();

	protected abstract void changeSprite();

	protected abstract void updateSprite();

	public abstract void draw(Graphics g);

	// method for enemy to "act"
	public void act() {
		// delay between enemy attacks
		if (System.nanoTime() - lastAttack >= attackSpeed) {
			// fires a projectile at the player
			int xDiff = Game.player.getDimensions().x / 2 * (Game.player.pos.x > pos.x ? 1 : -1);
			int yDiff = Game.player.getDimensions().y / 2 * (Game.player.pos.y > pos.y ? 1 : -1);
			// fires the projectile
			Game.projectiles.add(new ProjectileEnemy(pos.x + dimensions.x / 2, pos.y + dimensions.y / 2,
					Game.player.pos.x - pos.x + xDiff, Game.player.pos.y - pos.y + yDiff, projectileSpeed));
			// recounts delay before next attack
			lastAttack = System.nanoTime();
		} else {
			move();
		}
	}
}
