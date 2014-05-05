import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Monster {
	private BufferedImage monster;
	private int xPosition, yPosition, maxHealth, currentHealth, damage, moveCounter;
	String strDirection;
	Random rand = new Random();
	private boolean moveToPlayer = false, moveLeft = false, moveRight = false, moveUp = false, moveDown = false;

	public Monster(){
		monster = loadMonsterImage("Character.png");
		maxHealth = rand.nextInt((20*DrawGame.character.getLevel()) + (5*DrawGame.character.getLevel()));
		currentHealth = maxHealth;
		damage = rand.nextInt(DrawGame.character.getLevel()*5 + DrawGame.character.getLevel()*2);
		moveCounter = 16;
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

	public void drawImage(Graphics g){
		g.drawImage(monster, xPosition, yPosition, null);
	}

	public void takeDamage(int damage){
		currentHealth -= damage;
	}

	public void move(){

		if(strDirection.matches("y")){
			if(currentYTile() != 19)
				if(moveDown && DrawGame.character.getCurrentRoom().getCollisionMap()[((yPosition + 32) / 32)][((xPosition) / 32)] != 1){
					yPosition += 2;
				}
			if(currentYTile() != 0){
				if(moveUp && DrawGame.character.getCurrentRoom().getCollisionMap()[((yPosition - 2) / 32)][((xPosition) / 32)] != 1){
					yPosition -= 2;
				}
			}
		}
		if(strDirection.matches("x")){
			if(currentXTile() != 31){
				if(moveRight && DrawGame.character.getCurrentRoom().getCollisionMap()[((yPosition) / 32)][((xPosition + 32) / 32)] != 1){
					xPosition += 2;
				}
			}
			if(currentXTile() != 0){
				if(moveLeft && DrawGame.character.getCurrentRoom().getCollisionMap()[((yPosition)/32)][((xPosition-2) / 32)] != 1){
					xPosition -= 2;
				}
			}
		}

		moveCounter++;
		if(moveCounter == 16){
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
	}


	public void startMoving(int direction){
		moveCounter = 0;
		if(Math.abs(DrawGame.character.getYTile()-currentYTile()) <= 7 && Math.abs(DrawGame.character.getXTile()-currentXTile()) <= 7){
			moveToPlayer=true;
		}
		if(direction == 0){
			strDirection = "y";
			if(moveToPlayer){
				if(DrawGame.character.getYTile() > currentYTile()){
					moveDown = true;
				}
				if(DrawGame.character.getYTile() < currentYTile()){
					moveUp = true;
				}
				if(DrawGame.character.getYTile() == currentYTile()){
					strDirection = "x";
				}
			}
			else{
				int a = rand.nextInt(2);
				if(a == 1){
					moveDown = true;
				}
				else{
					moveUp = true;
				}
			}
		}
		else{
			strDirection = "x";
			if(moveToPlayer){
				if(DrawGame.character.getXTile() >currentXTile()){
					moveRight = true;
				}
				if(DrawGame.character.getXTile() < currentXTile()){
					moveLeft = true;
				}
				if(DrawGame.character.getXTile() == currentXTile()){
					strDirection = "y";
				}
			}
			else{
				int a = rand.nextInt(2);
				if(a == 1){
					moveRight = true;
				}
				else{
					moveLeft = true;
				}
			}
		}
	}

	public int getMoveCounter(){
		return moveCounter;
	}

}
