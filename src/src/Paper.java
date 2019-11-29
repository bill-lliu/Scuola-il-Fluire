import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

class Paper {
	public static BufferedImage image;
	private Rectangle hitbox;

	public static Point[] locations = new Point[7];

	Paper(int x, int y) {
		int pixelsPerBlock = Game.map.getPixelsPerBlock();
		hitbox = new Rectangle(x, y, pixelsPerBlock, pixelsPerBlock);
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public void draw(Graphics g) {
		int pixelsPerBlock = Game.map.getPixelsPerBlock();
		g.drawImage(image, hitbox.x - Game.player.pos.x + (int) Game.screenSize.getWidth() / 2 - pixelsPerBlock,
				hitbox.y - Game.player.pos.y + (int) Game.screenSize.getHeight() / 2 - pixelsPerBlock, pixelsPerBlock,
				pixelsPerBlock, null);
	}
}