import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Room {
	private int[][] map;
	private int[][] map2;
	private BufferedImage tileSheet;

	public Room(int[][] existingMap, int[][] existingMap2) {
		map = new int[existingMap.length][existingMap[0].length];
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[y].length; x++) {
				map[y][x] = existingMap[y][x];
			}
		}
		map2 = new int[existingMap2.length][existingMap2[0].length];
		for (int y = 0; y < map2.length; y++) {
			for (int x = 0; x < map2[y].length; x++) {
				map2[y][x] = existingMap2[y][x];
			}
		}
		tileSheet = LoadTileSheet("tileset.png");

	}

	public BufferedImage LoadTileSheet(String fileName) {
		BufferedImage img = null;

		try {
			img = ImageIO.read(new File(fileName));
		} catch (IOException e1) {
			// e1.printStackTrace();
			throw new IllegalArgumentException("Could not load the file.");
		}
		
		return img;
	}
	
	public void drawImage(Graphics g) {
		   for(int y = 0; y < map.length; y++){
			   for(int x = 0; x < map[y].length; x++){
				   int index = map[y][x];
				   int yOffset = 0;
				   while(index > (tileSheet.getWidth() / Engine.TILE_WIDTH) - 1){
					   index = index - (tileSheet.getWidth() / Engine.TILE_WIDTH);
					   yOffset++; 
				   }
				   
				   g.drawImage(tileSheet,
						   x * Engine.TILE_WIDTH,
						   y * Engine.TILE_HEIGHT,
						   (x * Engine.TILE_WIDTH) + Engine.TILE_WIDTH,
						   (y * Engine.TILE_HEIGHT) + Engine.TILE_HEIGHT,
						   index * Engine.TILE_WIDTH,
						   yOffset * Engine.TILE_HEIGHT,
						   (index * Engine.TILE_WIDTH) + Engine.TILE_WIDTH,
						   (yOffset * Engine.TILE_HEIGHT) + Engine.TILE_HEIGHT,
						   null);
			   }
		   }
		   
		   for(int y = 0; y < map2.length; y++){
			   for(int x = 0; x < map2[y].length; x++){
				   int index = map2[y][x];
				   int yOffset = 0;
				   while(index > (tileSheet.getWidth() / Engine.TILE_WIDTH) - 1){
					   index = index - (tileSheet.getWidth() / Engine.TILE_WIDTH);
					   yOffset++; 
				   }
				   
				   g.drawImage(tileSheet,
						   x * Engine.TILE_WIDTH,
						   y * Engine.TILE_HEIGHT,
						   (x * Engine.TILE_WIDTH) + Engine.TILE_WIDTH,
						   (y * Engine.TILE_HEIGHT) + Engine.TILE_HEIGHT,
						   index * Engine.TILE_WIDTH,
						   yOffset * Engine.TILE_HEIGHT,
						   (index * Engine.TILE_WIDTH) + Engine.TILE_WIDTH,
						   (yOffset * Engine.TILE_HEIGHT) + Engine.TILE_HEIGHT,
						   null);
			   }
		   }
		}
}
