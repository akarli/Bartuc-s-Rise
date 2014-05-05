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
	JPanel gamePanel = new JPanel(); // Panel containing the game
	JPanel infoPanel = new JPanel(); // Panel containing the stats and info boxes
	
	DrawGame draw = new DrawGame(); // The game
	
	Timer t = new Timer(1000/60, this); // The game timer
	
	public GameMain(){
		super("Bartuc's Rise"); // Names the window
		t.start(); // Starts the game timer
		
		/**
		 * GAME WINDOW PREFERENCES
		 */
		
		setLayout(null); // Allows for free placement of components in this window
		setPreferredSize(new Dimension(32*Engine.TILE_WIDTH, 20*Engine.TILE_HEIGHT+220)); // Sets the size for the game window
		setResizable(false);
		setUndecorated(false);
		setBackground(Color.WHITE);  
		//gs.setFullScreenWindow(this);
		
		/**
		 * PANEL PREFERENCES
		 */
		
		gamePanel.setBounds(0, -5, 1024, 640); // Sets the size and position of the game panel
		infoPanel.setBounds(0, 635, 1024, 200); // Sets the size and position for the info panel
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setLayout(null); // Allows for free placement of components in this panel

		/**
		 * GAME PREFERENCES
		 */
		
		draw.setPreferredSize(new Dimension(1024, 640)); // Sets the size of the game
		
		/**
		 * BUILDNING THE WINDOW
		 */
		
		getContentPane().add(gamePanel); // Adds the game panel to its position
		getContentPane().add(infoPanel); // Adds the info panel to its position
		gamePanel.add(draw); // Adds the game to the game panel
		
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
