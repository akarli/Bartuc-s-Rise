import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ShadowBlast {

	private int xPosition;
	private int yPosition;
	private int spriteFire;
	private int damage;
	private boolean castingShadowBlast = false;
	private Room atRoom;
	private BufferedImage shadowblast;
	private BufferedImage[] shadowBlast;
	

	public ShadowBlast(){
		shadowblast = loadImage("Graphics\\shadowblast.png");
		shadowBlast = new BufferedImage[48];
		for(int i = 0; i < 48 ;i++){
			shadowBlast[i] = shadowblast.getSubimage(0 + (i*128), 0, 128, 128);
		}
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
	public void cast(int x, int y, int damage, Room room){
		castingShadowBlast = true;
		xPosition = x*32 -46;
		yPosition = y*32 -50;
		atRoom = room;
		this.damage = damage;
	}

	public void Explode() {
		if(!Engine.fireballSound){
			Engine.fireballSound = true;
		}
		DrawGame.character.addFireBalls();
		if (Math.abs(DrawGame.character.getXTile() - (getXtile() + 2)) < 2 && Math.abs(DrawGame.character.getYTile() - (getYtile() + 2)) < 2){
			DrawGame.character.takeDamageBartuc(damage);
		}
	}

	public BufferedImage castShadowBlastImage(){
		if(spriteFire == 96){
			spriteFire = 0;
			castingShadowBlast = false;
			DrawGame.character.stopCasting();

		}

		if(spriteFire == 82){
			Explode();
		}
		return shadowBlast[spriteFire/2];
	}

	public void drawImage(Graphics g){
		if(castingShadowBlast && atRoom == DrawGame.character.getCurrentRoom()){
			spriteFire++;
			g.drawImage(castShadowBlastImage(), xPosition, yPosition, null);
		}
		if(castingShadowBlast && atRoom != DrawGame.character.getCurrentRoom()){
			spriteFire++;
			castShadowBlastImage();
		}
	}

}
