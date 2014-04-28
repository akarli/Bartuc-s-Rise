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
	
	Character character;

	public DrawGame(){

		character = new Character();

		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		character.getCurrentRoom().drawImage(g);
		character.drawImage(g);
		if(left){
			character.moveLeft();
		}
		if(right){
			character.moveRight();
		}
		if(up){
			character.moveUp();
		}
		if(down){
			character.moveDown();
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
		switch( keyCode ) {
		case KeyEvent.VK_W:
			up = true;
			left = false;
			right = false;
			down = false;
			break;
		case KeyEvent.VK_A:
			left = true;
			right = false;
			up = false;
			down = false;
			break;
		case KeyEvent.VK_S:
			down = true;
			left = false;
			up = false;
			right = false;
			break;
		case KeyEvent.VK_D:
			right = true;
			left = false;
			up = false;
			down = false;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch( keyCode ) {
		case KeyEvent.VK_W:
			up = false;
			break;
		case KeyEvent.VK_A:
			left = false;
			break;
		case KeyEvent.VK_S:
			down = false;
			break;
		case KeyEvent.VK_D:
			right = false;
			break;
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {


	}

}
