import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DrawGame extends JPanel implements KeyListener, MouseListener, MouseMotionListener{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	static boolean newZone = true; //blir true när man går in i en ny zone
	static int moveCounter = 0;
	static boolean pressed = false;
	static boolean attacking = false;
	public static final Character character = new Character();
	private Queue<Integer> Q; //kö för knapptryckningar vid move för character
	public static ArrayList[] monsterList; //en array med arraylists vid varje position, där platsen i arrayen är zonen och platserna i arraylisten är monstren i zonen
	static HashMap<Room, Integer> monsterHash; // en hashmap där varje room tilldelas en siffra
	private HashMap <Room, RoomOverlay> overlay;
	int monsterMoveCounter = 0, direction = 0, castShadowCounter=0;
	Random rand = new Random();
	int lastKey;
	boolean wait;
	public static Bartuc bartuc;
	private BufferedImage bartucsymbol;

	public DrawGame(){
		monsterHash = new HashMap<Room, Integer>();
		overlay = new HashMap<Room, RoomOverlay>();
		monsterHash.put(Engine.centralZone, 0); //tilldelar rum till siffror
		monsterHash.put(Engine.northZone, 1);
		monsterHash.put(Engine.westZone, 2);
		monsterHash.put(Engine.southZone, 3);
		monsterHash.put(Engine.eastZone, 4);
		monsterHash.put(Engine.caveZone, 5);
		bartucsymbol = loadImage("Graphics\\bartucsymbol.png");
		Engine.centralZone.setExit("north", Engine.northZone); // sätter exits för rummen och vart de leder
		Engine.centralZone.setExit("west", Engine.westZone);
		Engine.centralZone.setExit("south", Engine.southZone);
		Engine.centralZone.setExit("east", Engine.eastZone);
		Engine.centralZone.setExit("cave", Engine.caveZone);
		Engine.northZone.setExit("south", Engine.centralZone);
		Engine.westZone.setExit("east", Engine.centralZone);
		Engine.eastZone.setExit("west", Engine.centralZone);
		Engine.southZone.setExit("north", Engine.centralZone);
		Engine.caveZone.setExit("south", Engine.centralZone);
		overlay.put(Engine.centralZone, Engine.centralOverlay);
		overlay.put(Engine.northZone, Engine.northOverlay);
		overlay.put(Engine.westZone, Engine.westOverlay);
		overlay.put(Engine.eastZone, Engine.eastOverlay);
		overlay.put(Engine.southZone, Engine.southOverlay);
		overlay.put(Engine.caveZone, Engine.caveOverlay);
		Q = new LinkedList<Integer>();
		monsterList = new ArrayList[6]; //skapar 6 platser i arrayen (6 zoner)
		for(int i = 0; i < 6; i++){
			monsterList[i] = new ArrayList<Monster>(); //skapar arraylistorna i varje zon
		}
		bartuc = new Bartuc(DrawGame.character.getBartucKills());
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
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

	@SuppressWarnings("unchecked")
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		character.getCurrentRoom().drawImage(g);

		monsterMoveCounter++;
		if(newZone){ //om du går in i en ny zon, detta körs bara en gång när du går in i en ny zon
			if(monsterList[monsterHash.get(character.getCurrentRoom())].size() == 0 ||
					monsterList[monsterHash.get(character.getCurrentRoom())] == null){ // kollar om listan i zonen du går in i är tom
				for(int j = 0; j< rand.nextInt(6)+5; j++){	//skapar i så fall ett slumpat antal nya mosnter
					monsterList[monsterHash.get(character.getCurrentRoom())].add(new Monster());
					Monster a = (Monster) monsterList[monsterHash.get(character.getCurrentRoom())].get(j);
					a.place();
				}
			}
			newZone = false;
			bartuc.stopCasting();
		}
		if(character.getCurrentRoom() == Engine.caveZone){
			g.drawImage(bartucsymbol, 463, 113, null);
		}
		for(int j = 0; j < monsterList[monsterHash.get(character.getCurrentRoom())].size();j++){ //ritar alla monster
			Monster a = (Monster) monsterList[monsterHash.get(character.getCurrentRoom())].get(j);
			a.drawImage(g);
			int random = rand.nextInt(100); //slumpar när monstret ska gå
			if(random >= 98){
				int randDirection = rand.nextInt(2); //slumpar åt vilket håll monstret ska gå
				if(!a.moving() && !a.attacking()){
					a.startMoving(randDirection);
				}
			}
		}
		if (DrawGame.character.getCurrentRoom() == Engine.caveZone) {
			if (bartuc.aggro && bartuc.alive) {
				int random = rand.nextInt(100);
				if (random >= 98) {
					bartuc.castShadowBlast();
				}
			}
		}
		if(bartuc.alive && character.getCurrentRoom() == Engine.caveZone){
			bartuc.drawImage(g); // ritar bartuc om du är i cavezone
		}
		character.drawImage(g);

		for(int i = 0; i < monsterList[monsterHash.get(character.getCurrentRoom())].size();i++){
			Monster a = (Monster) monsterList[monsterHash.get(character.getCurrentRoom())].get(i);
			if(a.moving()){
				a.move();
			}
		}
		overlay.get(character.getCurrentRoom()).drawImage(g);
		if(character.getXP() >= character.getMaxXP()){
			character.levelUp();
		}
		if(character.getCurrHP() <= 0){
			character.die();
		}

		if(!character.moving()){
			if(Q.size() != 0){
				int dir = Q.remove();
				if(dir == 1){
					character.startMoving("up");
				}
				if(dir == 2){
					character.startMoving("left");
				}
				if(dir == 3){
					character.startMoving("down");
				}
				if(dir == 4){
					character.startMoving("right");
				}
			}
		}

		if(bartuc.alive && bartuc.getCurrentHealth()<=0 && !bartuc.dying){
			bartuc.startDying();
			Engine.bartucThemeSound = false;
		}

		/**
		 * SOUND CHECKS
		 * Checks which sound to play
		 */

		if(!Engine.mainThemePlayer.isPlaying() && Engine.mainThemeSound){
			Engine.mainThemePlayer.setVolume(0.1f);
			new Thread(Engine.mainThemePlayer).start();
		}

		if(!Engine.caveThemePlayer.isPlaying() && Engine.caveThemeSound){
			Engine.caveThemePlayer.setVolume(0.1f);
			new Thread(Engine.caveThemePlayer).start();
		}
		if(!Engine.bartucThemePlayer.isPlaying() && Engine.bartucThemeSound){
			Engine.bartucThemePlayer.setVolume(0.075f);
			new Thread(Engine.bartucThemePlayer).start();
		}
		if(Engine.hitSound && !Engine.hitPlayer.isPlaying()){
			Engine.hitPlayer.setVolume(0.03f);
			new Thread(Engine.hitPlayer).start();
			Engine.hitSound = false;
		}
		if(!Engine.fireBallChargePlayer.isPlaying() && Engine.fireBallChargeSound){
			Engine.fireBallChargePlayer.setVolume(0.03f);
			new Thread(Engine.fireBallChargePlayer).start();
			Engine.fireBallChargeSound = false;
		}
		if(!Engine.fireBallPlayer.isPlaying() && Engine.fireballSound){
			Engine.fireBallPlayer.setVolume(0.02f);
			new Thread(Engine.fireBallPlayer).start();
			Engine.fireballSound = false;
		}
		if(!Engine.potionPlayer.isPlaying() && Engine.potionSound){
			Engine.potionPlayer.setVolume(0.05f);
			new Thread(Engine.potionPlayer).start();
			Engine.potionSound = false;
		}
		if(!Engine.levelUpPlayer.isPlaying() && Engine.levelUpSound){
			Engine.levelUpPlayer.setVolume(0.02f);
			new Thread(Engine.levelUpPlayer).start();
			Engine.levelUpSound = false;
		}
		if(!Engine.skeletonHitPlayer.isPlaying() && Engine.skeletonHitSound){
			Engine.skeletonHitPlayer.setVolume(0.02f);
			new Thread(Engine.skeletonHitPlayer).start();
			Engine.skeletonHitSound = false;
		}
		if(!Engine.playerDeathPlayer.isPlaying() && Engine.playerDeathSound){
			Engine.playerDeathPlayer.setVolume(0.05f);
			new Thread(Engine.playerDeathPlayer).start();
			Engine.playerDeathSound = false;
		}
		if(!Engine.lootPlayer.isPlaying() && Engine.lootSound){
			Engine.lootPlayer.setVolume(0.05f);
			new Thread(Engine.lootPlayer).start();
			Engine.lootSound = false;
		}
		if(!Engine.potionPickupPlayer.isPlaying() && Engine.potionPickupSound){
			Engine.potionPickupPlayer.setVolume(0.05f);
			new Thread(Engine.potionPickupPlayer).start();
			Engine.potionPickupSound = false;
		}
		if(!Engine.walkPlayer.isPlaying() && Engine.walkingSound){
			Engine.walkPlayer.setVolume(0.01f);
			new Thread(Engine.walkPlayer).start();
			Engine.walkingSound = false;
		}
		if(!Engine.writePlayer.isPlaying() && Engine.writingSound){
			Engine.writePlayer.setVolume(0.2f);
			new Thread(Engine.writePlayer).start();
			Engine.writingSound = false;
		}
		if(!Engine.mainThemeSound){
			Engine.mainThemePlayer.stop();
		}
		if(!Engine.caveThemeSound){
			Engine.caveThemePlayer.stop();
		}
		if(!Engine.bartucThemeSound){
			Engine.bartucThemePlayer.stop();
		}
	}

	public static void reset(){

		newZone = true; 
		moveCounter = 0;
		pressed = false;
		attacking = false;
		bartuc.aggro = false;
		Engine.caveThemeSound = false;
		Engine.bartucThemeSound = false;
		Engine.mainThemeSound = true;
		for(int i = 0; i < monsterList.length; i++){
			if(monsterList[i] != null){
				while(monsterList[i].size() > 0){
					monsterList[i].remove(0);
				}
			}
		}
	}
	
	public static void clearDeadMonster(Room room) {
		int size = monsterList[monsterHash.get(room)].size();
		for (int i = size - 1; i >= 0; i--) {
			Monster a = (Monster) monsterList[monsterHash.get(room)].get(i);
			if (a.getHealth() <= 0) {
				monsterList[monsterHash.get(room)].remove(i);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {


	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)){
			if(!character.attacking && !character.moving()){
				Point a = e.getPoint();
				if(a.getX()/32 > character.getXTile()+1 && Math.abs(a.getY()/32 - character.getYTile())< 3){
					character.castFireBall("right");
				}
				if(a.getX()/32 < character.getXTile() && Math.abs(a.getY()/32 - character.getYTile())< 3){
					character.castFireBall("left");
				}
				if(a.getY()/32 > character.getYTile()){
					character.castFireBall("down");
				}
				if(a.getY()/32 < character.getYTile()+1){
					character.castFireBall("up");
				}
			}
		}
		if(SwingUtilities.isLeftMouseButton(e)){
			if(!character.attacking && !character.moving()){
				Point a = e.getPoint();
				if(a.getX()/32 > character.getXTile()+1 && Math.abs(a.getY()/32 - character.getYTile())< 3){
					character.attack("right");
				}
				if(a.getX()/32 < character.getXTile() && Math.abs(a.getY()/32 - character.getYTile())< 3){
					character.attack("left");
				}
				if(a.getY()/32 > character.getYTile()){
					character.attack("down");
				}
				if(a.getY()/32 < character.getYTile()+1){
					character.attack("up");
				}
			}
		}
		requestFocus();

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(!pressed && !character.moving()){
			switch( keyCode ) {
			case KeyEvent.VK_W:
				pressed = true;
				if(!character.attacking){
					character.startMoving("up");
					direction = 1;
					lastKey = keyCode;
				}
				break;
			case KeyEvent.VK_A:
				pressed = true;
				if(!character.attacking){
					character.startMoving("left");
					direction = 2;
					lastKey = keyCode;
				}
				break;
			case KeyEvent.VK_S:
				pressed = true;
				if(!character.attacking){
					character.startMoving("down");
					direction = 3;
					lastKey = keyCode;
				}
				break;
			case KeyEvent.VK_D:
				pressed = true;
				if(!character.attacking){
					character.startMoving("right");
					direction = 4;
					lastKey = keyCode;
				}
				break;

			}
			switch(keyCode){
			case KeyEvent.VK_Q:
				character.useHPPot();
				break;
			case KeyEvent.VK_E:
				character.useManaPot();
				break;
			}
		}
		else if (lastKey != keyCode && !character.attacking){
			switch(keyCode){
			case KeyEvent.VK_W:
				if(Q.size()<1){
					Q.add(1);
				}
				break;
			case KeyEvent.VK_A:
				if(Q.size()<1){
					Q.add(2);
				}
				break;
			case KeyEvent.VK_S:
				if(Q.size()<1){
					Q.add(3);
				}
				break;
			case KeyEvent.VK_D:
				if(Q.size()<1){
					Q.add(4);
				}
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();

		switch( keyCode ) {
		case KeyEvent.VK_W:
			pressed = false;
			break;
		case KeyEvent.VK_A:
			pressed = false;
			break;
		case KeyEvent.VK_S:
			pressed = false;
			break;
		case KeyEvent.VK_D:
			pressed = false;
			break;
		}
		pressed = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {


	}

}
