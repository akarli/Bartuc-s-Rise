import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Monster {
	private BufferedImage monster;
	private int xPosition, yPosition, maxHealth, currentHealth, damage;
	Random rand = new Random();
	
	public Monster(){
		monster = loadMonsterImage("Character.png");
		maxHealth = rand.nextInt((20*DrawGame.character.getLevel()) + (5*DrawGame.character.getLevel()));
		currentHealth = maxHealth;
		damage = rand.nextInt(DrawGame.character.getLevel()*5 + DrawGame.character.getLevel()*2);
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
			
			if(DrawGame.character.getCurrentRoom().getCollisionMap()[y][x] == 0){
				placeable = true;
			}
		}
		xPosition = x * Engine.TILE_WIDTH;
		yPosition = y * Engine.TILE_HEIGHT;
	}
	
	public void drawImage(Graphics g){
		g.drawImage(monster, xPosition, yPosition, null);
	}

}
