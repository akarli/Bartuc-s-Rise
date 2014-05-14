import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;


public class Character {
	private BufferedImage character, levelup;
	private int xPosition, yPosition, baseDamage, level, armor, currentExperience, maxExperience, manaCounter, healthCounter, healthPotions, manaPotions;
	private double manaRegen, hpRegen, currentHealth, maxHealth, currentMana, maxMana, baseHealth, baseMana, baseManaRegen, baseHpRegen;
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
	private BufferedImage[] healing;
	private BufferedImage[] manas;
	private BufferedImage[] loots;
	private BufferedImage lastSprite;
	private BufferedImage charAttack;
	private BufferedImage heal;
	private BufferedImage mana;
	private BufferedImage loot;
	private String statsMessage;
	private int currentSprite = 0;
	private int animationCounter=1;
	private int x = 0;
	private int moveCounter = 0;
	private int sprite = 0;
	private int hpSprite =0;
	private int manaSprite = 0;
	private int lootSprite = 0;
	private String name;
	public ArrayList<Item> inventory;
	private Item helm;
	private Item boots;
	private Item gloves;
	private Item pants;
	private Item shoulders;
	private Item chest;
	private Item sword;
	private Item shield;
	private boolean levelingUp, castingFireBall, aLeft, aRight, aUp, aDown, mLeft, mRight, mUp, mDown, useHpPot, useManaPot, findLoot;
	private boolean bartucEngage = false;
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
		character = loadCharacterImage("Graphics\\charwalk.png");
		levelup = loadCharacterImage("Graphics\\levelup.png");
		charAttack = loadCharacterImage("Graphics\\charattack.png");
		heal = loadCharacterImage("Graphics\\healing.png");
		mana = loadCharacterImage("Graphics\\mana.png");
		loot = loadCharacterImage("Graphics\\loot.png");
		moveUp = new BufferedImage[4];
		moveDown = new BufferedImage[4];
		moveRight = new BufferedImage[4];
		moveLeft = new BufferedImage[4];
		levelUp = new BufferedImage[39];
		attackUp = new BufferedImage[4];
		attackDown = new BufferedImage[4];
		attackLeft = new BufferedImage[4];
		attackRight = new BufferedImage[4];
		healing = new BufferedImage[15];
		manas = new BufferedImage[20];
		loots = new BufferedImage[16];
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
		x = 0;
		int y = 0;
		for(int i = 0; i < 15; i++){
			if(i == 5 || i == 10){
				y++;
				x=0;
			}
			healing[i] = heal.getSubimage(x*96, y*96, 96, 96);
			x++;
		}
		x = 0;
		y = 0;
		for(int i = 0; i < 20; i++){
			if(i == 5 || i == 10 || i == 15){
				y++;
				x=0;
			}
			manas[i] = mana.getSubimage(x*96, y*96, 96, 96);
			x++;
		}

		x = 0;
		y = 0;
		for(int i = 0; i < 16; i++){
			if(i == 4 || i == 8 || i == 12){
				y++;
				x=0;
			}
			loots[i] = loot.getSubimage(x*30, y*30, 30, 30);
			x++;
		}

		for(int i = 0; i < 39 ;i++){
			levelUp[i] = levelup.getSubimage(0 + (i*128), 0, 128, 128);
		}

		sword = Engine.startSword;
		sword.setDamage(10);

		helm = Engine.startHelm;
		helm.setArmor(0);
		helm.setCritDmg(5);

		boots = Engine.startBoots;
		boots.setArmor(0);
		boots.setDodge(1);

		gloves = Engine.startGloves;
		gloves.setArmor(0);
		gloves.setCritChance(1);

		pants = Engine.startPants;
		pants.setArmor(0);
		pants.setBonusHealth(0);
		pants.setHPregen(0);

		shoulders = Engine.startShoulders;
		shoulders.setArmor(0);
		shoulders.setBonusMana(0);
		shoulders.setManaRegen(0);

		chest = Engine.startChest;
		chest.setArmor(0);
		chest.setBonusHealth(0);
		chest.setHPregen(0);

		shield = Engine.startShield;
		shield.setArmor(3);

		xPosition = 448;
		yPosition = 416;
		baseHealth = 100.0;
		currentHealth = 100.0;

		setArmor();
		setHP(100.0);
		level = 1;
		baseDamage = 10;
		currentExperience = 0;
		maxExperience = level*100;
		currentRoom = Engine.centralZone;
		currentMana = 100.0;
		setMana(100.0);
		healthPotions = 0;
		manaCounter = 60;
		healthCounter = 60;
		manaPotions = 0;
		baseHpRegen = 1.0;
		baseManaRegen = 1.0;
		name = "Hero";
		inventory = new ArrayList<Item>();		
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
					DrawGame.clearDeadMonster(currentRoom);
					currentRoom = currentRoom.getExit("cave");
					Engine.mainThemeSound = false;
					Engine.caveThemeSound = true;
					addTimesInCave(); // Adds total times in cave
					addZoneChanges(); // Adds total zone changes
					DrawGame.newZone = true;
				}
				else if(getYTile()-1 < 0){
					DrawGame.clearDeadMonster(currentRoom);
					currentRoom = currentRoom.getExit("north");
					yPosition = 608;
					DrawGame.newZone = true;
					addZoneChanges(); // Adds total zone changes
				}
				else if(currentRoom.getCollisionMap()[getYTile()-1][getXTile()] != 1){
					mUp = true;
					moveCounter += 16;
					addStepsTaken(); // Adds total steps taken
					if(!Engine.walkingSound){
						Engine.walkingSound = true;
					}
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
					Engine.caveThemeSound = false;
					Engine.bartucThemeSound = false;
					Engine.mainThemeSound = true;
					if(DrawGame.bartuc.aggro && DrawGame.bartuc.alive){
						DrawGame.bartuc.reset();
						DrawGame.bartuc.aggro = false;
						bartucEngage = false;
						GameMain.infoBox.append(Engine.bartucFledMessage);
						GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
						Engine.writingSound = true;
					}
					if(!DrawGame.bartuc.alive){
						GameMain.infoBox.append(Engine.bartucSpawnMessage);
						Engine.writingSound = true;
						GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
						DrawGame.bartuc.spawn(getBartucKills());
					}
					yPosition = 7*Engine.TILE_HEIGHT;
					DrawGame.clearDeadMonster(currentRoom);
					currentRoom = currentRoom.getExit("south");
					DrawGame.newZone = true;
					addZoneChanges(); // Adds total zone changes
				}
				else if(getYTile()+1 > 19){
					DrawGame.clearDeadMonster(currentRoom);
					currentRoom = currentRoom.getExit("south");
					yPosition = 0;
					DrawGame.newZone = true;
					addZoneChanges(); // Adds total zone changes
				}
				else if(currentRoom.getCollisionMap()[getYTile()+1][getXTile()] != 1){
					mDown = true;
					moveCounter += 16;
					addStepsTaken(); // Adds total steps taken
					if(!Engine.walkingSound){
						Engine.walkingSound = true;
					}
				}
			}
			if(dir.matches("left")){
				lastSprite = moveLeft[1];
				if(getXTile()-1 < 0){
					DrawGame.clearDeadMonster(currentRoom);
					currentRoom = currentRoom.getExit("west");
					xPosition = 992;
					DrawGame.newZone = true;
					addZoneChanges(); // Adds total zone changes
					if(!Engine.walkingSound){
						Engine.walkingSound = true;
					}
				}
				else if(currentRoom.getCollisionMap()[getYTile()][getXTile()-1] != 1){
					mLeft = true;
					moveCounter += 16;
					addStepsTaken(); // Adds total steps taken
					if(!Engine.walkingSound){
						Engine.walkingSound = true;
					}
				}
			}
			if(dir.matches("right")){
				lastSprite = moveRight[1];
				if(getXTile()+1 > 31){
					DrawGame.clearDeadMonster(currentRoom);
					currentRoom = currentRoom.getExit("east");
					xPosition = 0;
					DrawGame.newZone = true;
					addZoneChanges(); // Adds total zone changes
				}
				else if(currentRoom.getCollisionMap()[getYTile()][getXTile()+1] != 1){
					mRight = true;
					moveCounter += 16;
					addStepsTaken(); // Adds total steps taken
					if(!Engine.walkingSound){
						Engine.walkingSound = true;
					}
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
		Engine.walkingSound = false;
		Engine.walkPlayer.stop();
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
				if(!a.dead && (a.currentXTile() == getXTile() || a.currentXTile() - getXTile() == -1) && a.currentYTile() == getYTile()){
					int dealtDamage = DrawGame.character.getAttackDamage();
					DrawGame.character.addSwordDamageDealt(dealtDamage); // Adds total sword damage
					if(a.takeDamage(dealtDamage)){
						DrawGame.character.increaseXP(25);
						DrawGame.character.addSwordKills(); // Adds total sword kills
					}
				}
				lastSprite = moveLeft[1];
			}
			else if(aRight){
				if(!a.dead && (a.currentXTile() == getXTile() || a.currentXTile() - getXTile() == 1) && (a.currentYTile() == getYTile())){
					int dealtDamage = DrawGame.character.getAttackDamage();
					DrawGame.character.addSwordDamageDealt(dealtDamage); // Adds total sword damage
					if(a.takeDamage(dealtDamage)){
						DrawGame.character.increaseXP(25);
						DrawGame.character.addSwordKills(); // Adds total sword kills
					}
				}
				lastSprite = moveRight[1];
			}
			else if(aUp){
				if(!a.dead && (a.currentXTile() == getXTile()) && (a.currentYTile() == getYTile() || a.currentYTile() - getYTile() == -1)){
					int dealtDamage = DrawGame.character.getAttackDamage();
					DrawGame.character.addSwordDamageDealt(dealtDamage); // Adds total sword damage
					if(a.takeDamage(dealtDamage)){
						DrawGame.character.increaseXP(25);
						DrawGame.character.addSwordKills(); // Adds total sword kills
					}
				}
				lastSprite = moveUp[1];
			}
			else if(aDown){
				if(!a.dead && (a.currentXTile() == getXTile()) && (a.currentYTile() == getYTile() || a.currentYTile() - getYTile() == 1)){
					int dealtDamage = DrawGame.character.getAttackDamage();
					DrawGame.character.addSwordDamageDealt(dealtDamage); // Adds total sword damage
					if(a.takeDamage(dealtDamage)){
						DrawGame.character.increaseXP(25);
						DrawGame.character.addSwordKills(); // Adds total sword kills
					}
				}
				lastSprite = moveDown[1];
			}
		}
		if (aLeft && currentRoom == Engine.caveZone && DrawGame.bartuc.alive) {
			if ((DrawGame.bartuc.getXTile() == getXTile() || DrawGame.bartuc
					.getXTile() - getXTile() == -1 || DrawGame.bartuc
					.getXTile() - getXTile() == -2) && (DrawGame.bartuc.getYTile() - getYTile() == -1 || DrawGame.bartuc.getYTile() == getYTile())) {
				DrawGame.bartuc
				.takeDamage(DrawGame.character.getAttackDamage());
			}
		}
		if (aRight && currentRoom == Engine.caveZone && DrawGame.bartuc.alive) {
			if ((DrawGame.bartuc.getXTile() == getXTile() || DrawGame.bartuc
					.getXTile() - getXTile() == 1)  && (DrawGame.bartuc.getYTile() - getYTile() == -1 || DrawGame.bartuc.getYTile() == getYTile())) {
				DrawGame.bartuc
				.takeDamage(DrawGame.character.getAttackDamage());
			}
		}
		if (aUp && currentRoom == Engine.caveZone && DrawGame.bartuc.alive) {
			if ((DrawGame.bartuc.getYTile() == getYTile() || DrawGame.bartuc
					.getYTile() - getYTile() == -1) && (DrawGame.bartuc.getXTile() - getXTile() == -1 || DrawGame.bartuc.getXTile() == getXTile())) {
				DrawGame.bartuc
				.takeDamage(DrawGame.character.getAttackDamage());
			}
		}
		if (aDown && currentRoom == Engine.caveZone && DrawGame.bartuc.alive) {
			if ((DrawGame.bartuc.getYTile() == getYTile() || DrawGame.bartuc
					.getYTile() - getYTile() == +1) && (DrawGame.bartuc.getXTile() - getXTile() == -1 || DrawGame.bartuc.getXTile() == getXTile())) {
				DrawGame.bartuc
				.takeDamage(DrawGame.character.getAttackDamage());
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

		baseHealth += Engine.HP_LVL_UP;
		setHP(baseHealth);
		baseMana += Engine.MANA_LVL_UP;
		setMana(baseMana);
		baseDamage += Engine.DAMAGE_LVL_UP;
		baseHpRegen += Engine.HPREGEN_LVL_UP;
		baseManaRegen += Engine.MANAREGEN_LVL_UP;

		currentHealth = getMaxHP();
		currentMana = getMaxMana();

		GameMain.infoBox.append(Engine.levelUpMessage);
		GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
		levelingUp = true;
	}

	public void castFireBall(String dir){
		if(!castingFireBall){
			if(currentMana-30 >= 0){
				Engine.fireBallChargeSound = true;
				currentMana -=30;
				castingFireBall = true;
				if(dir.matches("right")){
					eldBoll = new FireBall(xPosition + 32, yPosition, currentRoom);
					eldBoll.cast();
					lastSprite = moveRight[1];
				}
				if(dir.matches("left")){
					eldBoll = new FireBall(xPosition - 32, yPosition, currentRoom);
					eldBoll.cast();
					lastSprite = moveLeft[1];
				}
				if(dir.matches("up")){
					eldBoll = new FireBall(xPosition, yPosition - 32, currentRoom);
					eldBoll.cast();
					lastSprite = moveUp[1];
				}
				if(dir.matches("down")){
					eldBoll = new FireBall(xPosition, yPosition + 32, currentRoom);
					eldBoll.cast();
					lastSprite = moveDown[1];
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

	public double getCurrHP(){
		return currentHealth;
	}

	public double getMaxHP(){
		return maxHealth;
	}

	public void setHP(Double double1){
		baseHealth = double1;
		maxHealth = baseHealth + chest.getBonusHealth() + pants.getBonusHealth();
	}

	public int getDamage(){
		return sword.getDamage() + baseDamage;
	}

	public int getAttackDamage(){
		int randDamage = rand.nextInt((getDamage()/10))+(getDamage() - getDamage()/20);
		int crit = rand.nextInt(100);
		if(crit >= gloves.getCritChance()){
			GameMain.infoBox.append("\n Critical hit!");
			return (randDamage*(helm.getCritDamage()+100))/100;
		}
		return randDamage;
	}

	public void setDamage(int damage){
		this.baseDamage = damage;
	}

	public int getArmor(){
		return armor;
	}

	public void setArmor(){
		this.armor = shield.getArmor() + helm.getArmor() + chest.getArmor() + pants.getArmor() + gloves.getArmor() + boots.getArmor() + shoulders.getArmor();
	}

	public int getXP(){
		return currentExperience;
	}

	public double getHPRegen(){
		return baseHpRegen + pants.getHpRegen() + chest.getHpRegen();
	}

	public void setHPRegen(Double double1){
		hpRegen += double1;
	}

	public double getManaRegen(){
		return baseManaRegen + shoulders.getManaRegen();
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
		return baseMana + shoulders.getBonusMana();
	}

	public void setMana(Double double1){
		baseMana = double1;
		maxMana = baseMana + shoulders.getBonusMana();
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
				useHpPot = true;
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
				useManaPot = true;
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

	public void takeDamageBartuc(double damage) {
		double damageReduction = (0.06 * armor) / (1 + 0.06 * armor);
		damage = damage * (1 - damageReduction);
		if (damage < 0) {
			damage = 0;
		}
		currentHealth -= damage;
		addDamageTakenBartuc(Math.ceil(damage)); // Adds total damage taken from
		// bartuc
		if (!Engine.hitSound) {
			Engine.hitSound = true;
		}
	}

	public void takeDamage(double damage) {
		int dodge = boots.getDodge();
		int dodgeRand = rand.nextInt(100);
		if(!(dodge >= dodgeRand)){
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
		else{
			GameMain.infoBox.append("\n You dodged");
			GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
		}
	}

	public void printInventory(){
		if(inventory.size() == 0){
			GameMain.listModel.addElement(" Your inventory is empty.");
		}
		else{
			for(int i = 0; i < inventory.size(); i++){
				if(inventory.get(i).getType().matches("sword")){
					GameMain.listModel.addElement((i+1) + ". Sword - " + GameMain.decimals.format(inventory.get(i).getDamage()) + " damage.\n");
				}
				if(inventory.get(i).getType().matches("shield")){
					GameMain.listModel.addElement((i+1) + ". Shield - " + GameMain.decimals.format(inventory.get(i).getArmor()) + " armor.\n");
				}
				if(inventory.get(i).getType().matches("chest")){
					GameMain.listModel.addElement((i+1) + ". Chest - " + GameMain.decimals.format(inventory.get(i).getArmor()) + " armor, " + GameMain.decimals.format(inventory.get(i).getBonusHealth()) + " hp, " + GameMain.oneDigit.format(inventory.get(i).getHpRegen()) + " hp/s.\n");
				}
				if(inventory.get(i).getType().matches("pants")){
					GameMain.listModel.addElement((i+1) + ". Legs - " + GameMain.decimals.format(inventory.get(i).getArmor()) + " armor, " + GameMain.decimals.format(inventory.get(i).getBonusHealth()) + " health, " + GameMain.oneDigit.format(inventory.get(i).getHpRegen()) + " hp/s.\n");
				}
				if(inventory.get(i).getType().matches("shoulders")){
					GameMain.listModel.addElement((i+1) + ". Shoulders - " + GameMain.decimals.format(inventory.get(i).getArmor()) + " armor, " + GameMain.decimals.format(inventory.get(i).getBonusMana()) + " mana, " + GameMain.oneDigit.format(inventory.get(i).getManaRegen()) + " mana/s.\n");
				}
				if(inventory.get(i).getType().matches("helm")){
					GameMain.listModel.addElement((i+1) + ". Helmet - " + GameMain.decimals.format(inventory.get(i).getArmor()) + " armor,  " +  GameMain.decimals.format(inventory.get(i).getBonusStat()) + "% critical hit damage.\n");
				}
				if(inventory.get(i).getType().matches("gloves")){
					GameMain.listModel.addElement((i+1) + ". Gloves - " + GameMain.decimals.format(inventory.get(i).getArmor()) + " armor, " +  GameMain.decimals.format(inventory.get(i).getBonusStat()) + "% critical hit chance.\n");
				}
				if(inventory.get(i).getType().matches("boots")){
					GameMain.listModel.addElement((i+1) + ". Boots - " + GameMain.decimals.format(inventory.get(i).getArmor()) + " armor, " +  GameMain.decimals.format(inventory.get(i).getBonusStat()) + "% dodge chance.\n");
				}
			} 
		}
		//GameMain.inventoryBox.setCaretPosition(GameMain.inventoryBox.getDocument().getLength());
	}
	public void clearInventory(){
		if(inventory.size() > 0){
			inventory.clear();
			GameMain.infoBox.append("\n Inventory emptied.");
		}
		else{
			GameMain.infoBox.append("\n Your inventory is already empty.");
		}
		GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
		GameMain.listModel.clear();
		printInventory();
	}

	public void removeItemFromInventory(int index){
		index = index -1;
		if(inventory.size() > index  && inventory.size() > 0){
			inventory.remove(index);
			GameMain.infoBox.append("\n Item removed");
			GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
		}
		else{
			GameMain.infoBox.append("\n Not a valid index");
			GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
		}
		GameMain.listModel.clear();
		printInventory();
	}

	public void printEquipment(){
		GameMain.equipmentBox.append(" Your equipped items:");
		GameMain.equipmentBox.append("\n Helmet - " + GameMain.decimals.format(helm.getArmor()) + " armor, " +  GameMain.decimals.format(helm.getBonusStat()) + "% critical hit damage.");
		GameMain.equipmentBox.append("\n Chest - " + GameMain.decimals.format(chest.getArmor()) + " armor, " + GameMain.decimals.format(chest.getBonusHealth()) + " hp, " + GameMain.oneDigit.format(chest.getHpRegen()) + " hp/s.");
		GameMain.equipmentBox.append("\n Gloves - " + GameMain.decimals.format(gloves.getArmor()) + " armor, " + GameMain.decimals.format(gloves.getBonusStat()) + "% critical hit chance.");
		GameMain.equipmentBox.append("\n Shoulders - " + GameMain.decimals.format(shoulders.getArmor()) + " armor, " + GameMain.decimals.format(shoulders.getBonusMana()) + " mana, " + GameMain.oneDigit.format(shoulders.getManaRegen()) + " mana/s.");
		GameMain.equipmentBox.append("\n Boots - " + GameMain.decimals.format(boots.getArmor()) + " armor and " + GameMain.decimals.format(boots.getBonusStat()) + "% dodge chance.");
		GameMain.equipmentBox.append("\n Legs - " + GameMain.decimals.format(pants.getArmor()) + " armor, " + GameMain.decimals.format(pants.getBonusHealth()) + " hp, " + GameMain.oneDigit.format(pants.getHpRegen()) + " hp/s.");
		GameMain.equipmentBox.append("\n Shield - " + GameMain.decimals.format(shield.getArmor()) + " armor.");
		GameMain.equipmentBox.append("\n Sword - " + GameMain.decimals.format(sword.getDamage()) + " damage.");
		GameMain.equipmentBox.setCaretPosition(GameMain.equipmentBox.getDocument().getLength());

	}

	public void changeItem(int index){
		index = index - 1;
		if(inventory.size() > index  && inventory.size() > 0){
			Item temp;
			if(inventory.get(index).getType().matches("helm")){
				temp = helm;
				helm = inventory.get(index);
				inventory.set(index, temp);
			}
			if(inventory.get(index).getType().matches("gloves")){
				temp = gloves;
				gloves= inventory.get(index);
				inventory.set(index, temp);
			}
			if(inventory.get(index).getType().matches("chest")){
				temp = chest;
				chest = inventory.get(index);
				setHP(baseHealth);
				hpRegen += chest.getHpRegen();
				inventory.set(index, temp);
			}
			if(inventory.get(index).getType().matches("pants")){
				temp = pants;
				pants = inventory.get(index);
				setHP(baseHealth);
				hpRegen += chest.getHpRegen();
				inventory.set(index, temp);
			}
			if( inventory.get(index).getType().matches("boots")){
				temp = boots;
				boots = inventory.get(index);
				inventory.set(index, temp);
			}
			if(inventory.get(index).getType().matches("shoulders")){
				temp = shoulders;
				shoulders = inventory.get(index);
				setMana(baseMana);
				inventory.set(index, temp);
			}
			if(inventory.get(index).getType().matches("sword")){
				temp = sword;
				sword = inventory.get(index);
				inventory.set(index, temp);
			}
			if(inventory.get(index).getType().matches("shield")){
				temp = shield;
				shield = inventory.get(index);
				inventory.set(index, temp);
			}
			setArmor();
			GameMain.infoBox.append("\n You equipped the new item.");
			GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
		}
		else{
			GameMain.infoBox.append("\n Not a valid index.");
			GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
		}
		GameMain.listModel.clear();
		printInventory();
	}

	public void getLoot(){
		Boolean epicGear = false;
		int drop = rand.nextInt(100);
		int dropMessage = 0;
		if(drop >= 0){
			findLoot = true;
			Engine.lootSound = true;
			int quality = rand.nextInt(100);
			if(quality >= 84){
				epicGear = true;
				dropMessage = rand.nextInt(3);
			}
			int type = rand.nextInt(8);
			Item newItem = new Item(level, epicGear, Engine.gearType[type]);
			inventory.add(newItem);
			if(newItem.getType().matches("sword")){
				if(epicGear){
					GameMain.infoBox.append("\n You found a legendary sword" + Engine.epicGear[dropMessage] + "\n It increases damage by " + GameMain.decimals.format(newItem.getDamage()) + ".");
					addEpicSwordsFound();
				}
				else{
					GameMain.infoBox.append("\n You found a mighty sword from a fallen enemy. \n It increases damage by " + GameMain.decimals.format(newItem.getDamage()) + ".");
					addSwordsFound();
				}
			}
			if(newItem.getType().matches("shield")){
				if(epicGear){
					GameMain.infoBox.append("\n You found a magnificent kite shield" + Engine.epicGear[dropMessage] + "\n It increases armor by " + GameMain.decimals.format(newItem.getArmor()) + ".");
					addEpicShieldsFound();
				}
				else{
					GameMain.infoBox.append("\n You found a robust targe shield from a fallen enemy. \n It increases armor by " + GameMain.decimals.format(newItem.getArmor()) + ".");
					addShieldsFound();
				}
			}
			if(newItem.getType().matches("helm")){
				if(epicGear){
					GameMain.infoBox.append("\n You found a glorious barbute" + Engine.epicGear[dropMessage] + "\n It increases armor by " + GameMain.decimals.format(newItem.getArmor()) + " and critical hit damage by " + newItem.getBonusStat() + "%.");
					addEpicGearFound();
				}
				else{
					GameMain.infoBox.append("\n You found a barbute from a fallen enemy. \n It increases armor by " + GameMain.decimals.format(newItem.getArmor()) + " and critical hit damage by " + newItem.getBonusStat() + "%.");
					addGearFound();
				}
			}
			if(newItem.getType().matches("chest")){
				if(epicGear){
					GameMain.infoBox.append("\n You found a glorious cuirass" + Engine.epicGear[dropMessage] + "\n It increases armor by " + GameMain.decimals.format(newItem.getArmor()) + ", health by " + GameMain.decimals.format(newItem.getBonusHealth()) + " and health regeneration by " + GameMain.oneDigit.format(newItem.getHpRegen()) +  " per second.");
					addEpicGearFound();
				}
				else{
					GameMain.infoBox.append("\n You found a cuirass from a fallen enemy.\n It increases armor by " + GameMain.decimals.format(newItem.getArmor()) + ", health by " + GameMain.decimals.format(newItem.getBonusHealth()) + " and health regeneration by " + GameMain.oneDigit.format(newItem.getHpRegen()) + " per second.");
					addGearFound();
				}
			}
			if(newItem.getType().matches("pants")){
				if(epicGear){
					GameMain.infoBox.append("\n You found a pair of glorious greaves" + Engine.epicGear[dropMessage] + "\n It increases armor by " + GameMain.decimals.format(newItem.getArmor()) + ", health by " + GameMain.decimals.format(newItem.getBonusHealth()) + " and health regeneration by " + GameMain.oneDigit.format(newItem.getHpRegen()) +  " per second.");
					addEpicGearFound();
				}
				else{
					GameMain.infoBox.append("\n You found a pair of greaves from a fallen enemy.\n It increases armor by " + GameMain.decimals.format(newItem.getArmor()) + ", health by " + GameMain.decimals.format(newItem.getBonusHealth()) + " and health regeneration by " + GameMain.oneDigit.format(newItem.getHpRegen()) + " per second.");
					addGearFound();
				}
			}
			if(newItem.getType().matches("gloves")){
				if(epicGear){
					GameMain.infoBox.append("\n You found a pair of glorious gauntlets" + Engine.epicGear[dropMessage] + "\n It increases armor by " + GameMain.decimals.format(newItem.getArmor()) + " and critical hit chance by " + GameMain.decimals.format(newItem.getBonusStat())  +  "%.");
					addEpicGearFound();
				}
				else{
					GameMain.infoBox.append("\n You found a pair of gauntlets from a fallen enemy.\n It increases armor by " + GameMain.decimals.format(newItem.getArmor()) + " and critical hit chance by " + GameMain.decimals.format(newItem.getBonusStat())  +  "%.");
					addGearFound();
				}
			}
			if(newItem.getType().matches("boots")){
				if(epicGear){
					GameMain.infoBox.append("\n You found a pair of glorious sabatons" + Engine.epicGear[dropMessage] + "\n It increases armor by " + GameMain.decimals.format(newItem.getArmor()) + " and dodge chance by " + GameMain.decimals.format(newItem.getBonusStat())  +  "%.");
					addEpicGearFound();
				}
				else{
					GameMain.infoBox.append("\n You found a pair of sabatons from a fallen enemy.\n It increases armor by " + GameMain.decimals.format(newItem.getArmor()) + " and dodge chance by " + GameMain.decimals.format(newItem.getBonusStat())  +  "%.");
					addGearFound();
				}
			}
			if(newItem.getType().matches("shoulders")){
				if(epicGear){
					GameMain.infoBox.append("\n You found a pair of glorious pauldrons" + Engine.epicGear[dropMessage] + "\n It increases armor by " + GameMain.decimals.format(newItem.getArmor()) + ", mana by " + GameMain.decimals.format(newItem.getBonusMana()) + " and mana regeneration by " + GameMain.oneDigit.format(newItem.getManaRegen()) +  " per second.");
					addEpicGearFound();
				}
				else{
					GameMain.infoBox.append("\n You found a pair of pauldrons from a fallen enemy.\n It increases armor by " + GameMain.decimals.format(newItem.getArmor()) + ", mana by " + GameMain.decimals.format(newItem.getBonusMana()) + " and mana regeneration by " + GameMain.oneDigit.format(newItem.getManaRegen()) + " per second.");
					addGearFound();
				}
				GameMain.listModel.clear();
				printInventory();
			}

			GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
			GameMain.infoBox.append("\n Your currently equipped ");
			if(newItem.getType().matches("chest")){
				GameMain.infoBox.append("chest armor gives you " + GameMain.decimals.format(chest.getArmor()) + " armor, " + GameMain.decimals.format(chest.getBonusHealth()) + " health and \n " + GameMain.oneDigit.format(chest.getHpRegen()) + " health regeneration per second.");
			}
			if(newItem.getType().matches("gloves")){
				GameMain.infoBox.append("gloves gives you " +  GameMain.decimals.format(gloves.getArmor()) + " armor and " + GameMain.decimals.format(gloves.getBonusStat()) + "% critical hit chance.");
			}
			if(newItem.getType().matches("pants")){
				GameMain.infoBox.append("leg armor gives you " + GameMain.decimals.format(pants.getArmor()) + " armor, " + GameMain.decimals.format(pants.getBonusHealth()) + " health and \n " + GameMain.oneDigit.format(pants.getHpRegen()) + " health regeneration per second.");
			}
			if(newItem.getType().matches("boots")){
				GameMain.infoBox.append("boots gives you " +  GameMain.decimals.format(boots.getArmor()) + " armor and " + GameMain.decimals.format(boots.getBonusStat()) + "% dodge chance.");
			}
			if(newItem.getType().matches("helm")){
				GameMain.infoBox.append("helmet gives you " +  GameMain.decimals.format(helm.getArmor()) + " armor and " + GameMain.decimals.format(helm.getBonusStat()) + "% critical hit damage.");
			}
			if(newItem.getType().matches("shoulders")){
				GameMain.infoBox.append("shoulder armor gives you " + GameMain.decimals.format(shoulders.getArmor()) + " armor, " + GameMain.decimals.format(shoulders.getBonusMana()) + " mana and \n " + GameMain.oneDigit.format(shoulders.getManaRegen()) + " mana regeneration per second.");
			}
			if(newItem.getType().matches("shield")){
				GameMain.infoBox.append("shield gives you " + shield.getArmor() + " armor.");
			}
			if(newItem.getType().matches("sword")){
				GameMain.infoBox.append("sword gives you " + sword.getDamage() + " damage.");
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
		GameMain.listModel.clear();
		printInventory();
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

	public BufferedImage getHpPotImage(){
		if(hpSprite == 30){
			hpSprite = 0;
			useHpPot = false;
			return healing[14];
		}
		return healing[hpSprite/2];
	}

	public BufferedImage getManaPotImage(){
		if(manaSprite == 40){
			manaSprite = 0;
			useManaPot = false;
		}
		return manas[manaSprite/2];
	}

	public BufferedImage getLootImage(){
		if(lootSprite == 32){
			lootSprite = 0;
			findLoot = false;
		}
		return loots[lootSprite/2];
	}
	public void die(){		
		Engine.playerDeathSound = true;
		Engine.hitSound = false;
		if(Engine.caveThemeSound){
			Engine.caveThemeSound = false;
		}
		if(Engine.bartucThemeSound){
			Engine.bartucThemeSound = false;
		}
		if(Engine.mainThemePlayer.isPlaying()){
			Engine.mainThemePlayer.stop();
		}
		if(!Engine.mainThemeSound){
			Engine.mainThemeSound = true;
		}
		sword = Engine.startSword;
		sword.setDamage(10);

		helm = Engine.startHelm;
		helm.setArmor(0);
		helm.setCritDmg(5);

		boots = Engine.startBoots;
		boots.setArmor(0);
		boots.setDodge(1);

		gloves = Engine.startGloves;
		gloves.setArmor(0);
		gloves.setCritChance(1);

		pants = Engine.startPants;
		pants.setArmor(0);
		pants.setBonusHealth(0);
		pants.setHPregen(0);

		shoulders = Engine.startShoulders;
		shoulders.setArmor(0);
		shoulders.setBonusMana(0);
		shoulders.setManaRegen(0);

		chest = Engine.startChest;
		chest.setArmor(0);
		chest.setBonusHealth(0);
		chest.setHPregen(0);

		shield = Engine.startShield;
		shield.setArmor(3);
		GameMain.infoBox.append(Engine.deathMessage);
		stopMoving();
		level = 1;
		currentHealth = 100;
		baseHealth = 100;
		setHP(baseHealth);
		currentMana = 100;
		baseMana = 100;
		setMana(baseMana);
		baseDamage = 10;
		setArmor();
		currentExperience = 0;
		maxExperience = level*100;
		manaPotions = 0;
		healthPotions = 0;
		baseHpRegen = 1.0;
		baseManaRegen = 1.0;
		DrawGame.bartuc.aggro = false;
		DrawGame.bartuc.reset();
		resetStats();
		DrawGame.reset();

		currentRoom = Engine.centralZone;
		setX(448);
		setY(416);

		inventory.clear();
		
		GameMain.listModel.clear();
		printInventory();

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
			currentHealth += getHPRegen();
			if(currentHealth > maxHealth){
				currentHealth = maxHealth;
			}
			healthCounter = 0;
		}
		if(currentMana < maxMana && manaCounter >= 60){
			currentMana += getManaRegen();
			if(currentMana > maxMana){
				currentMana = maxMana;
			}
			manaCounter = 0;
		}
		if(currentRoom == Engine.caveZone && getXTile() >= 12 && getXTile() <= 19 && getYTile() <= 9 && DrawGame.bartuc.alive){
			if(!DrawGame.bartuc.aggro)
				DrawGame.bartuc.aggro = true;
			if(!Engine.bartucThemeSound)
				Engine.bartucThemeSound = true;
			if(Engine.caveThemeSound)
				Engine.caveThemeSound = false;
			if(!bartucEngage){
				GameMain.infoBox.append(Engine.bartucEngageMessage);
				GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
				bartucEngage = true;
			}

		}
		if(useHpPot){
			g.drawImage(getHpPotImage(), xPosition - 30, yPosition-27, null);
			hpSprite++;
		}
		if(useManaPot){
			g.drawImage(getManaPotImage(), xPosition - 30, yPosition-18, null);
			manaSprite++;
		}
		if(findLoot){
			g.drawImage(getLootImage(), xPosition, yPosition-30, null);
			lootSprite++;
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
	public void addDamageTakenBartuc(double damage){
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
				+ GameMain.decimals.format(getTotalKills())
				+ " of bartucs minions. "
				+ GameMain.decimals.format(getSwordKills())
				+ " by sword and "
				+ GameMain.decimals.format(getMagicKills())
				+ " by magic \n You have dealt "
				+ GameMain.decimals.format(getDamageDealt())
				+ " damage. "
				+ GameMain.decimals.format(getSwordDamageDealt())
				+ " by sword and "
				+ GameMain.decimals.format(getMagicDamageDealt())
				+ " by magic \n You have conjured "
				+ GameMain.decimals.format(getFireBalls())
				+ " fireballs. "
				+ GameMain.decimals.format(getMissedFireBalls())
				+ " of them missed their target \n You have slain the mighty Bartuc "
				+ GameMain.decimals.format(getBartucKills()) + " times \n You have taken "
				+ GameMain.decimals.format(getDamageTaken())
				+ " damage by bartucs minions and "
				+ GameMain.decimals.format(getDamageTakenBartuc())
				+ " by Bartuc himself \n You have used "
				+ GameMain.decimals.format(getHealthPotsUsed()) + " health potions and "
				+ GameMain.decimals.format(getManaPotsUsed())
				+ " mana potions \n\n Loot \n\n You have looted "
				+ GameMain.decimals.format(getGearFound())
				+ " pieces of armor \n You have looted "
				+ GameMain.decimals.format(getEpicGearFound())
				+ " pieces of legendary armor \n You have looted "
				+ GameMain.decimals.format(getSwordsFound())
				+ " swords \n You have looted "
				+ GameMain.decimals.format(getEpicSwordsFound())
				+ " legendary swords \n You have looted "
				+ GameMain.decimals.format(getShieldsFound())
				+ " shields \n You have looted "
				+ GameMain.decimals.format(getEpicShieldsFound())
				+ " legendary shields \n\n Exploration\n\n You have taken "
				+ GameMain.decimals.format(getStepsTaken())
				+ " steps \n You have walked between zones "
				+ GameMain.decimals.format(getZoneChanges()) + " times \n You have entered bartucs cave "
				+ GameMain.decimals.format(getTimesInCave()) + " times";

		return statsMessage;
	}

	public void load(Item helm, Item boots, Item gloves, Item pants, Item shoulders, Item chest, Item sword, Item shield, int savedlevel, 
			double currHp, double currMana, int healthPots, int manaPots, int currXP){
		this.helm = helm;
		this.boots = boots;
		this.gloves = gloves;
		this.pants = pants;
		this.shoulders = shoulders;
		this.chest = chest;
		this.sword = sword;
		this.shield = shield;
		this.level = savedlevel;
		this.healthPotions = healthPots;
		this.manaPotions = manaPots;
		this.baseHealth = 100.0;
		this.baseMana = 100.0;
		this.baseManaRegen = 1.0;
		this.baseHpRegen = 1.0;
		this.baseDamage = 10;
		this.level = 1;
		for(int i = 0 ; i < savedlevel-1; i++){
			this.level++;
			baseHealth += Engine.HP_LVL_UP;
			baseMana += Engine.MANA_LVL_UP;
			baseDamage += Engine.DAMAGE_LVL_UP;
			baseHpRegen += Engine.HPREGEN_LVL_UP;
			baseManaRegen += Engine.MANAREGEN_LVL_UP;
		}
		setHP(baseHealth);
		setMana(baseMana);
		maxExperience = this.level*100;
		this.currentExperience = currXP;
		this.currentHealth = currHp;
		this.currentMana = currMana;
		setArmor();
	}

	public Item chest(){
		return chest;
	}
	public Item gloves(){
		return gloves;
	}
	public Item pants(){
		return pants;
	}
	public Item helm(){
		return helm;
	}
	public Item boots(){
		return boots;
	}
	public Item shoulders(){
		return shoulders;
	}
	public Item shield(){
		return shield;
	}
	public Item sword(){
		return sword;
	}

}
