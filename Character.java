import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;


public class Character {
	private BufferedImage character, levelup, fireball;
	private int xPosition, yPosition, maxHealth, currentHealth, damage, level, armor, currentExperience, maxExperience, currentMana, maxMana, manaCounter, healthCounter ;
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
	private int fireBallX = 0, fireBallY=0, offsetx = 0;
	private int currentSprite = 0;
	private int animationCounter=1;
	private int x = 0;
	private int sprite =0, spriteFire = 0;
	private boolean levelingUp, castingFireBall, aLeft, aRight, aUp, aDown;
	private FireBall eldBoll;

	public Character(){
		character = loadCharacterImage("charWalk.png");
		levelup = loadCharacterImage("LEVELUP.png");
		fireball = loadCharacterImage("FIREBALL.png");
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
		maxHealth = 100;
		currentHealth = 100;
		damage = 25;
		armor = 0;
		level = 1;
		currentExperience = 0;
		maxExperience = level*100;
		currentRoom = Engine.centralZone;
		currentMana = 100;
		maxMana = 100;
		manaCounter = 60;
		healthCounter = 60;
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
	public void moveUp() {
		if(currentRoom == Engine.centralZone && (xPosition/32 == 26 || xPosition/32 == 27) && yPosition/32 < 7 && yPosition/32 > 5){
			if(getXTile() == 26){
				currentRoom = currentRoom.getExit("cave");
				xPosition = 15*Engine.TILE_WIDTH;
				yPosition = 19*Engine.TILE_HEIGHT-2;
				DrawGame.newZone = true;
			}
			else{
				currentRoom = currentRoom.getExit("cave");
				xPosition = 16*Engine.TILE_WIDTH;
				yPosition = 19*Engine.TILE_HEIGHT-2;
				DrawGame.newZone = true;
			}
		}
		if (yPosition <= 0) {
			currentRoom = currentRoom.getExit("north");
			yPosition = 608;
			DrawGame.newZone = true;
		}
		if (currentRoom.getCollisionMap()[((yPosition - 2) / 32)][((xPosition) / 32)] != 1) {
			yPosition -= 2;
		}
	}

	public void moveDown() {
		if (yPosition >= 608) {
			if(currentRoom != Engine.caveZone){
				yPosition = 0;
			}
			else{
				if(getXTile() == 15){
					xPosition = 26*Engine.TILE_WIDTH;
				}
				else{
					xPosition = 27*Engine.TILE_WIDTH;
				}
				yPosition = 7*Engine.TILE_HEIGHT;
			}
			currentRoom = currentRoom.getExit("south");
			DrawGame.newZone = true;
		}

		if (currentRoom.getCollisionMap()[((yPosition + 32) / 32)][((xPosition) / 32)] != 1 ) {
			yPosition += 2;
		}
	}

	public void moveLeft() {
		if (xPosition <= 0) {
			currentRoom = currentRoom.getExit("west");
			DrawGame.newZone = true;
			xPosition = 992;
		}
		if (currentRoom.getCollisionMap()[((yPosition)/32)][((xPosition-2) / 32)] != 1) {
			xPosition -= 2;
		}

	}

	public void moveRight() {
		if (xPosition >= 992) {
			currentRoom = currentRoom.getExit("east");
			DrawGame.newZone = true;
			xPosition = 0;
		}
		if (currentRoom.getCollisionMap()[((yPosition) / 32)][((xPosition + 32) / 32)] != 1) {
			xPosition += 2;
		}
	}

	public void attack(){
		animationCounter = 0;
		currentSprite = 0;
		if(lastSprite == moveRight[1]){
			aRight = true;
		}
		if(lastSprite == moveLeft[1]){
			aLeft=true;
		}
		if(lastSprite == moveUp[1]){
			aUp=true;
		}
		if(lastSprite == moveDown[1]){
			aDown=true;
		}
	}

	public void stopAttack(){
		for(int j = 0; j < DrawGame.monsterList[DrawGame.monsterHash.get(currentRoom)].size();j++){ 
			Monster a = (Monster) DrawGame.monsterList[DrawGame.monsterHash.get(currentRoom)].get(j);
			if(aLeft){
				if((a.currentXTile() == getXTile() || a.currentXTile() - getXTile() == -1) && a.currentYTile() == getYTile()){
					if(a.takeDamage(DrawGame.character.getDamage())){
						DrawGame.monsterList[DrawGame.monsterHash.get(currentRoom)].remove(j);
						DrawGame.character.increaseXP(25);
					}
				}
			}
			else if(aRight){
				if((a.currentXTile() == getXTile() || a.currentXTile() - getXTile() == 1) && (a.currentYTile() == getYTile())){
					if(a.takeDamage(DrawGame.character.getDamage())){
						DrawGame.monsterList[DrawGame.monsterHash.get(currentRoom)].remove(j);
						DrawGame.character.increaseXP(25);
					}
				}
			}
			else if(aUp){
				if((a.currentXTile() == getXTile()) && (a.currentYTile() == getYTile() || a.currentYTile() - getYTile() == -1)){
					if(a.takeDamage(DrawGame.character.getDamage())){
						DrawGame.monsterList[DrawGame.monsterHash.get(currentRoom)].remove(j);
						DrawGame.character.increaseXP(25);
					}
				}
			}
			else if(aDown){
				if((a.currentXTile() == getXTile()) && (a.currentYTile() == getYTile() || a.currentYTile() - getYTile() == 1)){
					if(a.takeDamage(DrawGame.character.getDamage())){
						DrawGame.monsterList[DrawGame.monsterHash.get(currentRoom)].remove(j);
						DrawGame.character.increaseXP(25);
					}
				}
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

	public int getCurrHP(){
		return currentHealth;
	}

	public int getMaxHP(){
		return maxHealth;
	}

	public void setHP(int HP){
		maxHealth = HP;
	}

	public int getDamage(){
		return damage;
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

	public int getMaxXP(){
		return maxExperience;
	}

	public void setXP(int xp){
		currentExperience = xp;
	}

	public int getMana(){
		return currentMana;
	}

	public int getMaxMana(){
		return maxMana;
	}

	public void setMana(int mana){
		maxMana = mana;
	}

	public Room getCurrentRoom(){
		return currentRoom;
	}

	public void setCurrHP(int hp){
		currentHealth = hp;
	}

	public void setCurrMana(int mana){
		currentMana = mana;
	}

	public void setMaxXP(){
		maxExperience = level*100;
	}

	public void takeDamage(int damage){
		damage -= armor;
		if(damage < 0){
			damage = 0;
		}
		currentHealth -= damage;
	}

	public void getLoot(){
		Random rand = new Random();
		Boolean epicGear = false;
		int drop = rand.nextInt(100);
		if(drop >= 84){
			int quality = rand.nextInt(100);
			if(quality >= 84){
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
			if(lootType == 3){
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
					GameMain.infoBox.append("\n You found " + Engine.gearTypeEpic[gearType] + " worn by angles! \n Armor increased by " + armorUpgrade + ".\n Health increased by " + healthUpgrade + ".\n Mana increased by " + manaUpgrade + ".");
				}
				else{
					int stat = rand.nextInt(2);
					int armorUpgrade = rand.nextInt(level) + 1;
					armor += armorUpgrade;
					if(stat == 0){
						int healthUpgrade = rand.nextInt(3*level)+level;
						maxHealth += healthUpgrade;
						GameMain.infoBox.append("\n You found " + Engine.gearTypeHealth[gearType] + " from a fallen enemy! \n Armor increased by " + armorUpgrade + ". \n Health increased by " + healthUpgrade + ".");
					}
					else{
						int manaUpgrade = rand.nextInt(level) + level;
						maxMana += manaUpgrade;
						GameMain.infoBox.append("\n You found " + Engine.gearTypeMana[gearType] + " from a fallen enemy! \n Armor increased by " + armorUpgrade + ". \n Mana increased by " + manaUpgrade + ".");
					}
				}
			}
			GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
		}
	}
	public BufferedImage getImage(){
		if(animationCounter == 8){
			currentSprite++;
			animationCounter = 1;
		}
		if(currentSprite >= 4){
			currentSprite = 0;
			if(DrawGame.attacking){
				stopAttack();
			}
			DrawGame.attacking = false;
		}
		if(DrawGame.up){
			lastSprite = moveUp[1];
			return moveUp[currentSprite];
		}
		if(DrawGame.down){
			lastSprite = moveDown[1];
			return moveDown[currentSprite];
		}
		if(DrawGame.right){
			lastSprite = moveRight[1];
			return moveRight[currentSprite];
		}
		if(DrawGame.left){
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

		if(DrawGame.up || DrawGame.down || DrawGame.left || DrawGame.right){
			animationCounter++;
		}
		if(currentHealth < maxHealth && healthCounter >= 60){
			currentHealth++;
			healthCounter = 0;
		}
		if(currentMana < maxMana && manaCounter >= 60){
			currentMana++;
			manaCounter = 0;
		}
		manaCounter++;
		healthCounter++;
	}

}
