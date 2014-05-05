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
	static boolean newZone = true;
	static int moveCounter = 0;
	boolean pressed = false;
	public static final Character character = new Character();
	public Queue<Integer> Q;
	public ArrayList[] monsterList;
	private HashMap<Room, Integer> monsterHash;
	int monsterMoveCounter = 0;


	public DrawGame(){
		monsterHash = new HashMap<Room, Integer>();
		monsterHash.put(Engine.centralZone, 0);
		monsterHash.put(Engine.northZone, 1);
		monsterHash.put(Engine.westZone, 2);
		monsterHash.put(Engine.southZone, 3);
		monsterHash.put(Engine.eastZone, 4);
		monsterHash.put(Engine.caveZone, 5);
		Engine.centralZone.setExit("north", Engine.northZone);
		Engine.centralZone.setExit("west", Engine.westZone);
		Engine.centralZone.setExit("south", Engine.southZone);
		Engine.centralZone.setExit("east", Engine.eastZone);
		Engine.centralZone.setExit("cave", Engine.caveZone);
		Engine.northZone.setExit("south", Engine.centralZone);
		Engine.westZone.setExit("east", Engine.centralZone);
		Engine.eastZone.setExit("west", Engine.centralZone);
		Engine.southZone.setExit("north", Engine.centralZone);
		Engine.caveZone.setExit("south", Engine.centralZone);
		Q = new LinkedList<Integer>();
		monsterList = new ArrayList[6];
		for(int i = 0; i < 6; i++){
			monsterList[i] = new ArrayList<Monster>();
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
		if(newZone){

			if(monsterList[monsterHash.get(character.getCurrentRoom())].size() == 0 ||
				monsterList[monsterHash.get(character.getCurrentRoom())] == null){
				for(int j = 0; j< 4; j++){
					monsterList[monsterHash.get(character.getCurrentRoom())].add(new Monster());
					Monster a = (Monster) monsterList[monsterHash.get(character.getCurrentRoom())].get(j);
					a.place();
				}
			}
			
			newZone = false;

		}
		for(int j = 0; j < monsterList[monsterHash.get(character.getCurrentRoom())].size();j++){
			Monster a = (Monster) monsterList[monsterHash.get(character.getCurrentRoom())].get(j);
			a.drawImage(g);
		}
		
		if(monsterMoveCounter >= 120 ){
			for(int j = 0; j < monsterList[monsterHash.get(character.getCurrentRoom())].size();j++){
				Monster a = (Monster) monsterList[monsterHash.get(character.getCurrentRoom())].get(j);
				a.move();
			}
			monsterMoveCounter = 0;
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
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

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


	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(!up && !left && !down && !right){
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
			case KeyEvent.VK_SPACE:
				character.attack();
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
