import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Character {
	private BufferedImage character;
	private int xPosition, yPosition, maxHealth, currentHealth, damage, level, armor, currentExperience, maxExperience, currentMana, maxMana;
	private Room currentRoom;
	private BufferedImage[] moveUp;
	private BufferedImage[] moveDown;
	private BufferedImage[] moveLeft;
	private BufferedImage[] moveRight;
	private BufferedImage lastSprite;
	private Image fireBall;
	private int currentSprite = 0;
	private int animationCounter=1;
	private int x = 0;

	public Character(){
		character = loadCharacterImage("charWalk.png");
		fireBall = Toolkit.getDefaultToolkit().getImage("fireball.gif");
		moveUp = new BufferedImage[4];
		moveDown = new BufferedImage[4];
		moveRight = new BufferedImage[4];
		moveLeft = new BufferedImage[4];
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

		if (currentRoom.getCollisionMap()[((yPosition + 32) / 32)][((xPosition) / 32)] != 1) {
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
	
	public int getCurrHP(){
		return currentHealth;
	}
	
	public int getMaxHP(){
		return maxHealth;
	}
	
	public int getDamage(){
		return damage;
	}
	
	public int getArmor(){
		return armor;
	}
	
	public int getXP(){
		return currentExperience;
	}
	
	public int getMaxXP(){
		return maxExperience;
	}
	
	public int getMana(){
		return currentMana;
	}

	public int getMaxMana(){
		return maxMana;
	}

	public Room getCurrentRoom(){
		return currentRoom;
	}

	public BufferedImage getImage(){
		if(animationCounter == 8){
			currentSprite++;
			animationCounter = 1;
		}
		if(currentSprite >= 4){
			currentSprite = 0;
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
		return lastSprite;
	}

	public void drawImage(Graphics g){
		g.drawImage(getImage(), xPosition, yPosition, null);
		if(DrawGame.up || DrawGame.down || DrawGame.left || DrawGame.right){
			animationCounter++;
		}
	}

}
