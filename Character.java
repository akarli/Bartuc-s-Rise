import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Character {
	private BufferedImage character;
	private int xPosition, yPosition, maxHealth, currentHealth, damage;
	Room centralZone, northZone, southZone, eastZone, westZone, caveZone, currentRoom;


	public Character(){
		centralZone = new Room(Engine.centralZoneGround, Engine.centralZoneTop, Engine.centralZoneCollision);
		northZone = new Room(Engine.northZoneGround, Engine.northZoneTop, Engine.northZoneCollision);
		southZone = new Room(Engine.southZoneGround, Engine.southZoneTop, Engine.southZoneCollision);
		eastZone = new Room(Engine.eastZoneGround, Engine.eastZoneTop, Engine.eastZoneCollision);
		westZone = new Room(Engine.westZoneGround, Engine.westZoneTop, Engine.westZoneCollision);
		caveZone = new Room(Engine.caveZoneGround, Engine.caveZoneTop, Engine.caveZoneCollision);
		centralZone.setExit("north", northZone);
		centralZone.setExit("west", westZone);
		centralZone.setExit("south", southZone);
		centralZone.setExit("east", eastZone);
		northZone.setExit("south", centralZone);
		westZone.setExit("east", centralZone);
		eastZone.setExit("west", centralZone);
		southZone.setExit("north", centralZone);
		character = loadCharacterImage("Character.png");
		xPosition = 448;
		yPosition = 416;
		maxHealth = 100;
		currentHealth = 100;
		damage = 25;
		currentRoom = centralZone;
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

	public void moveUp(){
		if(yPosition == 0){
			currentRoom = currentRoom.getExit("north");
			yPosition = 608;
		}
		else if(currentRoom.getCollisionMap()[((yPosition-31)/32)][((xPosition+1)/32)] != 1){
			yPosition -= 32;
		}
	}

	public void moveDown(){
		if(yPosition == 608){
			currentRoom = currentRoom.getExit("south");
			yPosition = 0;
		}
		else if(currentRoom.getCollisionMap()[((yPosition+33)/32)][((xPosition+1)/32)] != 1){
			yPosition +=32;

		}
	}

	public void moveLeft(){
		if(xPosition == 0){
			currentRoom = currentRoom.getExit("west");
			xPosition = 992;
		}
		else if(currentRoom.getCollisionMap()[((yPosition+1)/32)][((xPosition-31)/32)] != 1){
			xPosition-= 32;
		}
	}

	public void moveRight(){
		if(xPosition == 992){
			currentRoom = currentRoom.getExit("east");
			xPosition = 0;
		}
		else if(currentRoom.getCollisionMap()[((yPosition+1)/32)][((xPosition+33)/32)] != 1){
			xPosition += 32;
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
