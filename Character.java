import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Character {
	private BufferedImage character, levelup, fireball;
	private int xPosition, yPosition, maxHealth, currentHealth, damage, level, armor, currentExperience, maxExperience, currentMana, maxMana;
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
			currentRoom = currentRoom.getExit("cave");
			levelUp();
			xPosition = 15*Engine.TILE_WIDTH;
			yPosition = 19*Engine.TILE_HEIGHT-2;
			DrawGame.newZone = true;
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
				xPosition = 26*Engine.TILE_WIDTH;
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

	public BufferedImage getImage(){
		if(animationCounter == 8){
			currentSprite++;
			animationCounter = 1;
		}
		if(currentSprite >= 4){
			currentSprite = 0;
			aLeft = false;
			aRight = false;
			aUp = false;
			aDown = false;
			if(DrawGame.attacking){
				for(int j = 0; j < DrawGame.monsterList[DrawGame.monsterHash.get(currentRoom)].size();j++){ 
					Monster a = (Monster) DrawGame.monsterList[DrawGame.monsterHash.get(currentRoom)].get(j);
					if(Math.abs(a.currentXTile()- getXTile()) < 2 && Math.abs(a.currentYTile() - getYTile()) < 2){
						if(a.takeDamage(DrawGame.character.getDamage())){
							DrawGame.monsterList[DrawGame.monsterHash.get(currentRoom)].remove(j);
							DrawGame.character.increaseXP(25);
						}
					}
				}
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


	public void drawImage(Graphics g){
		g.drawImage(getImage(), xPosition, yPosition, null);
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
		currentMana++;
	}

}
