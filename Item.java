
import java.util.Random;


public class Item {
	String type;
	int armor,  critChance,  critDamage,  xpBonus,  bonusHealth,  bonusMana, dodge, damage;
	double  hpRegen, manaRegen;
	Random rand = new Random();

	public Item(int level, boolean epic, String type){
		this.type = type;
		if(type.matches("helm")){
			if(epic){
				armor = rand.nextInt(level*5) + level * 4;
				critDamage = rand.nextInt(level*5) + level * 10;
				if(critDamage > 500){
					critDamage = 500;
				}
			}
			else{
				armor = rand.nextInt(level*3) + level * 2;
				critDamage = rand.nextInt(level*3) + level * 5;
				if(critDamage > 400){
					critDamage = 400;
				}
			}
		}
		if(type.matches("gloves")){
			if(epic){
				armor = rand.nextInt(level*4) + level * 3;
				critChance = rand.nextInt((level*5)/10 + 1) + level;
				if(critChance > 50){
					critChance = 50;
				}
			}
			else{
				armor = rand.nextInt(level*2) + level;
				critChance = rand.nextInt((level*3/10) + 1) + level/2;
				if(critChance > 50){
					critChance = 50;
				}
			}
		}
		if(type.matches("chest")){
			if(epic){
				armor = rand.nextInt(level*8) + level * 7;
				bonusHealth = rand.nextInt(5*level) + 10*level;
				hpRegen = (rand.nextInt(4*level)/10.0) + 0.4*level;
			}
			else{
				armor = rand.nextInt(level*4) + level * 3;
				bonusHealth = rand.nextInt(3*level) + 5*level;
				hpRegen = (rand.nextInt(2*level)/10.0) + 0.2*level;
			}
			
		}
		if(type.matches("pants")){
			if(epic){
				armor = rand.nextInt(level*6) + level * 5;
				bonusHealth = rand.nextInt(4*level) + 8*level;
				hpRegen = (rand.nextInt(3*level)/10.0) + 0.3*level;
			}
			else{
				armor = rand.nextInt(level*3) + level * 2;
				bonusHealth = rand.nextInt(5*level) + 2*level;
				hpRegen = (rand.nextInt(level)/10.0) + 0.1*level;
			}
		}
		if(type.matches("boots")){
			if(epic){
				armor = rand.nextInt(level*4) + level * 3;
				dodge = rand.nextInt(level + 1)+ level;
				if(dodge>40){
					dodge = 40;
				}
			}
			else{
				armor = rand.nextInt(level*2) + level;
				dodge = rand.nextInt(level/2 + 1)+ level/2;
				if(dodge>30){
					dodge = 30;
				}
			}
		}
		if(type.matches("shoulders")){
			if(epic){
				armor = rand.nextInt(level*6) + level * 5;
				bonusMana = rand.nextInt(5*level) + 10*level;
				manaRegen = (rand.nextInt(4*level)/10.0) + 0.4*level;
			}
			else{
				armor = rand.nextInt(level*3) + level * 2;
				bonusMana = rand.nextInt(3*level) + 5*level;
				manaRegen = (rand.nextInt(2*level)/10.0) + 0.2*level;
			}
		}
		if(type.matches("sword")){
			if(epic){
				damage = rand.nextInt(level*7) + level * 13;
			}
			else{
				damage = rand.nextInt(level*4)+ level * 7;
			}
		}
		if(type.matches("shield")){
			if(epic){
				armor = rand.nextInt(level*10) + level * 9;
			}
			else{
				armor = rand.nextInt(level*6) + level * 5;
			}

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
