import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Character {
	private BufferedImage character;
	private int xPosition, yPosition, maxHealth, currentHealth, damage;
	private Room currentRoom;

	public Character(){
		character = loadCharacterImage("Character.png");
		xPosition = 448;
		yPosition = 416;
		maxHealth = 100;
		currentHealth = 100;
		damage = 25;
		currentRoom = Engine.centralZone;
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

	public int getX(){
		return xPosition;
	}

	public int getY(){
		return yPosition;
	}
	public void moveUp() {
		if(currentRoom == Engine.centralZone && (xPosition/32 == 26 || xPosition/32 == 27) && yPosition/32 < 7 && yPosition/32 > 5){
			currentRoom = currentRoom.getExit("cave");
			xPosition = 15*Engine.TILE_WIDTH;
			yPosition = 19*Engine.TILE_HEIGHT;
		}
		if (yPosition <= 0) {
			currentRoom = currentRoom.getExit("north");
			yPosition = 608;
		}
		if (currentRoom.getCollisionMap()[((yPosition - 2) / 32)][((xPosition) / 32)] != 1) {
			yPosition -= 2;
		}
	}

	public void moveDown() {
		if (yPosition >= 606) {
			if(currentRoom != Engine.caveZone){
				yPosition = 0;
			}
			else{
				xPosition = 26*Engine.TILE_WIDTH;
				yPosition = 7*Engine.TILE_HEIGHT;
			}
			currentRoom = currentRoom.getExit("south");
		} 
		
		if (currentRoom.getCollisionMap()[((yPosition + 32) / 32)][((xPosition) / 32)] != 1) {
			yPosition += 2;
		}
	}

	public void moveLeft() {
		if (xPosition <= 0) {
			currentRoom = currentRoom.getExit("west");
			xPosition = 992;
		}
		if (currentRoom.getCollisionMap()[((yPosition)/32)][((xPosition-2) / 32)] != 1) {
			xPosition -= 2;
		}

	}

	public void moveRight() {
		if (xPosition >= 992) {
			currentRoom = currentRoom.getExit("east");
			xPosition = 0;
		}
		if (currentRoom.getCollisionMap()[((yPosition) / 32)][((xPosition + 32) / 32)] != 1) {
			xPosition += 2;
		}
	}
	public void setX(int x){
		xPosition = x;
	}

	public void setY(int y){
		yPosition = y;
	}

	public Room getCurrentRoom(){
		return currentRoom;
	}


	public void drawImage(Graphics g){
		g.drawImage(character, xPosition, yPosition, null);
	}

}
