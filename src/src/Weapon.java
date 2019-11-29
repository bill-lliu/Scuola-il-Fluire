/* [Weapon.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * class of items that have the ability to change your damage output
 * Date Completed: Jan 19, 2018
 */

//subclass of damage items (weapons)
class Weapon extends Item {
	private int damage;

	// constructor for weapons
	Weapon(String name, int itemDamage) {
		super(name);
		damage = itemDamage;
	}

	// getter for the new damage reset by this weapons
	public int getDamage() {
		return damage;
	}
}
