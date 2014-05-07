import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.swing.JPanel;

public class DrawGame extends JPanel implements KeyListener, MouseListener, MouseMotionListener{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static boolean up = false;
	static boolean down = false;
	static boolean left = false;
	static boolean right = false;
	static boolean newZone = true; //blir true när man går in i en ny zone
	static int moveCounter = 0;
	static int magicCounter = 0;
	static boolean pressed = false;
	static boolean attacking = false;
	public static final Character character = new Character();
	public Queue<Integer> Q; //kö för knapptryckningar vid move för character
	public static ArrayList[] monsterList; //en array med arraylists vid varje position, där platsen i arrayen är zonen och platserna i arraylisten är monstren i zonen
	static HashMap<Room, Integer> monsterHash; // en hashmap där varje room tilldelas en siffra
	private HashMap <Room, RoomOverlay> overlay;
	int monsterMoveCounter = 0;
	Random rand = new Random();


	public DrawGame(){
		monsterHash = new HashMap<Room, Integer>();
		overlay = new HashMap<Room, RoomOverlay>();
		monsterHash.put(Engine.centralZone, 0); //tilldelar rum till siffror
		monsterHash.put(Engine.northZone, 1);
		monsterHash.put(Engine.westZone, 2);
		monsterHash.put(Engine.southZone, 3);
		monsterHash.put(Engine.eastZone, 4);
		monsterHash.put(Engine.caveZone, 5);
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
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
	}

	@SuppressWarnings("unchecked")
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		character.getCurrentRoom().drawImage(g);

		monsterMoveCounter++;
		if(newZone){ //om du går in i en ny zon, detta körs bara en gång när du går in i en ny zon
			if(monsterList[monsterHash.get(character.getCurrentRoom())].size() == 0 ||
					monsterList[monsterHash.get(character.getCurrentRoom())] == null){ // kollar om listan i zonen du går in i är tom
				for(int j = 0; j< rand.nextInt(5)+3; j++){	//skapar i så fall ett slumpat antal nya mosnter
					monsterList[monsterHash.get(character.getCurrentRoom())].add(new Monster());
					Monster a = (Monster) monsterList[monsterHash.get(character.getCurrentRoom())].get(j);
					a.place();
				}
			}
			newZone = false;
		}
		for(int j = 0; j < monsterList[monsterHash.get(character.getCurrentRoom())].size();j++){ //ritar alla monster
			Monster a = (Monster) monsterList[monsterHash.get(character.getCurrentRoom())].get(j);
			a.drawImage(g);
			int random = rand.nextInt(100); //slumpar när monstret ska gå
			if(random >= 98){
				int randDirection = rand.nextInt(2); //slumpar åt vilket håll monstret ska gå
				if(a.getMoveCounter() == 16 || a.getMoveCounter() == 0){
					a.startMoving(randDirection);
				}
			}
		}

		character.drawImage(g);

		for(int i = 0; i < monsterList[monsterHash.get(character.getCurrentRoom())].size();i++){
			Monster a = (Monster) monsterList[monsterHash.get(character.getCurrentRoom())].get(i);
			if(a.getMoveCounter() < 16){
				a.move();
			}
		}


		if(left && moveCounter < 16){
			character.moveLeft();
			moveCounter++;
		}
		if(right && moveCounter < 16){
			character.moveRight();
			moveCounter++;
		}
		if(up && moveCounter < 16){
			character.moveUp();
			moveCounter++;
		}
		if(down && moveCounter < 16){
			character.moveDown();
			moveCounter++;
		}
		if(moveCounter == 16){
			moveCounter = 0;
			if(!pressed){
				up = false;
				left = false;
				right = false;
				down = false;
			}
			if(Q.size()!=0){
				int direction = Q.remove();
				if(direction == 1){
					up = true;
				}
				if(direction == 2){
					left = true;
				}
				if(direction == 3){
					down = true;
				}
				if(direction == 4){
					right = true;
				}
			}
		}
		overlay.get(character.getCurrentRoom()).drawImage(g);
		if(character.getXP() >= character.getMaxXP()){
			character.levelUp();
		}
		if(character.getCurrHP() <= 0){
			character.die();
		}
	}

	public static void reset(){
		up = false;
		down = false;
		left = false;
		right = false;
		newZone = true; 
		moveCounter = 0;
		magicCounter = 0;
		pressed = false;
		attacking = false;
		for(int i = 0; i < monsterList.length; i++){
			if(monsterList[i] != null){
				int a = monsterList[i].size();
				for(int j = 0; j < a; j++){
					monsterList[i].remove(j);
					a--;
				}
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
	public void mousePressed(MouseEvent arg0) {
		requestFocus();

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(!up && !left && !down && !right && !attacking){
			switch( keyCode ) {
			case KeyEvent.VK_W:
				pressed = true;
				up = true;
				break;
			case KeyEvent.VK_A:
				pressed = true;
				left = true;
				break;
			case KeyEvent.VK_S:
				pressed = true;
				down = true;
				break;
			case KeyEvent.VK_D:
				pressed = true;
				right = true;
				break;
			case KeyEvent.VK_C:
				character.castFireBall();
				break;
			case KeyEvent.VK_SPACE:
				attacking = true;
				character.attack();
				break;
			}
		}
		else{
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


	}

	@Override
	public void keyTyped(KeyEvent e) {


	}

}
