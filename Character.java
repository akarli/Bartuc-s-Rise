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
		centralZone = new Room(Engine.centralZoneGround, Engine.centralZoneTop);
		northZone = new Room(Engine.northZoneGround, Engine.northZoneTop);
		southZone = new Room(Engine.southZoneGround, Engine.southZoneTop);
		eastZone = new Room(Engine.eastZoneGround, Engine.eastZoneTop);
		westZone = new Room(Engine.westZoneGround, Engine.westZoneTop);
		caveZone = new Room(Engine.caveZoneGround, Engine.caveZoneTop);
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
		yPosition -= 32;
		if(yPosition < 0){
			currentRoom = currentRoom.getExit("north");
			yPosition = 618;
		}
	}

	public void moveDown(){
		yPosition +=32;
		if(yPosition > 640){
			currentRoom = currentRoom.getExit("south");
			yPosition = 0;
		}
	}
	
	public void moveLeft(){
		xPosition-= 32;
		if(xPosition < 0){
			currentRoom = currentRoom.getExit("west");
			xPosition = 992;
		}
	}
	
	public void moveRight(){
		xPosition += 32;
		if(xPosition > 992){
			currentRoom = currentRoom.getExit("east");
			xPosition = 0;
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
