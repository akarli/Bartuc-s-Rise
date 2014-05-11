import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;

public class RoomOverlay {
	private int[][] map3;
	private BufferedImage tileSheet;

	public RoomOverlay(int[][] existingMap3) {

		map3 = new int[existingMap3.length][existingMap3[0].length];
		for (int y = 0; y < map3.length; y++) {
			for (int x = 0; x < map3[y].length; x++) {
				map3[y][x] = existingMap3[y][x];
			}
		}
		tileSheet = LoadTileSheet("Graphics\\tileset.png");

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

		for(int y = 0; y < map3.length; y++){
			for(int x = 0; x < map3[y].length; x++){
				int index = map3[y][x]-1;
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
