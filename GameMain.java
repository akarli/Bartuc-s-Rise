import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class GameMain extends JFrame implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	static GraphicsDevice gs = ge.getDefaultScreenDevice();
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	DrawGame draw = new DrawGame();
	
	Timer t = new Timer(1000/60, this);
	
	public GameMain(){
		super("Bartuc's Rise");
		
		t.start();
		
		setPreferredSize(new Dimension(32*Engine.TILE_WIDTH, 20*Engine.TILE_HEIGHT+25));
		setResizable(false);
		setUndecorated(false);
		setBackground(Color.WHITE);  
		//gs.setFullScreenWindow(this);
		
		getContentPane().add(draw);
		pack();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		}
	
	public static void main (String args[]){
		GameMain gw = new GameMain();
		gw.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent a) {
		draw.repaint();		
	}

}
