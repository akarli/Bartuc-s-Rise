
import java.util.Random;


public class Item {
	String type;
	int armor,  critChance,  critDamage,  xpBonus,  bonusHealth,  bonusMana, dodge, damage;
	double  hpRegen, manaRegen;
	Random rand = new Random();

	public Item(int level, boolean epic, String type){
		this.type = type;
		if(type.matches("helm")){
			armor = rand.nextInt(level*5) + level * 10;
			critDamage = rand.nextInt(level*5) + level * 50;
		}
		if(type.matches("gloves")){
			armor = rand.nextInt(level*5) + level * 10;
			critChance = rand.nextInt(level * 25) + 40*level;
		}
		if(type.matches("chest")){
			armor = rand.nextInt(level*5) + level * 10;
			bonusHealth = rand.nextInt(5*level) + 2*level;
			hpRegen = (rand.nextInt(2*level)/10.0) + 0.2*level;
		}
		if(type.matches("pants")){
			armor = rand.nextInt(level*5) + level * 10;
			bonusHealth = rand.nextInt(5*level) + 2*level;
			hpRegen = (rand.nextInt(2*level)/10.0) + 0.2*level;
		}
		if(type.matches("boots")){
			armor = rand.nextInt(level*5) + level * 10;
			dodge = rand.nextInt(level*10)+ level * 10;
		}
		if(type.matches("shoulders")){
			armor = rand.nextInt(level*5) + level * 10;
			bonusMana = rand.nextInt(level * 10) + level *5;
			manaRegen = (rand.nextInt(2*level)/10.0) + 0.2*level;
		}
		if(type.matches("sword")){
			damage = rand.nextInt(level*5)+ level * 10;
		}
		if(type.matches("shield")){
			armor = rand.nextInt(level*5) + level * 10;
		}

	}

	public String getType(){
		return type;
	}

	public int getDamage(){
		return damage;
	}

	public int getArmor(){
		if(type.matches("sword")){
			return damage;
		}
		return armor;
	}

	public String bonusStat(){
		if(type.matches("helm")){
			return " with Critdamage: ";
		}
		if(type.matches("gloves")){
			return " with Critchance: ";
		}
		if(type.matches("chest")){
			return " with Bonus health: ";
		}
		if(type.matches("pants")){
			return " with Bonus health: ";
		}
		if(type.matches("boots")){
			return " with Dodge: ";
		}
		if(type.matches("shoulders")){
			return " with Bonus mana: ";
		}
		return "";
	}

	public int getBonusStat(){
		if(type.matches("helm")){
			return critDamage;
		}
		if(type.matches("gloves")){
			return critChance;
		}
		if(type.matches("chest")){
			return bonusHealth;
		}
		if(type.matches("pants")){
			return bonusHealth;
		}
		if(type.matches("boots")){
			return dodge;
		}
		if(type.matches("shoulders")){
			return bonusMana;
		}
		return 0;
	}

	public int getCritChance(){
		return critChance;
	}

	public int getCritDamage(){
		return critDamage;
	}

	public int getXpBonus(){
		return xpBonus;
	}

	public int getBonusHealth(){
		return bonusHealth;
	}

	public int getBonusMana(){
		return bonusMana;
	}

	public int getDodge(){
		return dodge;
	}

	public double getHpRegen(){
		return hpRegen;
	}

	public double getManaRegen(){
		return manaRegen;
	}

	public void setDamage(int damage){
		this.damage = damage;
	}

	public void setArmor(int armor){
		this.armor = armor;
	}

	public void setHPregen(double d){
		this.hpRegen = d;
	}

	public void setBonusHealth(int health){
		this.bonusHealth = health;
	}
	
	public void setManaRegen(double d){
		this.manaRegen = d;
	}

	public void setBonusMana(int mana){
		this.bonusMana = mana;
	}
	public void setCritDmg(int critdmg){
		this.critDamage = critdmg;
	}
	public void setCritChance(int critChance){
		this.critChance = critChance;
	}
	public void setDodge(int dodge){
		this.dodge = dodge;
	}

}
