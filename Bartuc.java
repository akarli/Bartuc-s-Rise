import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;


public class Bartuc {

	private double maxHealth, currentHealth, baseHealth;
	private int damage, baseDamage, attackCooldown, difficulty, xPosition, yPosition, shadowBlastCounter;
	private BufferedImage bartuc, currentSprite;
	private BufferedImage[] bartucIdle;
	private ShadowBlast shadowBlasts[];
	private Room zone;
	private boolean casting = false;
	public boolean aggro = false;
	public boolean alive = true;
	Random rand = new Random();

	public Bartuc(int kills){
		xPosition = 480;
		yPosition = 128;
		difficulty = kills+1;
		baseHealth = 100;
		baseDamage = 10;
		maxHealth = (100.0*baseHealth) * difficulty;
		currentHealth = maxHealth;
		damage = (10*baseDamage) * difficulty;
		attackCooldown = 10;
		shadowBlasts = new ShadowBlast[10];
		bartuc = loadBartucImage("Graphics\\bartuc.png");
		bartucIdle = new BufferedImage[3];
		bartucIdle[0] = bartuc.getSubimage(0, 0, 64, 50);
		bartucIdle[1] = bartuc.getSubimage(67, 0, 64, 50);
		bartucIdle[2] = bartuc.getSubimage(134, 0, 65, 50);
		currentSprite = bartucIdle[1];
		zone = Engine.caveZone;
		for(int i = 0; i<10; i++){
			shadowBlasts[i] = new ShadowBlast();
		}

	}

	public BufferedImage loadBartucImage(String fileName){
		BufferedImage img = null;
		try{
			img = ImageIO.read(new File(fileName));
		} catch (IOException e1) {
			throw new IllegalArgumentException("Could not load the file.");
		}

		return img;
	}


	public void drawImage(Graphics g){
		g.drawImage(bartucIdle[1], xPosition, yPosition, null);
		if(casting){
			for(int i = 0; i< shadowBlasts.length; i++){
				shadowBlasts[i].drawImage(g);
			}
		}
	}


	public double getCurrentHealth(){
		return currentHealth;
	}

	public double getMaxHealth(){
		return maxHealth;
	}

	public int getDamage(){
		return damage;
	}

	public int getCD(){
		return attackCooldown;
	}
	
	public int getXTile(){
		  return xPosition/32;
		 }
		 
		 public int getYTile(){
		  return yPosition/32;
	}
		 
	public void takeDamage(int damage){
		currentHealth -= damage;
	}
	
	public void reset(){
		
	}

	public void castShadowBlast(){
		int xOffset = rand.nextInt(5)-2;
		int yOffset = rand.nextInt(5)-2;
		shadowBlasts[shadowBlastCounter].cast(DrawGame.character.getXTile()+ xOffset, DrawGame.character.getYTile()+yOffset, damage, Engine.caveZone);
		shadowBlasts[shadowBlastCounter].sound = true;
		casting = true;
		shadowBlastCounter++;
		if(shadowBlastCounter > 9){
			shadowBlastCounter = 0;
		}
	}
}
