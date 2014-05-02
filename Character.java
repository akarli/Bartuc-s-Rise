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
		if(currentRoom == Engine.centralZone && (xPosition/32 == 26 || xPosition/32 == 27) && yPosition/32 < 7){
			currentRoom = currentRoom.getExit("cave");
			xPosition = 15*Engine.TILE_WIDTH-2; //kompensera för förskjutning
			yPosition = 19*Engine.TILE_HEIGHT-2;
		}
		if (yPosition <= 0) {
			currentRoom = currentRoom.getExit("north");
			yPosition = 608;
		} else if (currentRoom.getCollisionMap()[((yPosition - 3) / 32)][((xPosition + 16) / 32)] != 1) {
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
		} else if (currentRoom.getCollisionMap()[((yPosition + 34) / 32)][((xPosition + 16) / 32)] != 1) {
			yPosition += 2;
		}

	}

	public void moveLeft() {
		if (xPosition <= 0) {
			currentRoom = currentRoom.getExit("west");
			xPosition = 992;
		} else if (currentRoom.getCollisionMap()[((yPosition + 16) / 32)][((xPosition - 3) / 32)] != 1) {
			xPosition -= 2;
		}
	}

	public void moveRight() {
		if (xPosition >= 992) {
			currentRoom = currentRoom.getExit("east");
			xPosition = 0;
		} else if (currentRoom.getCollisionMap()[((yPosition + 16) / 32)][((xPosition + 32) / 32)] != 1) {
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
