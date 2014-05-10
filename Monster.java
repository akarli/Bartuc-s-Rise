import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Monster {
	private BufferedImage monster;
	private int xPosition, yPosition, maxHealth, currentHealth, damage, moveCounter, attackCounter;
	String strDirection;
	Random rand = new Random();
	private boolean moveToPlayer = false, moveLeft = false, moveRight = false, moveUp = false, moveDown = false, moving = false;
	private boolean aUp = false, aDown = false, aLeft = false, aRight = false;
	private BufferedImage[] moveUpPic;
	private BufferedImage[] moveDownPic;
	private BufferedImage[] moveLeftPic;
	private BufferedImage[] moveRightPic;
	private BufferedImage[] attackUp;
	private BufferedImage[] attackDown;
	private BufferedImage[] attackLeft;
	private BufferedImage[] attackRight;
	private BufferedImage lastSprite;
	private int currentSprite = 0;
	private int x = 2;
	private int animationCounter=1;
	private int offset = 0, offsetx = 0;

	public Monster(){
		monster = loadMonsterImage("Graphics\\skeleton.png");
		maxHealth = rand.nextInt(40*DrawGame.character.getLevel()) + 20*DrawGame.character.getLevel();
		currentHealth = maxHealth;
		damage = rand.nextInt(DrawGame.character.getLevel()*20)  + DrawGame.character.getLevel()*10;
		moveCounter = 0;
		moveUpPic = new BufferedImage[4];
		moveDownPic = new BufferedImage[4];
		moveRightPic = new BufferedImage[4];
		moveLeftPic = new BufferedImage[4];
		attackUp = new BufferedImage[4];
		attackDown = new BufferedImage[4];
		attackLeft = new BufferedImage[4];
		attackRight = new BufferedImage[4];
		for(int i = 0; i < 4; i++){
			moveUpPic[i] = monster.getSubimage(x+(i*19), 133, 19, 50);
		}
		x=462;
		lastSprite = moveUpPic[1];
		for(int i = 0; i < 4; i++){
			moveDownPic[i] = monster.getSubimage(x+(i*20), 134, 20, 49);
			x=x+1;
		}
		x=181;
		for(int i = 0; i < 4; i++){
			if(i == 1){
				offset = 10;
				offsetx= 12;
			}
			else{
				offsetx = 0;
			}
			moveRightPic[i] = monster.getSubimage(x+(i*30 + offset), 133, 30, 48);
			x += 2;
		}
		x= 0;
		offset = 0;
		offsetx= 0;
		for(int i = 0; i < 4; i++){
			if(i == 1){
				offset = 8;
				offsetx= 4;
			}
			else{
				offsetx = 0;
			}
			moveLeftPic[i] = monster.getSubimage(x+(i*32 + offset), 81, 32 + offsetx, 48);
			x +=2;
		}
		//ladda in bilder
		attackRight[0] = monster.getSubimage(511, 66, 41, 63);
		attackRight[1] = monster.getSubimage(553, 66, 36, 63);
		attackRight[2] = monster.getSubimage(592, 66, 29, 63);
		attackRight[3] = monster.getSubimage(625, 66, 37, 63);
		attackUp[0] = monster.getSubimage(284, 73, 21, 56);
		attackUp[1] = monster.getSubimage(309, 73, 19, 56);
		attackUp[2] = monster.getSubimage(332, 73, 18, 56);
		attackUp[3] = monster.getSubimage(353, 73, 20, 56);
		attackLeft[0] = monster.getSubimage(347, 4, 42, 60);
		attackLeft[1] = monster.getSubimage(391, 4, 38, 60);
		attackLeft[2] = monster.getSubimage(432, 4, 30, 60);
		attackLeft[3] = monster.getSubimage(465, 4, 40, 60);
		attackDown[0] = monster.getSubimage(110, 3, 21, 60);
		attackDown[1] = monster.getSubimage(135, 3, 20, 60);
		attackDown[2] = monster.getSubimage(159, 3, 18, 60);
		attackDown[3] = monster.getSubimage(181, 3, 22, 60);
	}

	public BufferedImage loadMonsterImage(String fileName){
		BufferedImage img = null;

		try{
			img = ImageIO.read(new File(fileName));
		} catch (IOException e1) {
			throw new IllegalArgumentException("Could not load the file.");
		}

		return img;
	}

	public void place() {
		int y = 0;
		int x = 0;
		boolean placeable = false;
		while(!placeable){
			y = rand.nextInt(Engine.MAP_COLUMN);
			x = rand.nextInt(Engine.MAP_ROW);

			if(DrawGame.character.getCurrentRoom().getCollisionMap()[y][x] == 0 && (x != DrawGame.character.getXTile() && y != DrawGame.character.getYTile())){
				placeable = true;
			}
		}
		xPosition = x * Engine.TILE_WIDTH;
		yPosition = y * Engine.TILE_HEIGHT;
	}

	public BufferedImage getImage(){
		if(animationCounter == 8){
			currentSprite++;
			animationCounter = 0;
		}
		if(currentSprite >= 4){
			currentSprite = 0;
		}
		if(moveUp){
			lastSprite = moveUpPic[0];
			return moveUpPic[currentSprite];
		}
		if(moveDown){
			lastSprite = moveDownPic[0];
			return moveDownPic[currentSprite];
		}
		if(moveRight){
			lastSprite = moveRightPic[0];
			return moveRightPic[currentSprite];
		}
		if(moveLeft){
			lastSprite = moveLeftPic[0];
			return moveLeftPic[currentSprite];
		}
		if(attackCounter >= 31){
			stopAttacking();
		}
		if(aUp){
			attackCounter++;
			lastSprite = moveUpPic[0];
			return attackUp[currentSprite];
		}
		if(aDown){
			attackCounter++;
			lastSprite = moveDownPic[0];
			return attackDown[currentSprite];
		}
		if(aRight){
			attackCounter++;
			lastSprite = moveLeftPic[0];
			return attackLeft[currentSprite];
		}
		if(aLeft){
			attackCounter++;
			lastSprite = moveRightPic[0];
			return attackRight[currentSprite];
		}
		return lastSprite;
	}

	public void drawImage(Graphics g){
		if(moveUp || moveDown || moveRight || moveLeft || aUp || aDown){
			g.drawImage(getImage(), xPosition, yPosition, null);
			animationCounter++;
		}
		else if(aLeft){
			if(currentSprite <= 2)
				g.drawImage(getImage(), xPosition, yPosition - 15, null);
			else if(currentSprite == 3 && animationCounter < 8)
				g.drawImage(getImage(), xPosition, yPosition - 15, null);
			else
				g.drawImage(getImage(), xPosition, yPosition, null);
			animationCounter++;
		}
		else if(aRight){
			if(currentSprite <= 2)
				g.drawImage(getImage(), xPosition, yPosition - 10, null);
			else if(currentSprite == 3 && animationCounter < 8)
				g.drawImage(getImage(), xPosition, yPosition - 10, null);
			else
				g.drawImage(getImage(), xPosition, yPosition, null);
			animationCounter++;
		}
		else{
			g.drawImage(getImage(), xPosition, yPosition, null);
		}
	}

	public void attack(){
		animationCounter = 0;
		attackCounter = 0;
		if(currentXTile() == DrawGame.character.getXTile()){
			if(currentYTile() < DrawGame.character.getYTile()){
				aDown = true;
			}
			else if (currentYTile() > DrawGame.character.getYTile()){
				aUp = true;
			}
		}
		if(currentYTile() ==  DrawGame.character.getYTile()){
			if(currentXTile() > DrawGame.character.getXTile()){
				aRight = true;
			}
			else{
				aLeft = true;
			}
		}
		if(currentYTile() == DrawGame.character.getYTile() && currentXTile() == DrawGame.character.getYTile()){
			aRight = true;
		}
	}

	public void stopAttacking(){
		if(aLeft){
			if((DrawGame.character.getXTile() == currentXTile() || DrawGame.character.getXTile() - currentXTile() == 1) && DrawGame.character.getYTile() == currentYTile()){
				DrawGame.character.takeDamage(damage);
			}
		}
		if(aRight){
			if((DrawGame.character.getXTile() == currentXTile() || DrawGame.character.getXTile() - currentXTile() == -1) && (DrawGame.character.getYTile() == currentYTile())){
				DrawGame.character.takeDamage(damage);
			}
		}
		if(aUp){
			if((DrawGame.character.getXTile() == currentXTile()) && (DrawGame.character.getYTile() - currentYTile() == -1) || (currentYTile() == DrawGame.character.getYTile() && currentXTile() == DrawGame.character.getYTile())){
				DrawGame.character.takeDamage(damage);
			}
		}
		if(aDown){
			if((DrawGame.character.getXTile() == currentXTile()) && (DrawGame.character.getYTile() - currentYTile() == 1) || (DrawGame.character.getXTile() == currentXTile()) && DrawGame.character.getYTile() == currentYTile()){
				DrawGame.character.takeDamage(damage);
			}
		}
		aRight = false;
		aUp = false;
		aLeft = false;
		aDown = false;
	}

	public boolean takeDamage(int damage) {
		currentHealth -= damage;
		if (currentHealth <= 0) {
			GameMain.infoBox.append("\n You hit the skeleton for " + damage + " damage and slayed it.");
			GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
			DrawGame.character.getLoot();
			Engine.skeletonDeathSound = true;
			return true;
		}
		GameMain.infoBox.append("\n You hit the skeleton for " + damage + " damage. It has " + currentHealth + " health left.");
		GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument()
				.getLength());
		Engine.skeletonHitSound = true;
		return false;
	}
	
	public boolean moving(){
		return moving;
	}
	
	public boolean attacking(){
		return (aUp|| aDown || aLeft || aRight);
	}


	public void move(){
		if(moveDown){
			yPosition += 2;
		}
		if(moveUp){
			yPosition -= 2;
		}
		if(moveRight){
			xPosition += 2;
		}

		if(moveLeft){
			xPosition -= 2;
		}
		if(moving){
			moveCounter--;
		}
		if(moveCounter == 0){
			doneMoving();
		}
	}

	public int currentXTile(){
		return xPosition/32;
	}

	public int currentYTile(){
		return yPosition/32;
	}


	public void doneMoving(){
		moveToPlayer = false;
		moveLeft = false;
		moveRight = false;
		moveUp = false;
		moveDown = false;
		moving = false;
	}


	public void startMoving(int direction){
		if(Math.abs(currentXTile() - DrawGame.character.getXTile()) <= 1 && Math.abs(currentYTile() - DrawGame.character.getYTile()) <= 1){
			attack();
		}
		else{
			moving = true;
			moveCounter = 16;
			if(Math.abs(DrawGame.character.getYTile()-currentYTile()) <= 7 && Math.abs(DrawGame.character.getXTile()-currentXTile()) <= 7){
				moveToPlayer=true;
			}
			if(direction == 0){
				if(moveToPlayer){
					if(DrawGame.character.getYTile() > currentYTile() && currentYTile() != 19){
						if(DrawGame.character.getCurrentRoom().getCollisionMap()[currentYTile()+1][currentXTile()] != 1)
							moveDown = true;
					}
					if(DrawGame.character.getYTile() < currentYTile() && currentYTile() != 0){
						if(DrawGame.character.getCurrentRoom().getCollisionMap()[currentYTile()-1][currentXTile()] != 1)
							moveUp = true;
					}
				}
				else{
					int a = rand.nextInt(2);
					if(a == 1 && currentYTile() != 19 && DrawGame.character.getCurrentRoom().getCollisionMap()[currentYTile()+1][currentXTile()] != 1){
						moveDown = true;
					}
					if(currentYTile() != 0 && DrawGame.character.getCurrentRoom().getCollisionMap()[currentYTile()-1][currentXTile()] != 1){
						moveUp = true;
					}
				}
			}
			else{
				if(moveToPlayer){
					if(DrawGame.character.getXTile() >currentXTile() && currentXTile() != 31){
						if(DrawGame.character.getCurrentRoom().getCollisionMap()[currentYTile()][currentXTile()+1] != 1)
							moveRight = true;
					}
					if(DrawGame.character.getXTile() < currentXTile() && currentXTile() != 0){
						if(DrawGame.character.getCurrentRoom().getCollisionMap()[currentYTile()][currentXTile()-1] != 1)
							moveLeft = true;
					}
				}
				else{
					int a = rand.nextInt(2);
					if(a == 1 && currentXTile() != 31 && DrawGame.character.getCurrentRoom().getCollisionMap()[currentYTile()][currentXTile()+1] != 1){
						moveRight = true;
					}
					if(currentXTile() != 0 && DrawGame.character.getCurrentRoom().getCollisionMap()[currentYTile()][currentXTile()-1] != 1){
						moveLeft = true;
					}
				}
			}
		}
	}

	public int getMoveCounter(){
		return moveCounter;
	}

}
