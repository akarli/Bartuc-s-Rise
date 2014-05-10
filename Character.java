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
	private String statsMessage;
	private int currentSprite = 0;
	private int animationCounter=1;
	private int x = 0;
	private int moveCounter = 0;
	private int sprite = 0;
	private String name;
	private boolean levelingUp, castingFireBall, aLeft, aRight, aUp, aDown, mLeft, mRight, mUp, mDown;
	boolean attacking;
	private FireBall eldBoll;
	Random rand = new Random();
	
	/**
	 * STATS COUNTERS
	 * Keeps track of most of the statistics in the game.
	 */
	private int bartucKills, totalKills, magicKills, swordKills, damageDealt, magicDamageDealt, swordDamageDealt, fireBalls, missedFireBalls, damageTaken;
	private int damageTakenBartuc, healthPotsUsed, manaPotsUsed, gearFound, epicGearFound, swordsFound, epicSwordsFound, shieldsFound, epicShieldsFound, stepsTaken, zoneChanges, timesInCave;

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
		name = "Hero";
		
		/**
		 * STATS
		 * Initializing the stats variables
		 */
		
		bartucKills = 0;
		magicKills = 0;
		swordKills = 0;
		totalKills = 0;
		damageDealt = 0;
		magicDamageDealt = 0;
		swordDamageDealt = 0;
		fireBalls = 0;
		missedFireBalls = 0;
		damageTaken = 0;
		damageTakenBartuc = 0;
		healthPotsUsed = 0;
		manaPotsUsed = 0;
		gearFound = 0;
		epicGearFound = 0;
		swordsFound = 0;
		epicSwordsFound = 0;
		shieldsFound = 0;
		epicShieldsFound = 0;
		stepsTaken = 0;
		zoneChanges = 0;
		timesInCave = 0;
		
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

	public void setRoom(Room room){
		currentRoom = room;
	}

	public void startMoving(String dir){
		if(!mUp && !mDown && !mLeft && !mRight){
			if(dir.matches("up")){
				lastSprite = moveUp[1];
				if(currentRoom == Engine.centralZone && (getXTile() == 27 || getXTile() == 26) && getYTile() < 8 && getYTile() > 5){
					xPosition = (getXTile() - 11)*Engine.TILE_WIDTH;
					yPosition = 18*Engine.TILE_HEIGHT;
					currentRoom = currentRoom.getExit("cave");
					Engine.mainTheme = false;
					Engine.caveTheme = true;
					addTimesInCave(); // Adds total times in cave
					addZoneChanges(); // Adds total zone changes
					DrawGame.newZone = true;

				}
				else if(getYTile()-1 < 0){
					currentRoom = currentRoom.getExit("north");
					yPosition = 608;
					DrawGame.newZone = true;
					addZoneChanges(); // Adds total zone changes
				}
				else if(currentRoom.getCollisionMap()[getYTile()-1][getXTile()] != 1){
					mUp = true;
					moveCounter += 16;
					addStepsTaken(); // Adds total steps taken
				}
			}
			if(dir.matches("down")){
				lastSprite = moveDown[1];
				if(currentRoom == Engine.caveZone && getYTile()+1 > 19){
					if(getXTile() == 15){
						xPosition = 26*Engine.TILE_WIDTH;
					}
					else{
						xPosition = 27*Engine.TILE_WIDTH;
					}
					Engine.caveTheme = false;
					Engine.mainTheme = true;
					yPosition = 7*Engine.TILE_HEIGHT;
					currentRoom = currentRoom.getExit("south");
					DrawGame.newZone = true;
					addZoneChanges(); // Adds total zone changes
				}
				else if(getYTile()+1 > 19){
					currentRoom = currentRoom.getExit("south");
					yPosition = 0;
					DrawGame.newZone = true;
					addZoneChanges(); // Adds total zone changes
				}
				else if(currentRoom.getCollisionMap()[getYTile()+1][getXTile()] != 1){
					mDown = true;
					moveCounter += 16;
					addStepsTaken(); // Adds total steps taken
				}
			}
			if(dir.matches("left")){
				lastSprite = moveLeft[1];
				if(getXTile()-1 < 0){
					currentRoom = currentRoom.getExit("west");
					xPosition = 992;
					DrawGame.newZone = true;
					addZoneChanges(); // Adds total zone changes
				}
				else if(currentRoom.getCollisionMap()[getYTile()][getXTile()-1] != 1){
					mLeft = true;
					moveCounter += 16;
					addStepsTaken(); // Adds total steps taken
				}
			}
			if(dir.matches("right")){
				lastSprite = moveRight[1];
				if(getXTile()+1 > 31){
					currentRoom = currentRoom.getExit("east");
					xPosition = 0;
					DrawGame.newZone = true;
					addZoneChanges(); // Adds total zone changes
				}
				else if(currentRoom.getCollisionMap()[getYTile()][getXTile()+1] != 1){
					mRight = true;
					moveCounter += 16;
					addStepsTaken(); // Adds total steps taken
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
					int dealtDamage = DrawGame.character.getAttackDamage();
					DrawGame.character.addSwordDamageDealt(dealtDamage); // Adds total sword damage
					if(a.takeDamage(dealtDamage)){
						DrawGame.monsterList[DrawGame.monsterHash.get(currentRoom)].remove(j);
						DrawGame.character.increaseXP(25);
						DrawGame.character.addSwordKills(); // Adds total sword kills
					}
				}
				lastSprite = moveLeft[1];
			}
			else if(aRight){
				if((a.currentXTile() == getXTile() || a.currentXTile() - getXTile() == 1) && (a.currentYTile() == getYTile())){
					int dealtDamage = DrawGame.character.getAttackDamage();
					DrawGame.character.addSwordDamageDealt(dealtDamage); // Adds total sword damage
					if(a.takeDamage(dealtDamage)){
						DrawGame.monsterList[DrawGame.monsterHash.get(currentRoom)].remove(j);
						DrawGame.character.increaseXP(25);
						DrawGame.character.addSwordKills(); // Adds total sword kills
					}
				}
				lastSprite = moveRight[1];
			}
			else if(aUp){
				if((a.currentXTile() == getXTile()) && (a.currentYTile() == getYTile() || a.currentYTile() - getYTile() == -1)){
					int dealtDamage = DrawGame.character.getAttackDamage();
					DrawGame.character.addSwordDamageDealt(dealtDamage); // Adds total sword damage
					if(a.takeDamage(dealtDamage)){
						DrawGame.monsterList[DrawGame.monsterHash.get(currentRoom)].remove(j);
						DrawGame.character.increaseXP(25);
						DrawGame.character.addSwordKills(); // Adds total sword kills
					}
				}
				lastSprite = moveUp[1];
			}
			else if(aDown){
				if((a.currentXTile() == getXTile()) && (a.currentYTile() == getYTile() || a.currentYTile() - getYTile() == 1)){
					int dealtDamage = DrawGame.character.getAttackDamage();
					DrawGame.character.addSwordDamageDealt(dealtDamage); // Adds total sword damage
					if(a.takeDamage(dealtDamage)){
						DrawGame.monsterList[DrawGame.monsterHash.get(currentRoom)].remove(j);
						DrawGame.character.increaseXP(25);
						DrawGame.character.addSwordKills(); // Adds total sword kills
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
		Engine.levelUpSound = true;
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
				Engine.fireBallChargeSound = true;
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
		if(healthPotions > 0){
			if(currentHealth >= maxHealth){
				GameMain.infoBox.append("\n You're already at full health.");
			}
			else{
				currentHealth += maxHealth/2;
				if(currentHealth > maxHealth){
					currentHealth = maxHealth;
				}
				healthPotions--;
				if(!Engine.potionSound){
					Engine.potionSound = true;
				}
				addHealthPotsUsed(); // Adds total health pots used
				GameMain.infoBox.append(Engine.hpPotMessage);
			}
			
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
		if(manaPotions > 0){
			if(currentMana >= maxMana){
				GameMain.infoBox.append("\n You're already at full mana.");
			}
			else{
				currentMana += maxMana/2;
				if(currentMana > maxMana){
					currentMana = maxMana;
				}
				manaPotions--;
				if(!Engine.potionSound){
					Engine.potionSound = true;
				}
				addManaPotsUsed(); // Adds total mana pots used
				GameMain.infoBox.append(Engine.manaPotMessage);
			}
		}
		else{
			GameMain.infoBox.append(Engine.noManaPotsMessage);
		}
		GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void takeDamageBartuc(double damage){
		
	}

	public void takeDamage(double damage) {
		double damageReduction = (0.06 * armor) / (1 + 0.06 * armor);
		damage = damage * (1 - damageReduction);
		if (damage < 0) {
			damage = 0;
		}
		currentHealth -= damage;
		addDamageTaken(Math.ceil(damage)); // Adds total damage taken
		if(!Engine.hitSound){
			Engine.hitSound = true;
		}
	}

	public void getLoot(){
		Boolean epicGear = false;
		int drop = rand.nextInt(100);
		if(drop >= 84){
			Engine.lootSound = true;
			int quality = rand.nextInt(100);
			if(quality >= 84){
				epicGear = true;
			}
			int lootType = rand.nextInt(4);
			if(lootType == 2){
				if(epicGear){
					int epicMessage = rand.nextInt(3);
					int damageUpgrade = rand.nextInt(level*10) + 5*level;
					damage += damageUpgrade;
					addEpicSwordsFound(); // Adds total epic swords found
					GameMain.infoBox.append("\n You found a legendary sword" + Engine.epicGear[epicMessage] + "\n Damage increased by " + damageUpgrade + ".");
				}
				else{
					int damageUpgrade = rand.nextInt(level) + level; 
					damage += damageUpgrade;
					addSwordsFound(); // Adds total swords found
					GameMain.infoBox.append("\n You found a mighty sword from a fallen enemy! \n Damage increased by " + damageUpgrade + ".");
				}
			}
			else if(lootType == 3){
				if(epicGear){
					int epicMessage = rand.nextInt(3);
					int armorUpgrade = rand.nextInt(4*level) + 2*level;
					armor += armorUpgrade;
					addEpicShieldsFound(); // Adds total epic shields found
					GameMain.infoBox.append("\n You found a huge kite shield" + Engine.epicGear[epicMessage] + "\n Armor increased by " + armorUpgrade + ".");
				}
				else{
					int armorUpgrade = rand.nextInt(2*level) + level;
					armor += armorUpgrade;
					addShieldsFound(); // Adds total shields found
					GameMain.infoBox.append("\n You found a robust targe shield from a fallen enemy! \n Armor increased by " + armorUpgrade + ".");
				}
			}
			else{
				int gearType = rand.nextInt(6);
				if(epicGear){
					int epicMessage = rand.nextInt(3);
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
					addEpicGearFound(); // Adds total epic armor found
					GameMain.infoBox.append("\n You found " + Engine.gearTypeEpic[gearType] + Engine.epicGear[epicMessage] + "\n Armor increased by " + armorUpgrade + ".\n Health increased by " + healthUpgrade +  ".\n Mana increased by " + manaUpgrade +  ". \n Heath regeneration increased by " + GameMain.oneDigit.format(hpRegenUpgrade) +". \n Mana  regeneration increased by " + GameMain.oneDigit.format(manaRegenUpgrade));
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
						addGearFound(); // Adds total gear found
						GameMain.infoBox.append("\n You found " + Engine.gearTypeHealth[gearType] + " from a fallen enemy! \n Armor increased by " + armorUpgrade + ". \n Health increased by " + healthUpgrade + ". \n Heath regeneration increased by " + GameMain.oneDigit.format(hpRegenUpgrade));
					}
					else{
						int manaUpgrade = rand.nextInt(level) + level;
						maxMana += manaUpgrade;
						Double manaRegenUpgrade = (rand.nextInt(level)/10.0) + 0.1*level;
						manaRegenUpgrade.doubleValue();
						manaRegen += manaRegenUpgrade;
						addGearFound(); // Adds total gear found
						GameMain.infoBox.append("\n You found " + Engine.gearTypeMana[gearType] + " from a fallen enemy! \n Armor increased by " + armorUpgrade + ". \n Mana increased by " + manaUpgrade + ". \n Mana  regeneration increased by " + GameMain.oneDigit.format(manaRegenUpgrade));
					}
				}
			}
			GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
		}
		int potionDrop = rand.nextInt(4);
		if (potionDrop == 3) {
			Engine.potionPickupSound = true;
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
		Engine.playerDeathSound = true;
		Engine.hitSound = false;
		if(Engine.caveTheme){
			Engine.caveTheme = false;
		}
		if(Engine.mainThemePlayer.isPlaying()){
			Engine.mainThemePlayer.stop();
		}
		if(!Engine.mainTheme){
			Engine.mainTheme = true;
		}
		
		GameMain.infoBox.append(Engine.deathMessage);

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
		resetStats();
		DrawGame.reset();

		currentRoom = Engine.centralZone;
		setX(448);
		setY(416);

		//GameMain.infoBox.append("\n" + Engine.startMessage);
		GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
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
	
	/**
	 * STAT METHODS
	 * Methods for getting, setting and changing the statistics
	 */
	
	public int getBartucKills(){
		return bartucKills;
	}
	public void setBartucKills(int kills){
		bartucKills = kills;
	}
	public void addBartucKills(){
		bartucKills++;
	}
	public int getTotalKills(){
		totalKills = getMagicKills() + getSwordKills();
		return totalKills;
	}
	public void setTotalKills(int kills){
		totalKills = kills;
	}
	public int getMagicKills(){
		return magicKills;
	}
	public void setMagicKills(int kills){
		magicKills = kills;
	}
	public void addMagicKills(){
		magicKills++;
	}
	public int getSwordKills(){
		return swordKills;
	}
	public void setSwordKills(int kills){
		swordKills = kills;
	}
	public void addSwordKills(){
		swordKills++;
	}
	public int getDamageDealt(){
		damageDealt = getMagicDamageDealt() + getSwordDamageDealt();
		return damageDealt;
	}
	public void setDamageDealt(int damage){
		damageDealt = damage;
	}
	public int getMagicDamageDealt(){
		return magicDamageDealt;
	}
	public void setMagicDamageDealt(int damage){
		magicDamageDealt = damage;
	}
	public void addMagicDamageDealt(int damage){
		magicDamageDealt += damage;
	}
	public int getSwordDamageDealt(){
		return swordDamageDealt;
	}
	public void setSwordDamageDealt(int damage){
		swordDamageDealt = damage;
	}
	public void addSwordDamageDealt(int damage){
		swordDamageDealt += damage;
	}
	public int getFireBalls(){
		return fireBalls;
	}
	public void setFireBalls(int balls){
		fireBalls = balls;
	}
	public void addFireBalls(){
		fireBalls++;
	}
	public int getMissedFireBalls(){
		return missedFireBalls;
	}
	public void setMissedFireBalls(int balls){
		missedFireBalls = balls;
	}
	public void addMissedFireBalls(){
		missedFireBalls++;
	}
	public int getDamageTaken(){
		return damageTaken;
	}
	public void setDamageTaken(int damage){
		damageTaken = damage;
	}
	public void addDamageTaken(double damage){
		damageTaken += damage;
	}
	public int getDamageTakenBartuc(){
		return damageTakenBartuc;
	}
	public void setDamageTakenBartuc(int damage){
		damageTakenBartuc = damage;
	}
	public void addDamageTakenBartuc(int damage){
		damageTakenBartuc += damage;
	}
	public int getHealthPotsUsed(){
		return healthPotsUsed;
	}
	public void setHealthPotsUsed(int pots){
		healthPotsUsed = pots;
	}
	public void addHealthPotsUsed(){
		healthPotsUsed++;
	}
	public int getManaPotsUsed(){
		return manaPotsUsed;
	}
	public void setManaPotsUsed(int pots){
		manaPotsUsed = pots;
	}
	public void addManaPotsUsed(){
		manaPotsUsed++;
	}
	public int getGearFound(){
		return gearFound;
	}
	public void setGearFound(int gear){
		gearFound = gear;
	}
	public void addGearFound(){
		gearFound++;
	}
	public int getEpicGearFound(){
		return epicGearFound;
	}
	public void setEpicGearFound(int gear){
		epicGearFound = gear;
	}
	public void addEpicGearFound(){
		epicGearFound++;
	}
	public int getSwordsFound(){
		return swordsFound;
	}
	public void setSwordsFound(int swords){
		swordsFound = swords;
	}
	public void addSwordsFound(){
		swordsFound++;
	}
	public int getEpicSwordsFound(){
		return epicSwordsFound;
	}
	public void setEpicSwordsFound(int swords){
		epicSwordsFound = swords;
	}
	public void addEpicSwordsFound(){
		epicSwordsFound++;
	}
	public int getShieldsFound(){
		return shieldsFound;
	}
	public void setShieldsFound(int shields){
		shieldsFound = shields;
	}
	public void addShieldsFound(){
		shieldsFound++;
	}
	public int getEpicShieldsFound(){
		return epicShieldsFound;
	}
	public void setEpicShieldsFound(int shields){
		epicShieldsFound = shields;
	}
	public void addEpicShieldsFound(){
		epicShieldsFound++;
	}
	public int getStepsTaken(){
		return stepsTaken;
	}
	public void setStepsTaken(int steps){
		stepsTaken = steps;
	}
	public void addStepsTaken(){
		stepsTaken++;
	}
	public int getZoneChanges(){
		return zoneChanges;
	}
	public void setZoneChanges(int changes){
		zoneChanges = changes;
	}
	public void addZoneChanges(){
		zoneChanges++;
	}
	public int getTimesInCave(){
		return timesInCave;
	}
	public void setTimesInCave(int times){
		timesInCave = times;
	}
	public void addTimesInCave(){
		timesInCave++;
	}
	public void resetStats(){
		bartucKills = 0;
		magicKills = 0;
		swordKills = 0;
		totalKills = 0;
		damageDealt = 0;
		magicDamageDealt = 0;
		swordDamageDealt = 0;
		fireBalls = 0;
		missedFireBalls = 0;
		damageTaken = 0;
		damageTakenBartuc = 0;
		healthPotsUsed = 0;
		manaPotsUsed = 0;
		gearFound = 0;
		epicGearFound = 0;
		swordsFound = 0;
		epicSwordsFound = 0;
		shieldsFound = 0;
		epicShieldsFound = 0;
		stepsTaken = 0;
		zoneChanges = 0;
	}
	public String getStatMessage(){
		statsMessage = "\n\n Feats of strength \n\n Combat\n\n You have slain "
				+ getTotalKills()
				+ " of bartucs minions. "
				+ getSwordKills()
				+ " by sword and "
				+ getMagicKills()
				+ " by magic \n You have dealt "
				+ getDamageDealt()
				+ " damage. "
				+ getSwordDamageDealt()
				+ " by sword and "
				+ getMagicDamageDealt()
				+ " by magic \n You have conjured "
				+ getFireBalls()
				+ " fireballs. "
				+ getMissedFireBalls()
				+ " of them missed their target \n You have slain the mighty Bartuc "
				+ getBartucKills() + " times \n You have taken "
				+ getDamageTaken()
				+ " damage by bartucs minions and "
				+ getDamageTakenBartuc()
				+ " by Bartuc himself \n You have used "
				+ getHealthPotsUsed() + " health potions and "
				+ getManaPotsUsed()
				+ " mana potions \n\n Loot \n\n You have looted "
				+ getGearFound()
				+ " pieces of armor \n You have looted "
				+ getEpicGearFound()
				+ " pieces of legendary armor \n You have looted "
				+ getSwordsFound()
				+ " swords \n You have looted "
				+ getEpicSwordsFound()
				+ " legendary swords \n You have looted "
				+ getShieldsFound()
				+ " shields \n You have looted "
				+ getEpicShieldsFound()
				+ " legendary shields \n\n Exploration\n\n You have taken "
				+ getStepsTaken()
				+ " steps \n You have walked between zones "
				+ getZoneChanges() + " times \n You have entered bartucs cave "
				+ getTimesInCave() + " times";
		
		return statsMessage;
	}
}
