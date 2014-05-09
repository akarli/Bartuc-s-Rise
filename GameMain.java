import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class GameMain extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	static GraphicsDevice gs = ge.getDefaultScreenDevice();
	
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int screenWidth = (int) screenSize.getWidth();
	private int screenHeight = (int) screenSize.getHeight();
	
	static DecimalFormat oneDigit = new DecimalFormat("#,##0.0");
	
	private String buttonString = "Send"; // The text string for the send button
	
	JPanel gamePanel = new JPanel(); // Panel containing the game
	JPanel statsPanel = new JPanel(); // Panel containing the stats
	JPanel infoPanel = new JPanel(); // Panel containing the info box
	
	/**
	 * CHARACTER SATISTICS LABELS
	 * These labels track the statistics of the character.
	 * Here they are created with correct text alignment.
	 */
	
	JLabel characterStatistics = new JLabel(DrawGame.character.getName());
	JLabel characterHealth = new JLabel("Health: " + DrawGame.character.getCurrHP() + " / " + DrawGame.character.getMaxHP(), JLabel.LEFT);
	JLabel characterDamage = new JLabel("Damage: " + DrawGame.character.getDamage(), JLabel.LEFT);
	JLabel characterArmor = new JLabel("Armor: " + DrawGame.character.getArmor(), JLabel.LEFT);
	JLabel characterHPRegen = new JLabel("HP/sec: " + oneDigit.format(DrawGame.character.getHPRegen()), JLabel.LEFT);
	JLabel characterManaRegen = new JLabel("Mana/sec: " + oneDigit.format(DrawGame.character.getManaRegen()), JLabel.LEFT);
	JLabel characterHealthPots = new JLabel("Health potions: " + DrawGame.character.getHPPots(), JLabel.LEFT);
	JLabel characterLevel = new JLabel("Level: " + DrawGame.character.getLevel(), JLabel.LEFT);
	JLabel characterXP = new JLabel("Experience: " + DrawGame.character.getXP() + "/" + DrawGame.character.getMaxXP(), JLabel.LEFT);
	JLabel characterMana = new JLabel("Mana: " + DrawGame.character.getMana() + "/" + DrawGame.character.getMaxMana(), JLabel.LEFT);
	JLabel characterManaPots = new JLabel("Mana potions: " + DrawGame.character.getManaPots(), JLabel.LEFT);
	
	
	public static JTextArea infoBox = new JTextArea(Engine.startMessage); // The box containing all info text
	public static JTextField commandBox = new JTextField(); // The box containing all info text
	
	JScrollPane scroll = new JScrollPane(infoBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Making the infoBox scrollable

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
		setLocation((screenWidth/2) - 512, (screenHeight/2) - 430); // Centers the window
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

		infoPanel.setBounds(513, 635, 512, 200); // Sets the size and position for the info panel
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
		characterStatistics.setBounds(40, 0, 300, 50);
		
		characterHealth.setFont(new Font("Serif", Font.PLAIN, 14));
		characterHealth.setBounds(40, 40, 171, 50);
		
		characterDamage.setFont(new Font("Serif", Font.PLAIN, 14));
		characterDamage.setBounds(40, 140, 171, 50);
		
		characterArmor.setFont(new Font("Serif", Font.PLAIN, 14));
		characterArmor.setBounds(205, 140, 171, 50);
		
		characterHPRegen.setFont(new Font("Serif", Font.PLAIN, 14));
		characterHPRegen.setBounds(205, 40, 170, 50);
		
		characterManaRegen.setFont(new Font("Serif", Font.PLAIN, 14));
		characterManaRegen.setBounds(205, 90, 170, 50);
		
		characterHealthPots.setFont(new Font("Serif", Font.PLAIN, 14));
		characterHealthPots.setBounds(360, 40, 171, 50);

		characterLevel.setFont(new Font("Serif", Font.PLAIN, 18));
		characterLevel.setBounds(340, 0, 171, 50);
		
		characterXP.setFont(new Font("Serif", Font.PLAIN, 14));
		characterXP.setBounds(360, 140, 171, 50);
		
		characterMana.setFont(new Font("Serif", Font.PLAIN, 14));
		characterMana.setBounds(40, 90, 171, 50);
		
		characterManaPots.setFont(new Font("Serif", Font.PLAIN, 14));
		characterManaPots.setBounds(360, 90, 171, 50);

		/**
		 * SCROLL AREA PREFERENCES
		 */

		infoBox.setEditable(false); // Makes the info box uneditable
		scroll.setBounds(0, 0, 507, 170); // Sets the size and position of the info box
		infoBox.setBackground(Color.WHITE); // Sets the background color of the info box
		infoBox.setFont(new Font("Serif", Font.PLAIN, 14)); // Sets the font of the info box
		
		/**
		 * COMMAND AREA PREFERENCES
		 */
		
		sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
            	String input = commandBox.getText();
				if(input.trim().equals("")){
					infoBox.append("\n No command entered.");
					infoBox.setCaretPosition(infoBox.getDocument().getLength());
				}
            	else{
					commandBox.setText("");
					infoBox.append("\n Command entered: \"" + input + "\".");
					infoBox.setCaretPosition(infoBox.getDocument().getLength());
					parseCommand(input.trim());
				}
				draw.requestFocus();
            }
        }); 
		sendButton.setBounds(430, 170, 74, 25); // Sets the size and position of the send button
		sendButton.setFont(new Font("Serif", Font.PLAIN, 14)); // Sets the font of the send button
		
		commandBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				String input = commandBox.getText();
				if(input.trim().equals("")){
					infoBox.append("\n No command entered.");
					infoBox.setCaretPosition(infoBox.getDocument().getLength());
				}
            	else{
					commandBox.setText("");
					infoBox.append("\n Command entered: \"" + input + "\".");
					infoBox.setCaretPosition(infoBox.getDocument().getLength());
					parseCommand(input.trim());
				}
				draw.requestFocus();
			}
		});
		commandBox.setBounds(0, 170, 430, 25); // Sets the size and position of the command box
		commandBox.setFont(new Font("Serif", Font.PLAIN, 14)); // Sets the font of the command box

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
		statsPanel.add(characterHPRegen);
		statsPanel.add(characterManaRegen);
		statsPanel.add(characterHealthPots);
		statsPanel.add(characterManaPots);
		
		pack();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
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
		
		characterStatistics.setText(DrawGame.character.getName());
		characterHealth.setText("Health: " + (int)DrawGame.character.getCurrHP() + " / " + (int)DrawGame.character.getMaxHP());
		characterDamage.setText("Damage: " + DrawGame.character.getDamage());
		characterArmor.setText("Armor: " + DrawGame.character.getArmor());
		characterLevel.setText("Level: " + DrawGame.character.getLevel());
		characterXP.setText("Experience: " + DrawGame.character.getXP() + "/" + DrawGame.character.getMaxXP());
		characterMana.setText("Mana: " + (int)DrawGame.character.getMana() + "/" + (int)DrawGame.character.getMaxMana());
		characterHPRegen.setText("HP/s: " + oneDigit.format(DrawGame.character.getHPRegen()));
		characterManaRegen.setText("Mana/s: " + oneDigit.format(DrawGame.character.getManaRegen()));
		characterHealthPots.setText("Health Potions: " + DrawGame.character.getHPPots());
		characterManaPots.setText("Mana potions: " + DrawGame.character.getManaPots());

		draw.repaint();
	}
	
	public void parseCommand(String command){
		String lineArray[] = command.split("\\s+");
		//String inputCommand = command;
		if((lineArray[0].trim().equals("help") && lineArray.length == 1) || (lineArray[0].trim().equals("Help") && lineArray.length == 1)){
			infoBox.append(Engine.helpMessage);
			infoBox.setCaretPosition(infoBox.getDocument().getLength());
		}
		else if((lineArray[0].trim().equals("load") && lineArray.length == 1) || (lineArray[0].trim().equals("Load") && lineArray.length == 1)){
			infoBox.append("\n Loading...");
			loadGame();
			infoBox.setCaretPosition(infoBox.getDocument().getLength());
		}
		else if((lineArray[0].trim().equals("save") && lineArray.length == 1)|| (lineArray[0].trim().equals("Save") && lineArray.length == 1)){
			infoBox.append("\n Saving...");
			saveGame();
			infoBox.setCaretPosition(infoBox.getDocument().getLength());
		}
		else if((lineArray[0].trim().equals("controls") && lineArray.length == 1) || (lineArray[0].trim().equals("Controls") && lineArray.length == 1)){
			infoBox.append(Engine.controlsMessage);
			infoBox.setCaretPosition(infoBox.getDocument().getLength());
		}
		else if((lineArray[0].trim().equals("commands") && lineArray.length == 1) || (lineArray[0].trim().equals("Commands") && lineArray.length == 1)){
			infoBox.append(Engine.commandsMessage);
			infoBox.setCaretPosition(infoBox.getDocument().getLength());
		}
		else if((lineArray[0].trim().equals("stats") && lineArray.length == 1) || (lineArray[0].trim().equals("Stats") && lineArray.length == 1)){
			infoBox.append(Engine.statsMessage);
			infoBox.setCaretPosition(infoBox.getDocument().getLength());
		}
		else if((lineArray[0].trim().equals("setname") || lineArray[0].trim().equals("Setname")) && lineArray.length <= 4){
			if(lineArray.length == 2){
				DrawGame.character.setName(lineArray[1]);
			}
			else if(lineArray.length == 3){
				String name = lineArray[1] + " " + lineArray[2];
				DrawGame.character.setName(name);
			}
			else if(lineArray.length == 4){
				String name = lineArray[1] + " " + lineArray[2] + " " + lineArray[3];
				DrawGame.character.setName(name);
			}
			infoBox.append(Engine.setNameMessage);
			infoBox.setCaretPosition(infoBox.getDocument().getLength());
		}
		else{
			infoBox.append(Engine.noSuchCommandMessage);
			infoBox.setCaretPosition(infoBox.getDocument().getLength());
		}
	}
	
	public void saveGame(){
		PrintWriter writer = null;
		try {
		    writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream("savegame.txt"), "utf-8"));
		    writer.println(DrawGame.character.getName());
		    writer.println("Level: ");
		    writer.println(DrawGame.character.getLevel());
		    writer.println("Max Health: ");
		    writer.println(DrawGame.character.getMaxHP());
		    writer.println("Current Health: ");
		    writer.println(DrawGame.character.getCurrHP());
		    writer.println("Health regeneration per second:");
		    writer.println(DrawGame.character.getHPRegen());
		    writer.println("Max Mana: ");
		    writer.println(DrawGame.character.getMaxMana());
		    writer.println("Current Mana: ");
		    writer.println(DrawGame.character.getMana());
		    writer.println("Mana regeneration per second:");
		    writer.println(DrawGame.character.getManaRegen());
		    writer.println("Damage: ");
		    writer.println(DrawGame.character.getDamage());
		    writer.println("Armor: ");
		    writer.println(DrawGame.character.getArmor());
		    writer.println("Experience: ");
		    writer.println(DrawGame.character.getXP());
		    writer.println("Health potions:");
		    writer.println(DrawGame.character.getHPPots());
		    writer.println("Mana potions:");
		    writer.println(DrawGame.character.getManaPots());
		    
		    infoBox.append(Engine.saveMessage);
		} catch (IOException ex) {
		  infoBox.append("\n Something went wrong, game not saved.");
		  return;
		} finally {
		   writer.close();
		}
	}
	
	public void loadGame() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("savegame.txt"), "UTF-8"));
			ArrayList<Double> loadList = new ArrayList<Double>();
		    String line = in.readLine();
		    DrawGame.character.setName(line);
		    while ((line = in.readLine()) != null){
			   line = in.readLine();
			   if (!line.matches("^[^\\d].*")) {
					loadList.add(Double.parseDouble(line));
				}
		    }
		    in.close();
		    
		    DrawGame.character.setLevel(loadList.get(0).intValue());
		    DrawGame.character.setHP(loadList.get(1));
		    DrawGame.character.setCurrHP(loadList.get(2));
		    DrawGame.character.loadHPRegen(loadList.get(3));
		    DrawGame.character.setMana(loadList.get(4));
		    DrawGame.character.setCurrMana(loadList.get(5));
		    DrawGame.character.loadManaRegen(loadList.get(6));
		    DrawGame.character.setDamage(loadList.get(7).intValue());
		    DrawGame.character.setArmor(loadList.get(8).intValue());
		    DrawGame.character.setXP(loadList.get(9).intValue());   
		    DrawGame.character.loadHPPots(loadList.get(10).intValue());
		    DrawGame.character.loadManaPots(loadList.get(11).intValue());
		    
		    DrawGame.character.setMaxXP();
		    DrawGame.character.setRoom(Engine.centralZone);
		    DrawGame.character.setX(448);
			DrawGame.character.setY(416);
			DrawGame.reset();
		    
			infoBox.append(Engine.loadMessage);
		    
		} catch (FileNotFoundException a){
			infoBox.append("\n The save file was not found. Game not loaded.");
		}
		catch (IOException e) {
			infoBox.append("\n There was a problem with the save file. Game not loaded.");
		}
	}
}
