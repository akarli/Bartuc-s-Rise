import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

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
	
	/**
	 * CHARACTER SATISTICS LABELS
	 * These labels track the stats of the character.
	 * Here they are created with centered text.
	 */
	
	JLabel characterStatistics = new JLabel("Character Statistics");
	JLabel characterHealth = new JLabel("Health: " + DrawGame.character.getCurrHP() + " / " + DrawGame.character.getMaxHP(), JLabel.CENTER);
	JLabel characterDamage = new JLabel("Damage: " + DrawGame.character.getDamage(), JLabel.CENTER);
	JLabel characterArmor = new JLabel("Armor: " + DrawGame.character.getArmor(), JLabel.CENTER);
	JLabel characterLevel = new JLabel("Level: " + DrawGame.character.getLevel(), JLabel.CENTER);
	JLabel characterXP = new JLabel("Experience to next level: " + DrawGame.character.getXP() + "/" + DrawGame.character.getMaxXP(), JLabel.CENTER);
	JLabel characterMana = new JLabel("Mana: " + DrawGame.character.getMana() + "/" + DrawGame.character.getMaxMana(), JLabel.CENTER);
	
	
	public static TextArea infoBox = new TextArea(); // The box containing all info text
	public static TextField commandBox = new TextField(); // The box containing all info text
	
	JScrollPane scroll = new JScrollPane(infoBox, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Making the infoBox scrollable
	
	JButton sendButton = new JButton(buttonString); // The button that will send the input command.
	
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

		statsPanel.setBounds(0, 635, 512, 197); // Sets the size and position for the stats panel
		statsPanel.setBackground(Color.WHITE);
		statsPanel.setLayout(null); // Allows for free placement of components in this panel
		statsPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED)); // Adds a border to the panel

		infoPanel.setBounds(512, 635, 512, 200); // Sets the size and position for the info panel
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setLayout(null); // Allows for free placement of components in this panel

		/**
		 * GAME PREFERENCES
		 */

		draw.setPreferredSize(new Dimension(1024, 640)); // Sets the size of the game
		
		/**
		 * STATISTICS AREA PREFERENCES
		 * Sets the font, size and position for each label.
		 */
		
		characterStatistics.setFont(new Font("Serif", Font.PLAIN, 18));
		characterStatistics.setBounds(170, 0, 170, 50);
		
		characterHealth.setFont(new Font("Serif", Font.PLAIN, 14));
		characterHealth.setBounds(40, 40, 170, 50);
		
		characterDamage.setFont(new Font("Serif", Font.PLAIN, 14));
		characterDamage.setBounds(40, 90, 170, 50);
		
		characterArmor.setFont(new Font("Serif", Font.PLAIN, 14));
		characterArmor.setBounds(40, 140, 170, 50);
		
		characterLevel.setFont(new Font("Serif", Font.PLAIN, 14));
		characterLevel.setBounds(250, 40, 170, 50);
		
		characterXP.setFont(new Font("Serif", Font.PLAIN, 14));
		characterXP.setBounds(250, 90, 175, 50);
		
		characterMana.setFont(new Font("Serif", Font.PLAIN, 14));
		characterMana.setBounds(250, 140, 170, 50);

		/**
		 * SCROLL AREA PREFERENCES
		 */

		infoBox.setEditable(false); // Makes the info box uneditable
		scroll.setBounds(0, 0, 508, 170); // Sets the size and position of the info box
		infoBox.setBackground(Color.WHITE); // Sets the background color of the info box
		
		/**
		 * COMMAND AREA PREFERENCES
		 */
		
		sendButton.setBounds(430, 170, 75, 25); // Sets the size and position of the send button
		commandBox.setBounds(0, 170, 430, 25); // Sets the size and position of the command box

		/**
		 * BUILDNING THE WINDOW
		 */

		getContentPane().add(statsPanel); // Adds the info panel to its position
		getContentPane().add(infoPanel); // Adds the info panel to its position
		getContentPane().add(gamePanel); // Adds the game panel to its position
		
		gamePanel.add(draw); // Adds the game to the game panel
		
		infoPanel.add(scroll); // Adds the info box to the info panel
		infoPanel.add(sendButton); // Adds the send button to the info panel
		infoPanel.add(commandBox); // Adds the command box to the info panel
		
		statsPanel.add(characterStatistics); // Adds the header label
		statsPanel.add(characterHealth); // Adds the HP label
		statsPanel.add(characterDamage); // Adds the damage label
		statsPanel.add(characterArmor); // Adds the armor label
		statsPanel.add(characterLevel); // Adds the level label
		statsPanel.add(characterXP); // Adds the XP label
		statsPanel.add(characterMana); // Adds the mana label
		
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
		
		/**
		 * Updates all labels with the current information.
		 */
		
		characterHealth.setText("Health: " + DrawGame.character.getCurrHP() + " / " + DrawGame.character.getMaxHP());
		characterDamage.setText("Damage: " + DrawGame.character.getDamage());
		characterArmor.setText("Armor: " + DrawGame.character.getArmor());
		characterLevel.setText("Level: " + DrawGame.character.getLevel());
		characterXP.setText("Experience to next level: " + DrawGame.character.getXP() + "/" + DrawGame.character.getMaxXP());
		characterMana.setText("Mana: " + DrawGame.character.getMana() + "/" + DrawGame.character.getMaxMana());
		
		draw.repaint();
	}

}
