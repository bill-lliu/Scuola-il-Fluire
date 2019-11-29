
/* [Item.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * Objects to be placed in inventory
 * Date Completed: Jan 19, 2018
 */

import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Graphics;

//item class
class Item {
	// veriables for the item
	private String name;
	private Image img;

	// constructor for item
	Item(String n) {
		img = Toolkit.getDefaultToolkit().getImage(name);
		name = n;
	}

	// methods to be differentiated
	public void use() {
	}

	public boolean check() {
		return true;
	}

	public void draw(Graphics g) {
	}

	// getters
	public String getName() {
		return name;
	}

	public Image getImg() {
		return img;
	}

}