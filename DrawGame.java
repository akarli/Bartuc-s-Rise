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
	Room centralZone, northZone, southZone, eastZone, westZone, caveZone;
	Character character;

	public DrawGame(){
		centralZone = new Room(Engine.centralZoneGround, Engine.centralZoneTop);
		northZone = new Room(Engine.northZoneGround, Engine.northZoneTop);
		southZone = new Room(Engine.southZoneGround, Engine.southZoneTop);
		eastZone = new Room(Engine.eastZoneGround, Engine.eastZoneTop);
		westZone = new Room(Engine.westZoneGround, Engine.westZoneTop);
		caveZone = new Room(Engine.caveZoneGround, Engine.caveZoneTop);
		character = new Character();

		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		centralZone.drawImage(g);
		character.drawImage(g);
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
	    	switch( keyCode ) { 
	        	case KeyEvent.VK_W:
	        		character.moveUp();
	        		break;
			case KeyEvent.VK_A:
	        		character.moveLeft();
	        		break;
	        	case KeyEvent.VK_S:
	        		character.moveDown();
	        	 	break;
		 	case KeyEvent.VK_D:
	        		character.moveRight();
	        		break;
	     }
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {


	}

}
