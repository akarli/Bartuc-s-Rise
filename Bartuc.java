import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;


public class Bartuc {

	private int maxHealth, currentHealth, baseHealth;
	private int damage, baseDamage, attackCooldown, difficulty, xPosition, yPosition, shadowBlastCounter, sprite, deathCounter, firesprite;
	private BufferedImage bartuc, currentSprite , superfireblast;
	private BufferedImage[] bartucIdle;
	private BufferedImage[] superfireblasts;
	private ShadowBlast shadowBlasts[];
	private Room zone;
	private boolean casting = false;
	public boolean aggro = false;
	public boolean alive = true;
	public boolean dying;
	Random rand = new Random();
	private MediaPlayer hitPlayer;
	private MediaPlayer deathPlayer;
	private MediaPlayer deathThemePlayer;
	private boolean hitSound = false;

	public Bartuc(int kills){
		xPosition = 480;
		yPosition = 128;
		difficulty = kills+1;
		baseHealth = 100;
		baseDamage = 10;
		maxHealth = (100*baseHealth) * difficulty;
		currentHealth = maxHealth;
		damage = (10*baseDamage) * difficulty;
		attackCooldown = 10;
		shadowBlasts = new ShadowBlast[10];
		bartuc = loadBartucImage("Graphics\\bartuc.png");
		superfireblast = loadBartucImage("Graphics\\superfireball.png");
		bartucIdle = new BufferedImage[3];
		superfireblasts = new BufferedImage[42];
		for(int i = 0; i<superfireblasts.length; i++){
			superfireblasts[i]= superfireblast.getSubimage(i*256, 0, 256, 256);
		}
		bartucIdle[0] = bartuc.getSubimage(0, 0, 64, 50);
		bartucIdle[1] = bartuc.getSubimage(67, 0, 64, 50);
		bartucIdle[2] = bartuc.getSubimage(134, 0, 65, 50);
		currentSprite = bartucIdle[1];
		zone = Engine.caveZone;
		hitPlayer = new MediaPlayer(Engine.bartucHit1);
		hitPlayer.setVolume(0.2f);
		deathPlayer = new MediaPlayer(Engine.bartucDeath);
		deathPlayer.setVolume(0.5f);
		deathThemePlayer = new MediaPlayer(Engine.bartucDeathTheme);
		deathThemePlayer.setVolume(0.2f);
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
		if(dying){
			g.drawImage(superfireblasts[firesprite/3], xPosition-100, yPosition-100, null);
			if(deathCounter < 100){
				g.drawImage(bartucIdle[sprite/5], rand.nextInt(10) + 476, rand.nextInt(10) + 123, null);
			}
			deathCounter++;
			sprite++;
			firesprite++;
			if(sprite>= 15){
				sprite = 0;
			}
			if(firesprite >= 126){
				firesprite = 0;
			}
		}
		else{
			g.drawImage(bartucIdle[1], xPosition, yPosition, null);
		}
		if(casting){
			for(int i = 0; i< shadowBlasts.length; i++){
				shadowBlasts[i].drawImage(g);
			}
		}
		if(deathCounter == 126){
			die();
			deathCounter = 0;
		}
	}


	public int getCurrentHealth(){
		return currentHealth;
	}

	public int getMaxHealth(){
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
		int sound = rand.nextInt(2);
		if(getCurrentHealth() > 0){
			GameMain.infoBox.append("\n You hit Bartuc for " + GameMain.decimals.format(damage) + " damage. He has " + GameMain.decimals.format(getCurrentHealth()) + " health left.");
			GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
		}
		if(sound == 0){
			hitPlayer.setFile(Engine.bartucHit1);
		}
		if(sound == 1){
			hitPlayer.setFile(Engine.bartucHit2);
		}
		hitSound = true;
		if(hitSound && !hitPlayer.isPlaying()){
			new Thread(hitPlayer).start();
		}
		hitSound = false;
	}

	public void reset(){
		xPosition = 480;
		yPosition = 128;
		aggro = false;
		dying = false;
		Engine.bartucThemeSound = false;
	}

	public void die(){
		DrawGame.character.addBartucKills();
		reset();
		alive = false;
		DrawGame.character.increaseXP(100*DrawGame.character.getBartucKills());
		DrawGame.character.getLoot();
		DrawGame.character.getLoot();
		DrawGame.character.getLoot();
		GameMain.infoBox.append(Engine.bartucDeathMessage);
		GameMain.infoBox.setCaretPosition(GameMain.infoBox.getDocument().getLength());
	}

	public void startDying(){
		if(!dying){
			new Thread(deathThemePlayer).start();
			new Thread(deathPlayer).start();
		}
		dying = true;
	}

	public void spawn(int kills){
		alive = true;
		aggro = false;
		maxHealth = (100*baseHealth) * (kills + 1); 
		currentHealth = maxHealth;
		damage = (10*baseDamage) * (kills + 1);	
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

	public void stopCasting() {
		for (int i = 0; i < shadowBlasts.length; i++) {
			shadowBlasts[i].stop();
		}
	}
}
