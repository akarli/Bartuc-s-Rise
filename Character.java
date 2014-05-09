import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;


public class Character {
	private BufferedImage character, levelup;
	private int xPosition, yPosition, damage, level, armor, currentExperience, maxExperience, manaCounter, healthCounter, healthPotions, manaPotions;
	private double manaRegen, hpRegen, currentHealth, maxHealth, currentMana, maxMana;
	private Room currentRoom;
	private BufferedImage[] moveUp;
	private BufferedImage[] moveDown;
	private BufferedImage[] moveLeft;
	private BufferedImage[] moveRight;
	private BufferedImage[] levelUp;
	private BufferedImage[] attackUp;
	private BufferedImage[] attackDown;
	private BufferedImage[] attackLeft;
	private BufferedImage[] attackRight;
	private BufferedImage lastSprite;
	private BufferedImage charAttack;
	private int currentSprite = 0;
	private int animationCounter=1;
	private int x = 0;
	private int moveCounter = 0;
	private int sprite = 0;
	private boolean levelingUp, castingFireBall, aLeft, aRight, aUp, aDown, moving, mLeft, mRight, mUp, mDown;
	boolean attacking;
	private FireBall eldBoll;
	Random rand = new Random();

	public Character(){
		character = loadCharacterImage("charWalk.png");
		levelup = loadCharacterImage("LEVELUP.png");
		charAttack = loadCharacterImage("charAttack.png");
		moveUp = new BufferedImage[4];
		moveDown = new BufferedImage[4];
		moveRight = new BufferedImage[4];
		moveLeft = new BufferedImage[4];
		levelUp = new BufferedImage[39];
		attackUp = new BufferedImage[4];
		attackDown = new BufferedImage[4];
		attackLeft = new BufferedImage[4];
		attackRight = new BufferedImage[4];
		for(int i = 0; i < 4; i++){
			moveUp[i] = character.getSubimage(x+(i*34), 64, 34, 53);
			x = x+3;
		}
		x=0;
		lastSprite = moveUp[1];
		for(int i = 0; i < 4; i++){
			moveDown[i] = character.getSubimage(x+(i*38), 4, 38, 57);
			x=x+4;
		}
		x=249;
		for(int i = 0; i < 4; i++){
			moveRight[i] = character.getSubimage(x+(i*42), 67, 42, 50);
			x=x+1;
		}
		x= 270;
		for(int i = 0; i < 4; i++){
			moveLeft[i] = character.getSubimage(x+(i*45), 7, 45, 53);
			x=x+2;
		}

		attackDown[0]= charAttack.getSubimage(35, 0, 37, 70);
		attackDown[1]= charAttack.getSubimage(72, 0, 38, 70);
		attackDown[2]= charAttack.getSubimage(112, 0, 29, 70);
		attackDown[3]= charAttack.getSubimage(143, 0, 30, 70);
		attackUp[0] = charAttack.getSubimage(55, 79, 39, 67);
		attackUp[1] = charAttack.getSubimage(96, 79, 39, 67);
		attackUp[2] = charAttack.getSubimage(135, 79, 31, 67);
		attackUp[3] = charAttack.getSubimage(167, 79, 30, 67);
		attackLeft[0] = charAttack.getSubimage(345, 4, 37, 64);
		attackLeft[1] = charAttack.getSubimage(385, 4, 35, 64);
		attackLeft[2] = charAttack.getSubimage(423, 4, 51, 64);
		attackLeft[3] = charAttack.getSubimage(474, 4, 39, 64);
		attackRight[0] = charAttack.getSubimage(356, 72, 38, 73);
		attackRight[1] = charAttack.getSubimage(396, 72, 34, 73);
		attackRight[2] = charAttack.getSubimage(432, 72, 51, 73);
		attackRight[3] = charAttack.getSubimage(485, 72, 41, 73);

		for(int i = 0; i < 39 ;i++){
			levelUp[i] = levelup.getSubimage(0 + (i*128), 0, 128, 128);
		}

		xPosition = 448;
		yPosition = 416;
		maxHealth = 100.0;
		currentHealth = 100.0;
		damage = 25;
		armor = 0;
		level = 1;
		currentExperience = 0;
		maxExperience = level*100;
		currentRoom = Engine.centralZone;
		currentMana = 100.0;
		maxMana = 100.0;
		healthPotions = 0;
		manaCounter = 60;
		healthCounter = 60;
		manaPotions = 0;
		hpRegen = 1.0;
		manaRegen = 1.0;
	}
	public BufferedImage loadCharacterImage(String fileName){
		BufferedImage img = null;

		try{
			img = ImageIO.read(new File(fileName));
		} catch (IOException e1) {
			throw new IllegalArgumentException("Could not load the file.");
		}

		return img;
	}

	public int getXTile(){
		return xPosition/32;
	}

	public int getYTile(){
		return yPosition/32;
	}

	public boolean moving(){
		return (mUp || mDown || mLeft || mRight);
	}

	public void startMoving(String dir){
		if(!mUp && !mDown && !mLeft && !mRight){
			if(dir.matches("up")){
				lastSprite = moveUp[1];
				if(currentRoom == Engine.centralZone && (getXTile() == 27 || getXTile() == 26) && getYTile() < 8 && getYTile() > 5){
					xPosition = (getXTile() - 11)*Engine.TILE_WIDTH;
					yPosition = 18*Engine.TILE_HEIGHT;
					currentRoom = currentRoom.getExit("cave");
					DrawGame.newZone = true;

				}
				else if(getYTile()-1 < 0){
					currentRoom = currentRoom.getExit("north");
					yPosition = 608;
					DrawGame.newZone = true;
				}
				else if(currentRoom.getCollisionMap()[getYTile()-1][getXTile()] != 1){
					mUp = true;
					moveCounter += 16;
				}
			}
			if(dir.matches("down")){
				lastSprite = moveDown[1];
				if(currentRoom == Engine.caveZone){
					if(getXTile() == 15){
						xPosition = 26*Engine.TILE_WIDTH;
					}
					else{
						xPosition = 27*Engine.TILE_WIDTH;
					}
					yPosition = 7*Engine.TILE_HEIGHT;
					currentRoom = currentRoom.getExit("south");
					DrawGame.newZone = true;
				}
				else if(getYTile()+1 > 19){
					currentRoom = currentRoom.getExit("south");
					yPosition = 0;
					DrawGame.newZone = true;
				}
				else if(currentRoom.getCollisionMap()[getYTile()+1][getXTile()] != 1){
					mDown = true;
					moveCounter += 16;
				}
			}
			if(dir.matches("left")){
				lastSprite = moveLeft[1];
				if(getXTile()-1 < 0){
					currentRoom = currentRoom.getExit("west");
					xPosition = 992;
					DrawGame.newZone = true;
				}
				else if(currentRoom.getCollisionMap()[getYTile()][getXTile()-1] != 1){
					mLeft = true;
					moveCounter += 16;
				}
			}
			if(dir.matches("right")){
				lastSprite = moveRight[1];
				if(getXTile()+1 > 31){
					currentRoom = currentRoom.getExit("east");
					xPosition = 0;
					DrawGame.newZone = true;
				}
				else if(currentRoom.getCollisionMap()[getYTile()][getXTile()+1] != 1){
					mRight = true;
					moveCounter += 16;
				}
			}
		}
	}
	public void move(){
		if(mUp){
			yPosition-=2;
		}
		if(mDown){
			yPosition+=2;
		}
		if(mLeft){
			xPosition-=2;
		}
		if(mRight){
			xPosition+=2;
		}
		moveCounter--;
		if(moveCounter == 0 && !DrawGame.pressed){
			stopMoving();
		}
		if(moveCounter == 0 && DrawGame.pressed){
			if(mUp){
				mUp = false;
				startMoving("up");
			}
			if(mDown){
				mDown = false;
				startMoving("down");
			}
			if(mLeft){
				mLeft = false;
				startMoving("left");
			}
			if(mRight){
				mRight = false;
				startMoving("right");
			}

		}
	}

	public void stopMoving(){
		mUp = false;
		mDown = false;
		mLeft = false;
		mRight = false;
		moveCounter = 0;
	}

	public void attack(String dir){
		animationCounter = 0;
		currentSprite = 0;
		attacking = true;
		if(dir.matches("right")){
			aRight = true;
		}
		if(dir.matches("left")){
			aLeft=true;
		}
		if(dir.matches("up")){
			aUp=true;
			lastSprite = moveUp[1];
		}
		if(dir.matches("down")){
			aDown=true;
		}

	}

	public void stopAttack(){
		for(int j = 0; j < DrawGame.monsterList[DrawGame.monsterHash.get(currentRoom)].size();j++){ 
			Monster a = (Monster) DrawGame.monsterList[DrawGame.monsterHash.get(currentRoom)].get(j);
			if(aLeft){
				if((a.currentXTile() == getXTile() || a.currentXTile() - getXTile() == -1) && a.currentYTile() == getYTile()){
					if(a.takeDamage(DrawGame.character.getAttackDamage())){
						DrawGame.monsterList[DrawGame.monsterHash.get(currentRoom)].remove(j);
						DrawGame.character.increaseXP(25);
					}
				}
				lastSprite = moveLeft[1];
			}
			else if(aRight){
				if((a.currentXTile() == getXTile() || a.currentXTile() - getXTile() == 1) && (a.currentYTile() == getYTile())){
					if(a.takeDamage(DrawGame.character.getAttackDamage())){
						DrawGame.monsterList[DrawGame.monsterHash.get(currentRoom)].remove(j);
						DrawGame.character.increaseXP(25);
					}
				}
				lastSprite = moveRight[1];
			}
			else if(aUp){
				if((a.currentXTile() == getXTile()) && (a.currentYTile() == getYTile() || a.currentYTile() - getYTile() == -1)){
					if(a.takeDamage(DrawGame.character.getAttackDamage())){
						DrawGame.monsterList[DrawGame.monsterHash.get(currentRoom)].remove(j);
						DrawGame.character.increaseXP(25);
					}
				}
				lastSprite = moveUp[1];
			}
			else if(aDown){
				if((a.currentXTile() == getXTile()) && (a.currentYTile() == getYTile() || a.currentYTile() - getYTile() == 1)){
					if(a.takeDamage(DrawGame.character.getAttackDamage())){
						DrawGame.monsterList[DrawGame.monsterHash.get(currentRoom)].remove(j);
						DrawGame.character.increaseXP(25);
					}
				}
				lastSprite = moveDown[1];
			}
		}
		aLeft = false;
		aRight = false;
		aUp = false;
		aDown = false;
	}

	public void increaseXP(int xp){
		currentExperience += xp;
	}

	public void levelUp(){
		level++;
		currentExperience = currentExperience - maxExperience;
		maxExperience = level*100;

		maxHealth += Engine.HP_LVL_UP;
		maxMana += Engine.MANA_LVL_UP;
		damage += Engine.DAMAGE_LVL_UP;
		armor += Engine.ARMOR_LVL_UP;
		hpRegen += Engine.HPREGEN_LVL_UP;
		manaRegen += Engine.MANAREGEN_LVL_UP;

		currentHealth = maxHealth;
		currentMana = maxMana;

		GameMain.infoBox.append(Engine.levelUpMessage);
		GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
		levelingUp = true;
	}

	public void castFireBall(){
		if(!castingFireBall){
			if(currentMana-30 >= 0){
				currentMana -=30;
				castingFireBall = true;
				if(lastSprite == moveRight[1]){
					eldBoll = new FireBall(xPosition + 32, yPosition, currentRoom);
					eldBoll.cast();
				}
				if(lastSprite == moveLeft[1]){
					eldBoll = new FireBall(xPosition - 32, yPosition, currentRoom);
					eldBoll.cast();
				}
				if(lastSprite == moveUp[1]){
					eldBoll = new FireBall(xPosition, yPosition - 32, currentRoom);
					eldBoll.cast();
				}
				if(lastSprite == moveDown[1]){
					eldBoll = new FireBall(xPosition, yPosition + 32, currentRoom);
					eldBoll.cast();
				}
			}
			else{
				GameMain.infoBox.append(Engine.noManaMessage);
				GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
			}
		}
	}

	public void stopCasting(){
		castingFireBall = false;
	}

	public void setX(int x){
		xPosition = x;
	}

	public void setY(int y){
		yPosition = y;
	}

	public int getLevel(){
		return level;
	}

	public void setLevel(int level){
		this.level = level;
	}

	public double getCurrHP(){
		return currentHealth;
	}

	public double getMaxHP(){
		return maxHealth;
	}

	public void setHP(Double double1){
		maxHealth = double1;
	}

	public int getDamage(){
		return damage;
	}

	public int getAttackDamage(){
		int randDamage = rand.nextInt((damage/10))+(damage - damage/20);
		return randDamage;
	}

	public void setDamage(int damage){
		this.damage = damage;
	}

	public int getArmor(){
		return armor;
	}

	public void setArmor(int armor){
		this.armor = armor;
	}

	public int getXP(){
		return currentExperience;
	}

	public double getHPRegen(){
		return hpRegen;
	}

	public void setHPRegen(Double double1){
		hpRegen += double1;
	}

	public double getManaRegen(){
		return manaRegen;
	}

	public void setManaRegen(Double double1){
		manaRegen += double1;
	}

	public int getMaxXP(){
		return maxExperience;
	}

	public void setXP(int xp){
		currentExperience = xp;
	}

	public double getMana(){
		return currentMana;
	}

	public double getMaxMana(){
		return maxMana;
	}

	public void setMana(Double double1){
		maxMana = double1;
	}

	public Room getCurrentRoom(){
		return currentRoom;
	}

	public void setCurrHP(Double double1){
		currentHealth = double1;
	}

	public void setCurrMana(Double double1){
		currentMana = double1;
	}

	public void setMaxXP(){
		maxExperience = level*100;
	}

	public int getHPPots(){
		return healthPotions;
	}

	public void setHPPots(int pots){
		healthPotions += pots;
	}
	public void loadHPPots(int pots){
		healthPotions = pots;
	}

	public void loadManaPots(int pots){
		manaPotions = pots;
	}

	public void loadManaRegen(double regen){
		manaRegen = regen;
	}
	public void loadHPRegen(double regen){
		hpRegen = regen;
	}

	public void useHPPot(){
		if(currentHealth >= maxHealth){
			GameMain.infoBox.append("\n You're already at full health.");
		}
		else if(healthPotions > 0){
			currentHealth += maxHealth/2;
			if(currentHealth > maxHealth){
				currentHealth = maxHealth;
			}
			healthPotions--;
			GameMain.infoBox.append(Engine.hpPotMessage);
		}
		else{
			GameMain.infoBox.append(Engine.noHpPotsMessage);
		}
		GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
	}

	public int getManaPots(){
		return manaPotions;
	}

	public void setManaPots(int pots){
		manaPotions += pots;
	}

	public void useManaPot(){
		if(currentMana >= maxMana){
			GameMain.infoBox.append("\n You're already at full mana.");
		}
		else if(manaPotions > 0){
			currentMana += maxMana/2;
			if(currentMana > maxMana){
				currentMana = maxMana;
			}
			manaPotions--;
			GameMain.infoBox.append(Engine.manaPotMessage);
		}
		else{
			GameMain.infoBox.append(Engine.noManaPotsMessage);
		}
		GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
	}

	public void takeDamage(double damage) {
		double damageReduction = (0.06 * armor) / (1 + 0.06 * armor);
		damage = damage * (1 - damageReduction);
		if (damage < 0) {
			damage = 0;
		}
		currentHealth -= damage;
	}

	public void getLoot(){
		Boolean epicGear = false;
		int drop = rand.nextInt(100);
		if(drop >= 0){
			int quality = rand.nextInt(100);
			if(quality >= 50){
				epicGear = true;
			}
			int lootType = rand.nextInt(4);
			if(lootType == 2){
				if(epicGear){
					int damageUpgrade = rand.nextInt(level*10) + 5*level;
					damage += damageUpgrade;
					GameMain.infoBox.append("\n You found a legendary sword forged by the gods! \n Damage increased by " + damageUpgrade + ".");
				}
				else{
					int damageUpgrade = rand.nextInt(level) + level; 
					damage += damageUpgrade;
					GameMain.infoBox.append("\n You found a mighty sword from a fallen enemy! \n Damage increased by " + damageUpgrade + ".");
				}
			}
			else if(lootType == 3){
				if(epicGear){
					int armorUpgrade = rand.nextInt(4*level) + 2*level;
					armor += armorUpgrade;

					GameMain.infoBox.append("\n You found a huge kite shield used by champions! \n Armor increased by " + armorUpgrade + ".");
				}
				else{
					int armorUpgrade = rand.nextInt(2*level) + level;
					armor += armorUpgrade;

					GameMain.infoBox.append("\n You found a robust targe shield from a fallen enemy! \n Armor increased by " + armorUpgrade + ".");
				}
			}
			else{
				int gearType = rand.nextInt(6);
				if(epicGear){
					int armorUpgrade = rand.nextInt(2*level) + level;
					armor += armorUpgrade;
					int healthUpgrade = rand.nextInt(5*level) + 3*level;
					maxHealth += healthUpgrade;
					int manaUpgrade = rand.nextInt(3*level)+2*level;
					maxMana += manaUpgrade;
					Double hpRegenUpgrade = (rand.nextInt(2*level)/10.0) + 0.2*level;
					hpRegenUpgrade.doubleValue();
					hpRegen += hpRegenUpgrade;
					Double manaRegenUpgrade = (rand.nextInt(2*level)/10.0) + 0.2*level;
					manaRegenUpgrade.doubleValue();
					manaRegen += manaRegenUpgrade;
					GameMain.infoBox.append("\n You found " + Engine.gearTypeEpic[gearType] + " worn by angles! \n Armor increased by " + armorUpgrade + ".\n Health increased by " + healthUpgrade +  ".\n Mana increased by " + manaUpgrade +  ". \n Heath regeneration increased by " + GameMain.oneDigit.format(hpRegenUpgrade) +". \n Mana  regeneration increased by " + GameMain.oneDigit.format(manaRegenUpgrade));
				}
				else{
					int stat = rand.nextInt(2);
					int armorUpgrade = rand.nextInt(level) + 1;
					armor += armorUpgrade;
					if(stat == 0){
						int healthUpgrade = rand.nextInt(3*level)+level;
						maxHealth += healthUpgrade;
						Double hpRegenUpgrade = (rand.nextInt(level)/10.0) + 0.1*level;
						hpRegenUpgrade.doubleValue();
						hpRegen += hpRegenUpgrade;
						GameMain.infoBox.append("\n You found " + Engine.gearTypeHealth[gearType] + " from a fallen enemy! \n Armor increased by " + armorUpgrade + ". \n Health increased by " + healthUpgrade + ". \n Heath regeneration increased by " + GameMain.oneDigit.format(hpRegenUpgrade));
					}
					else{
						int manaUpgrade = rand.nextInt(level) + level;
						maxMana += manaUpgrade;
						Double manaRegenUpgrade = (rand.nextInt(level)/10.0) + 0.1*level;
						manaRegenUpgrade.doubleValue();
						manaRegen += manaRegenUpgrade;
						GameMain.infoBox.append("\n You found " + Engine.gearTypeMana[gearType] + " from a fallen enemy! \n Armor increased by " + armorUpgrade + ". \n Mana increased by " + manaUpgrade + ". \n Mana  regeneration increased by " + GameMain.oneDigit.format(manaRegenUpgrade));
					}
				}
			}
			GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
		}
		int potionDrop = rand.nextInt(4);
		if (potionDrop == 3) {
			int potion = rand.nextInt(2);
			if (potion == 1) {
				healthPotions++;
				GameMain.infoBox.append(" \n You found a health potion!");
			} else {
				manaPotions++;
				GameMain.infoBox.append(" \n You found a mana potion!");
			}
		}
		GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
	}
	public BufferedImage getImage(){
		if(animationCounter == 8){
			currentSprite++;
			animationCounter = 1;
		}
		if(currentSprite >= 4){
			currentSprite = 0;
			if(attacking){
				stopAttack();
			}
			attacking = false;
		}
		if(mUp){
			lastSprite = moveUp[1];
			return moveUp[currentSprite];
		}
		if(mDown){
			lastSprite = moveDown[1];
			return moveDown[currentSprite];
		}
		if(mRight){
			lastSprite = moveRight[1];
			return moveRight[currentSprite];
		}
		if(mLeft){
			lastSprite = moveLeft[1];
			return moveLeft[currentSprite];
		}
		if(aLeft){
			return attackLeft[currentSprite];
		}
		if(aRight){
			return attackRight[currentSprite];
		}
		if(aUp){
			return attackUp[currentSprite];
		}
		if(aDown){
			return attackDown[currentSprite];
		}
		return lastSprite;
	}

	public BufferedImage getLevelupImage(){
		if(sprite == 76){
			sprite = 0;
			levelingUp = false;
		}
		return levelUp[sprite/2];
	}

	public void die(){
		level = 1;
		currentHealth = 100;
		maxHealth = 100;
		currentMana = 100;
		maxMana = 100;
		damage = 25;
		armor = 0;
		currentExperience = 0;
		maxExperience = level*100;
		manaPotions = 0;
		healthPotions = 0;
		hpRegen = 1.0;
		manaRegen = 1.0;
		DrawGame.reset();

		currentRoom = Engine.centralZone;
		setX(448);
		setY(416);

	}

	public void drawImage(Graphics g){
		if ((aLeft || aRight || aUp || aDown)) {
			int x = 0;
			if(aLeft)
				x = 15;
			else
				x = 20;
			if(currentSprite <= 2)
				g.drawImage(getImage(), xPosition, yPosition - x, null);
			else if(currentSprite == 3 && animationCounter < 8)
				g.drawImage(getImage(), xPosition, yPosition - x, null);
			else
				g.drawImage(getImage(), xPosition, yPosition, null);
		} else {
			g.drawImage(getImage(), xPosition, yPosition, null);
		}

		if(levelingUp){
			g.drawImage(getLevelupImage(), xPosition -46, yPosition- 50, null);
			sprite++;
		}
		if(castingFireBall){
			eldBoll.drawImage(g);
		}

		if(aLeft || aRight || aUp || aDown){
			animationCounter++;
		}

		if(mUp || mDown || mLeft || mRight){
			move();
			animationCounter++;
		}

		if(currentHealth < maxHealth && healthCounter >= 60){
			currentHealth += hpRegen;
			if(currentHealth > maxHealth){
				currentHealth = maxHealth;
			}
			healthCounter = 0;
		}
		if(currentMana < maxMana && manaCounter >= 60){
			currentMana += manaRegen;
			if(currentMana > maxMana){
				currentMana = maxMana;
			}
			manaCounter = 0;
		}
		manaCounter++;
		healthCounter++;
	}
}
