import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Character {
	private BufferedImage character;
	private int xPosition, yPosition, maxHealth, currentHealth, damage;


	public Character(){
		character = loadCharacterImage("Character.png");
		xPosition = 448;
		yPosition = 416;
		maxHealth = 100;
		currentHealth = 100;
		damage = 25;
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

	public void moveUp(){
		yPosition -= 32;
	}

	public void moveDown(){
		yPosition +=32;
	}

	public void moveLeft(){
		xPosition-= 32;
	}

	public void moveRight(){
		xPosition += 32;
	}

	public void setX(int x){
		xPosition = x;
	}

	public void setY(int y){
		yPosition = y;
	}

	public void drawImage(Graphics g){
		g.drawImage(character, xPosition, yPosition, null);
	}

}
