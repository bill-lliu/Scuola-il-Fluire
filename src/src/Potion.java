/* [Potion.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * class of items that have the ability to heal you
 * Date Completed: Jan 19, 2018
 */

//subclass of healing items (potions)
class Potion extends Item{
  
  int amount;
  
  //constructor for potions
  Potion(String name, int quantity){
    super(name); 
    amount = quantity;
  }
  
  //consuming the potion method
  public void use(){
    Game.player.heal(amount); 
  }
}
