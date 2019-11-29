/* [ProjectileEnemy.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * subclass of projectiles fired by the enemies
 * Date Completed: Jan 19, 2018
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;

//subclass of projectiles fired from enemies
public class ProjectileEnemy extends Projectile {
  public static BufferedImage img;
  
  //constructor for projectile enemy
  ProjectileEnemy(int x, int y, int xDiff, int yDiff, int s) {
    super(x, y, xDiff, yDiff, s);
  }
  
  //constantly updating the projectile 
  public void update() {
    if (destroyed) {
      return;
    }
    
    //movement of the projectile
    int timeElapsed = (int) ((System.nanoTime() - startTime) / (40000 * speed));
    pos.x = startPos.x + (int) (diff.x * timeElapsed);
    pos.y = startPos.y + (int) (diff.y * timeElapsed);
    int pixelsPerBlock = Game.map.getPixelsPerBlock();
    //checks if the projectile has hit an obstacle
    //obstacles such as water; projectiles can pass over; however, solid walls block projectiles
    if (Game.map.cannotShoot(pos.y / pixelsPerBlock, pos.x / pixelsPerBlock)) {
      destroyed = true;
      return;
    }
    
    //checks if the projectile has hit the hitbox of the player
    hitbox.x = pos.x;
    hitbox.y = pos.y;
    if (hitbox.intersects(Game.player.getHitbox())) {
      destroyed = true;
      Game.player.damage();
      return;
    }
  }
  
  //draws the projectile
  public void draw(Graphics g) {
    int pixelsPerBlock = Game.map.getPixelsPerBlock();
    g.drawImage(img, pos.x - Game.player.pos.x + (int) Game.screenSize.getWidth() / 2 - pixelsPerBlock,
                pos.y - Game.player.pos.y + (int) Game.screenSize.getHeight() / 2 - pixelsPerBlock, 20, 20, null);
  }
}
