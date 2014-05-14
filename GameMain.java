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
	static DecimalFormat decimals = new DecimalFormat( "#,###,###,###" );

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
	JLabel characterHealth = new JLabel("Health: " + decimals.format(DrawGame.character.getCurrHP()) + " / " + decimals.format(DrawGame.character.getMaxHP()), JLabel.LEFT);
	JLabel characterDamage = new JLabel("Damage: " + decimals.format(DrawGame.character.getDamage()), JLabel.LEFT);
	JLabel characterArmor = new JLabel("Armor: " + decimals.format(DrawGame.character.getArmor()), JLabel.LEFT);
	JLabel characterHPRegen = new JLabel("HP/sec: " + oneDigit.format(DrawGame.character.getHPRegen()), JLabel.LEFT);
	JLabel characterManaRegen = new JLabel("Mana/sec: " + oneDigit.format(DrawGame.character.getManaRegen()), JLabel.LEFT);
	JLabel characterHealthPots = new JLabel("Health potions: " + decimals.format(DrawGame.character.getHPPots()), JLabel.LEFT);
	JLabel characterLevel = new JLabel("Level: " + decimals.format(DrawGame.character.getLevel()), JLabel.LEFT);
	JLabel characterXP = new JLabel("Experience: " + decimals.format(DrawGame.character.getXP()) + "/" + decimals.format(DrawGame.character.getMaxXP()), JLabel.LEFT);
	JLabel characterMana = new JLabel("Mana: " + decimals.format(DrawGame.character.getMana()) + "/" + decimals.format(DrawGame.character.getMaxMana()), JLabel.LEFT);
	JLabel characterManaPots = new JLabel("Mana potions: " + decimals.format(DrawGame.character.getManaPots()), JLabel.LEFT);
	JLabel characterCritChance = new JLabel("Critical hit chance: " + decimals.format(DrawGame.character.gloves().getBonusStat()) + "%", JLabel.LEFT);
	JLabel characterCritDamage = new JLabel("Critical hit damage: " + decimals.format(DrawGame.character.helm().getBonusStat()) + "%", JLabel.LEFT);
	JLabel characterDodge = new JLabel("Dodge chance: " + decimals.format(DrawGame.character.boots().getBonusStat()) + "%", JLabel.LEFT);
	
	public static JTextArea infoBox = new JTextArea(Engine.startMessage); // The box containing all info text
	public static JTabbedPane equipmentWindow = new JTabbedPane();
	public static JTextField commandBox = new JTextField(); // The box containing all info text
	
	public static JTextArea equipmentBox = new JTextArea("");
	public static JTextArea inventoryBox = new JTextArea("asda");


	JScrollPane scroll = new JScrollPane(infoBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Making the infoBox scrollable
	JScrollPane scrollEquipment = new JScrollPane(equipmentBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	JScrollPane scrollInventory = new JScrollPane(inventoryBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
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
		setPreferredSize(new Dimension(32 * Engine.TILE_WIDTH + 300, 20 * Engine.TILE_HEIGHT + 120));
		setLocation((screenWidth/2) - 670, (screenHeight/2) - 320);
		setResizable(false);
		setUndecorated(false);
		setBackground(Color.WHITE);
		// gs.setFullScreenWindow(this);

		/**
		 * PANEL PREFERENCES
		 */
		
			gamePanel.setBounds(0, -5, 1024, 640); // Sets the size and position of the game panel

			statsPanel.setBounds(1024, -5, 300, 740); // Sets the size and position for the stats panel
			statsPanel.setBackground(Color.WHITE);
			statsPanel.setLayout(null); // Allows for free placement of components in this panel
			statsPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED)); // Adds a border to the panel

			infoPanel.setBounds(0, 635, 1024, 120); // Sets the size and position for the info panel
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

			characterStatistics.setFont(new Font("Serif", Font.PLAIN, 22));
			characterStatistics.setBounds(20, 10, 260, 40);
			
			characterLevel.setFont(new Font("Serif", Font.PLAIN, 20));
			characterLevel.setBounds(20, 43, 260, 40);

			characterHealth.setFont(new Font("Serif", Font.PLAIN, 16));
			characterHealth.setBounds(20, 80, 130, 40);
			
			characterHPRegen.setFont(new Font("Serif", Font.PLAIN, 16));
			characterHPRegen.setBounds(170, 80, 130, 40);
			
			characterMana.setFont(new Font("Serif", Font.PLAIN, 16));
			characterMana.setBounds(20, 120, 130, 40);
			
			characterManaRegen.setFont(new Font("Serif", Font.PLAIN, 16));
			characterManaRegen.setBounds(170, 120, 130, 40);
					
			characterHealthPots.setFont(new Font("Serif", Font.PLAIN, 16));
			characterHealthPots.setBounds(20, 160, 130, 40);
			
			characterManaPots.setFont(new Font("Serif", Font.PLAIN, 16));
			characterManaPots.setBounds(170, 160, 130, 40);
			
			characterDamage.setFont(new Font("Serif", Font.PLAIN, 16));
			characterDamage.setBounds(20, 200, 130, 40);

			characterArmor.setFont(new Font("Serif", Font.PLAIN, 16));
			characterArmor.setBounds(170, 200, 130, 40);
			
			characterCritChance.setFont(new Font("Serif", Font.PLAIN, 16));
			characterCritChance.setBounds(20, 240, 300, 40);
			
			characterCritDamage.setFont(new Font("Serif", Font.PLAIN, 16));
			characterCritDamage.setBounds(20, 280, 300, 40);

			characterXP.setFont(new Font("Serif", Font.PLAIN, 16));
			characterXP.setBounds(20, 360, 130, 40);
			
			characterDodge.setFont(new Font("Serif", Font.PLAIN, 16));
			characterDodge.setBounds(20, 320, 130, 40);
			
			equipmentWindow.setFont(new Font("Serif", Font.PLAIN, 15));
			equipmentWindow.setBounds(13, 400, 270, 320);
			equipmentWindow.setBackground(Color.WHITE);
			equipmentWindow.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

			
			equipmentWindow.addTab("Equipment", null, scrollEquipment, "This is your equipped items.");
			equipmentWindow.addTab("Inventory", null, scrollInventory, "This is your inventory.");
			
			equipmentBox.setFont(new Font("Serif", Font.PLAIN, 15));
			inventoryBox.setFont(new Font("Serif", Font.PLAIN, 15));
		/**
		 * SCROLL AREA PREFERENCES
		 */
		infoBox.setEditable(false); // Makes the info box uneditable
			infoBox.setBounds(0, 0, 800, 100);
			scroll.setBounds(0, 0, 800, 100); // Sets the size and position of the info box
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
			sendButton.setBounds(875, 60, 74, 25); // Sets the size and position of the send button
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
			commandBox.setBounds(800, 20, 224, 25); // Sets the size and position of the command box
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
		statsPanel.add(characterCritChance);
		statsPanel.add(characterCritDamage);
		statsPanel.add(characterDodge);
		if(screenHeight > 820){
			statsPanel.add(equipmentWindow);
		}

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
		characterHealth.setText("Health: " + decimals.format((int)DrawGame.character.getCurrHP()) + " / " + decimals.format((int)DrawGame.character.getMaxHP()));
		characterDamage.setText("Damage: " + decimals.format(DrawGame.character.getDamage()));
		characterArmor.setText("Armor: " + decimals.format(DrawGame.character.getArmor()));
		characterLevel.setText("Level: " + decimals.format(DrawGame.character.getLevel()));
		characterXP.setText("Experience: " + decimals.format(DrawGame.character.getXP()) + "/" + decimals.format(DrawGame.character.getMaxXP()));
		characterMana.setText("Mana: " + decimals.format((int)DrawGame.character.getMana()) + "/" + decimals.format((int)DrawGame.character.getMaxMana()));
		characterHPRegen.setText("HP/s: " + oneDigit.format(DrawGame.character.getHPRegen()));
		characterManaRegen.setText("Mana/s: " + oneDigit.format(DrawGame.character.getManaRegen()));
		characterHealthPots.setText("Health potions: " + decimals.format(DrawGame.character.getHPPots()));
		characterManaPots.setText("Mana potions: " + decimals.format(DrawGame.character.getManaPots()));
		characterCritChance.setText("Critical hit chance: " + decimals.format(DrawGame.character.gloves().getBonusStat()) + "%");
		characterCritDamage.setText("Critical hit damage: " + decimals.format(DrawGame.character.helm().getBonusStat()) + "%"); 
		characterDodge.setText("Dodge chance: " + decimals.format(DrawGame.character.boots().getBonusStat()) + "%");
		
		equipmentBox.setText("");
		DrawGame.character.printEquipment();
		inventoryBox.setText("");
		DrawGame.character.printInventory();

		draw.repaint();
	}

	public void parseCommand(String command){
		String lineArray[] = command.split("\\s+");
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
			infoBox.setCaretPosition(infoBox.getDocument().getLength() - 150);
		}
		else if((lineArray[0].trim().equals("stats") && lineArray.length == 1) || (lineArray[0].trim().equals("Stats") && lineArray.length == 1)){
			infoBox.append(DrawGame.character.getStatMessage());
			infoBox.setCaretPosition(infoBox.getDocument().getLength() - 420);
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
		else if((lineArray[0].trim().equals("setname") || lineArray[0].trim().equals("Setname")) && lineArray.length > 4){
			infoBox.append(Engine.setNameError);
			infoBox.setCaretPosition(infoBox.getDocument().getLength());
		}
		else if(lineArray[0].trim().equals("equipment") || lineArray[0].trim().equals("Equipment")){
			DrawGame.character.printEquipment();
		}
		else if((lineArray[0].trim().equals("clear") || (lineArray[0].trim().equals("Clear")) && (lineArray[1].trim().equals("inventory") || lineArray[1].trim().equals("Inventory"))) && lineArray.length == 2){
			DrawGame.character.clearInventory();
		}
		else if((lineArray[0].trim().equals("remove") || lineArray[0].trim().equals("Remove")) && lineArray[1].trim().matches("\\d+") && lineArray.length == 2){
			   DrawGame.character.removeItemFromInventory(Integer.parseInt(lineArray[1]));
		}
		else{
			infoBox.append(Engine.noSuchCommandMessage);
			infoBox.setCaretPosition(infoBox.getDocument().getLength());
		}
		Engine.writingSound = true;
	}

	public void saveGame(){
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream("savegame.txt"), "utf-8"));
			writer.println(DrawGame.character.getName());
			writer.println("Level: ");
			writer.println(DrawGame.character.getLevel());
			writer.println("Current Health: ");
			writer.println(DrawGame.character.getCurrHP());
			writer.println("Current Mana: ");
			writer.println(DrawGame.character.getCurrHP());
			writer.println("Experience: ");
			writer.println(DrawGame.character.getXP());
			writer.println("Health potions:");
			writer.println(DrawGame.character.getHPPots());
			writer.println("Mana potions:");
			writer.println(DrawGame.character.getManaPots());
			writer.println("Bartuc kills:");
			writer.println(DrawGame.character.getBartucKills());
			writer.println("Total minion kills:");
			writer.println(DrawGame.character.getTotalKills());
			writer.println("Sword kills:");
			writer.println(DrawGame.character.getSwordKills());
			writer.println("Magic kills:");
			writer.println(DrawGame.character.getMagicKills());
			writer.println("Total damage dealt:");
			writer.println(DrawGame.character.getDamageDealt());
			writer.println("Sword damage dealth:");
			writer.println(DrawGame.character.getSwordDamageDealt());
			writer.println("Magic damage dealt:");
			writer.println(DrawGame.character.getMagicDamageDealt());
			writer.println("Fire balls casted:");
			writer.println(DrawGame.character.getFireBalls());
			writer.println("Fire balls missed:");
			writer.println(DrawGame.character.getMissedFireBalls());
			writer.println("Damage taken by minions:");
			writer.println(DrawGame.character.getDamageTaken());
			writer.println("Damage taken by bartuc:");
			writer.println(DrawGame.character.getDamageTakenBartuc());
			writer.println("Health potions used:");
			writer.println(DrawGame.character.getHealthPotsUsed());
			writer.println("Mana potions used:");
			writer.println(DrawGame.character.getManaPotsUsed());
			writer.println("Armor found:");
			writer.println(DrawGame.character.getGearFound());
			writer.println("Epic armor found:");
			writer.println(DrawGame.character.getEpicGearFound());
			writer.println("Swords found:");
			writer.println(DrawGame.character.getSwordsFound());
			writer.println("Epic swords found:");
			writer.println(DrawGame.character.getEpicSwordsFound());
			writer.println("Shields found:");
			writer.println(DrawGame.character.getShieldsFound());
			writer.println("Epic shields found:");
			writer.println(DrawGame.character.getEpicShieldsFound());
			writer.println("Steps taken:");
			writer.println(DrawGame.character.getStepsTaken());
			writer.println("Zone changes:");
			writer.println(DrawGame.character.getZoneChanges());
			writer.println("Times in cave:");
			writer.println(DrawGame.character.getTimesInCave());
			writer.println(DrawGame.character.chest().type + " " +DrawGame.character.chest().armor + " " + DrawGame.character.chest().bonusHealth +  " " + DrawGame.character.chest().hpRegen);
			writer.println(DrawGame.character.pants().type + " " + DrawGame.character.pants().armor +  " " +DrawGame.character.pants().bonusHealth +  " " + DrawGame.character.pants().hpRegen);
			writer.println(DrawGame.character.shoulders().type +  " " +DrawGame.character.shoulders().armor +  " " +DrawGame.character.shoulders().bonusMana +  " " +DrawGame.character.shoulders().manaRegen);
			writer.println(DrawGame.character.gloves().type + " " + DrawGame.character.gloves().armor +  " " +DrawGame.character.gloves().critChance);
			writer.println(DrawGame.character.helm().type +  " " +DrawGame.character.helm().armor +  " " +DrawGame.character.helm().critDamage);
			writer.println(DrawGame.character.boots().type +  " " +DrawGame.character.boots().armor + " " + DrawGame.character.boots().dodge);
			writer.println(DrawGame.character.shield().type +  " " +DrawGame.character.chest().armor);
			writer.println(DrawGame.character.sword().type +  " " +DrawGame.character.sword().damage);

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
			ArrayList<Item> loadItemList = new ArrayList<Item>();
			String line = in.readLine();
			DrawGame.character.setName(line);
			while ((line = in.readLine()) != null){
	
				if (!line.matches("^[^\\d].*")) {
					loadList.add(Double.parseDouble(line));
				}
				if(line.startsWith("chest")){
					String[] linesplit = line.split("\\s");
					Item newItem = new Item(1, false, "chest");
					newItem.setArmor(Integer.parseInt(linesplit[1]));
					newItem.setBonusHealth(Integer.parseInt(linesplit[2]));
					newItem.setHPregen(Double.parseDouble(linesplit[3]));
					loadItemList.add(newItem);
				}
				if(line.startsWith("helm")){
					String[] linesplit = line.split("\\s");
					Item newItem = new Item(1, false, "helm");
					newItem.setArmor(Integer.parseInt(linesplit[1]));
					newItem.setCritDmg(Integer.parseInt(linesplit[2]));
					loadItemList.add(newItem);
				}
				if(line.startsWith("boots")){
					String[] linesplit = line.split("\\s");
					Item newItem = new Item(1, false, "boots");
					newItem.setArmor(Integer.parseInt(linesplit[1]));
					newItem.setDodge(Integer.parseInt(linesplit[2]));
					loadItemList.add(newItem);
				}
				if(line.startsWith("shield")){
					String[] linesplit = line.split("\\s");
					Item newItem = new Item(1, false, "shield");
					newItem.setArmor(Integer.parseInt(linesplit[1]));
					loadItemList.add(newItem);
				}
				if(line.startsWith("pants")){
					String[] linesplit = line.split("\\s");
					Item newItem = new Item(1, false, "pants");
					newItem.setArmor(Integer.parseInt(linesplit[1]));
					newItem.setBonusHealth(Integer.parseInt(linesplit[2]));
					newItem.setHPregen(Double.parseDouble(linesplit[3]));
					loadItemList.add(newItem);
				}
				if(line.startsWith("shoulders")){
					String[] linesplit = line.split("\\s");
					Item newItem = new Item(1, false, "shoulders");
					newItem.setArmor(Integer.parseInt(linesplit[1]));
					newItem.setBonusMana(Integer.parseInt(linesplit[2]));
					newItem.setManaRegen(Double.parseDouble(linesplit[3]));
					loadItemList.add(newItem);
				}
				if(line.startsWith("gloves")){
					String[] linesplit = line.split("\\s");
					Item newItem = new Item(1, false, "gloves");
					newItem.setArmor(Integer.parseInt(linesplit[1]));
					newItem.setCritChance(Integer.parseInt(linesplit[2]));
					loadItemList.add(newItem);
				}
				if(line.startsWith("sword")){
					String[] linesplit = line.split("\\s");
					Item newItem = new Item(1, false, "sword");
					newItem.setDamage(Integer.parseInt(linesplit[1]));
					loadItemList.add(newItem);
				}
			}
			in.close();
			
			DrawGame.character.load(loadItemList.get(4), loadItemList.get(5), loadItemList.get(3), loadItemList.get(1), loadItemList.get(2), loadItemList.get(0), loadItemList.get(7), loadItemList.get(6), loadList.get(0).intValue(), loadList.get(1), loadList.get(2), loadList.get(4).intValue(), loadList.get(5).intValue(), loadList.get(3).intValue());
			DrawGame.character.setBartucKills(loadList.get(6).intValue());
			DrawGame.character.setTotalKills(loadList.get(7).intValue());
			DrawGame.character.setSwordKills(loadList.get(8).intValue());
			DrawGame.character.setMagicKills(loadList.get(9).intValue());
			DrawGame.character.setDamageDealt(loadList.get(10).intValue());
			DrawGame.character.setSwordDamageDealt(loadList.get(11).intValue());
			DrawGame.character.setMagicDamageDealt(loadList.get(12).intValue());
			DrawGame.character.setFireBalls(loadList.get(13).intValue());
			DrawGame.character.setMissedFireBalls(loadList.get(14).intValue());
			DrawGame.character.setDamageTaken(loadList.get(15).intValue());
			DrawGame.character.setDamageTakenBartuc(loadList.get(16).intValue());
			DrawGame.character.setHealthPotsUsed(loadList.get(17).intValue());
			DrawGame.character.setManaPotsUsed(loadList.get(18).intValue());
			DrawGame.character.setGearFound(loadList.get(19).intValue());
			DrawGame.character.setEpicGearFound(loadList.get(20).intValue());
			DrawGame.character.setSwordsFound(loadList.get(21).intValue());
			DrawGame.character.setEpicSwordsFound(loadList.get(22).intValue());
			DrawGame.character.setShieldsFound(loadList.get(23).intValue());
			DrawGame.character.setEpicShieldsFound(loadList.get(24).intValue());
			DrawGame.character.setStepsTaken(loadList.get(25).intValue());
			DrawGame.character.setZoneChanges(loadList.get(26).intValue());
			DrawGame.character.setTimesInCave(loadList.get(27).intValue());

			DrawGame.character.setRoom(Engine.centralZone);
			DrawGame.bartuc.spawn(DrawGame.character.getBartucKills());
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
