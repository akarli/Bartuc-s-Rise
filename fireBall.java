import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class FireBall {

	private int xPosition;
	private int yPosition;
	private int spriteFire;
	private boolean castingFireBall = false;
	private Room atRoom;
	private BufferedImage fireball;
	private BufferedImage[] fireBall;
	private boolean addMiss, hit;

	public FireBall(int x, int y, Room room){
		fireball = loadImage("Graphics\\fireball.png");
		fireBall = new BufferedImage[42];
		for(int i = 0; i < 42 ;i++){
			fireBall[i] = fireball.getSubimage(0 + (i*128), 0, 128, 128);
		}
		xPosition = x -46;
		yPosition = y -50;
		atRoom = room;
		addMiss = false;
		hit = false;
	}

	public BufferedImage loadImage(String fileName){
		BufferedImage img = null;

		try{
			img = ImageIO.read(new File(fileName));
		} catch (IOException e1) {
			throw new IllegalArgumentException("Could not load the file.");
		}

		return img;
	}
	public int getXtile(){
		return xPosition/32;
	}

	public int getYtile(){
		return yPosition/32;
	}

	public void Explode() {
		if(!Engine.fireballSound){
			Engine.fireballSound = true;
		}
		DrawGame.character.addFireBalls();
		for (int j = 0; j < DrawGame.monsterList[DrawGame.monsterHash
				.get(atRoom)].size(); j++) {
			Monster a = (Monster) DrawGame.monsterList[DrawGame.monsterHash
					.get(atRoom)].get(j);
			if (Math.abs(a.currentXTile() - (getXtile() + 2)) < 2
					&& Math.abs(a.currentYTile() - (getYtile() + 2)) < 2) {
				DrawGame.character.addMagicDamageDealt(2 * DrawGame.character
						.getDamage()); // Adds total magic damage dealt.
				hit = true;
				if (a.takeDamage(2 * DrawGame.character.getDamage())) {
					DrawGame.monsterList[DrawGame.monsterHash.get(atRoom)]
							.remove(j);
					DrawGame.character.increaseXP(25);
					DrawGame.character.addMagicKills(); // Adds total kills by magic
				}
			}
		}
		if (!hit) {
			if (!addMiss){
				DrawGame.character.addMissedFireBalls(); // Adds total fireballs missed
				addMiss = true;
			}
		}
		hit = false;
		addMiss = false;
	}

	public void cast(){
		castingFireBall = true;
	}

	public BufferedImage castFireBallImage(){
		if(spriteFire == 84){
			spriteFire = 0;
			castingFireBall = false;
			DrawGame.character.stopCasting();

		}
		
		if(spriteFire == 70){
			Explode();
		}
		return fireBall[spriteFire/2];
	}

	public void drawImage(Graphics g){
		 if(castingFireBall && atRoom == DrawGame.character.getCurrentRoom()){
			   spriteFire++;
			   g.drawImage(castFireBallImage(), xPosition, yPosition, null);
			  }
			  if(castingFireBall && atRoom != DrawGame.character.getCurrentRoom()){
			   spriteFire++;
			   castFireBallImage();
			  }
	}

}
