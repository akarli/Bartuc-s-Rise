
public class Bartuc {

	private double maxHealth, currentHealth, baseHealth;
	private int damage, baseDamage, attackCooldown, difficulty;
	
	public Bartuc(int kills){
		difficulty = kills + 1;
		baseHealth = 100.0;
		baseDamage = 35;
		maxHealth = (100.0*baseHealth) * difficulty;
		currentHealth = 100.0;
		damage = (10*baseDamage) * difficulty;
		attackCooldown = 10;
		
	}
	
	public double getCurrentHealth(){
		return currentHealth;
	}
	
	public double getMaxHealth(){
		return maxHealth;
	}
	
	public int getDamage(){
		return damage;
	}
	
	public int getCD(){
		return attackCooldown;
	}
	
	public void shadowBlast(){
		
	}
}
