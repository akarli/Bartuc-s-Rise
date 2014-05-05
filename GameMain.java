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
	private String buttonString = "Send";
	JPanel gamePanel = new JPanel(); // Panel containing the game
	JPanel statsPanel = new JPanel(); // Panel containing the stats
	JPanel infoPanel = new JPanel(); // Panel containing the info box
	
	public static TextArea infoBox = new TextArea(); // The box containing all info text
	public static TextField commandBox = new TextField(); // The box containing all info text
	
	JScrollPane scroll = new JScrollPane(infoBox, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Making the infoBox scrollable
	
	JButton sendButton = new JButton(buttonString);
	
	DrawGame draw = new DrawGame(); // The game

	Timer t = new Timer(1000 / 60, this); // The game timer

	public GameMain() {
		super("Bartuc's Rise"); // Names the window
		t.start(); // Starts the game timer

		/**
		 * GAME WINDOW PREFERENCES
		 */

		setLayout(null); // Allows for free placement of components in this window
		setPreferredSize(new Dimension(32 * Engine.TILE_WIDTH, 20 * Engine.TILE_HEIGHT + 220)); // Sets the size for the game window
		setResizable(false);
		setUndecorated(false);
		setBackground(Color.WHITE);
		// gs.setFullScreenWindow(this);

		/**
		 * PANEL PREFERENCES
		 */

		gamePanel.setBounds(0, -5, 1024, 640); // Sets the size and position of the game panel

		statsPanel.setBounds(0, 635, 512, 200); // Sets the size and position for the stats panel
		statsPanel.setBackground(Color.WHITE);
		statsPanel.setLayout(null); // Allows for free placement of components in this panel

		infoPanel.setBounds(512, 635, 512, 200); // Sets the size and position for the info panel
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setLayout(null); // Allows for free placement of components in this panel

		/**
		 * GAME PREFERENCES
		 */

		draw.setPreferredSize(new Dimension(1024, 640)); // Sets the size of the game

		/**
		 * SCROLL AREA PREFERENCES
		 */

		infoBox.setEditable(false);
		scroll.setBounds(0, 0, 508, 170);
		infoBox.setBackground(Color.WHITE);
		
		/**
		 * COMMAND AREA PREFERENCES
		 */
		
		sendButton.setBounds(430, 170, 75, 25);
		commandBox.setBounds(0, 170, 430, 25);

		/**
		 * BUILDNING THE WINDOW
		 */

		getContentPane().add(statsPanel); // Adds the info panel to its position
		getContentPane().add(infoPanel); // Adds the info panel to its position
		getContentPane().add(gamePanel); // Adds the game panel to its position
		gamePanel.add(draw); // Adds the game to the game panel
		infoPanel.add(scroll);
		infoPanel.add(sendButton);
		infoPanel.add(commandBox);

		pack();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		infoBox.append("asda\ns\ndss\n\nsss\nssss\nssada\nssdads\nahgd\nsaghsdagdsh\njdjhsdsaj\nhgdsajgh\nsadkjh\nadsjk\nhadshg\njka\nsdhj\nkadsgsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssshjds");
		infoBox.append("\nHej!");
	}

	public static void main(String args[]) {
		GameMain gw = new GameMain();
		gw.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		draw.repaint();
	}

}
