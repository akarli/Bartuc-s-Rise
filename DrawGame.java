import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class DrawGame extends JPanel implements KeyListener, MouseListener, MouseMotionListener{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	boolean up = false;
	boolean down = false;
	boolean left = false;
	boolean right = false;
	int moveCounter = 0;
	public static final Character character = new Character();
	
	public DrawGame(){

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

		
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		character.getCurrentRoom().drawImage(g);
		//character.drawImage(g);

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
			up = false;
			left = false;
			right = false;
			down = false;
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
		if(!up && !left && !right && !down){
			switch( keyCode ) {
			case KeyEvent.VK_W:
				up = true;
				break;
			case KeyEvent.VK_A:
				left = true;
				break;
			case KeyEvent.VK_S:
				down = true;
				break;
			case KeyEvent.VK_D:
				right = true;
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {


	}

	@Override
	public void keyTyped(KeyEvent e) {


	}

}
