package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {
	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[][];
	
	//CREATING A CONSTRUCTOR FOR THIS TILEMANAGER
	public TileManager(GamePanel gp) {
		this.gp=gp;
		
		tile= new Tile[50];
		mapTileNum=new int[gp.maxWorldColumn][gp.maxWorldRow];
		getTileImage();
		loadMap("/maps/worldV2.txt");
	}

	public void loadMap(String filePath) {
		try {
			InputStream is=getClass().getResourceAsStream(filePath);
			BufferedReader br= new BufferedReader(new InputStreamReader(is));
			
			int column= 0;
			int row=0;
			while(column < gp.maxWorldColumn && row < gp.maxWorldRow) {
				String line=br.readLine();
				while(column < gp.maxWorldColumn) {
					String numbers[]=line.split(" ");
					int num=Integer.parseInt(numbers[column]);
					
					mapTileNum[column][row]=num;
					column++;
				}
				if(column == gp.maxWorldColumn) {
					column=0; //RESETING THE COLUMN 
					row++;
				}
			}
			br.close();
			
		}catch(Exception e) {
			
		}
		
	}
	public void getTileImage() {
		//INSANTIATEING TILE BY SCALING	
		
		//PLACEHOLDER, WE DONT USE THE FIRST 0-9 INDEX
		//WE CANT KEEP IT EMPTY EITHER OR IT WILL GIVE NULL SO WE USE IT LIKE THIS
			setup(0, "grass00", false);
			setup(1, "grass00", false);
			setup(2, "grass00", false);
			setup(3, "grass00", false);
			setup(4, "grass00", false);
			setup(5, "grass00", false);
			setup(6, "grass00", false);
			setup(7, "grass00", false);
			setup(8, "grass00", false);
			setup(9, "grass00", false);
			
			//now actual tile index
			setup(10, "grass00", false);
			setup(11, "grass01", false);
			setup(12, "water00", true);
			setup(13, "water001", true);
			setup(14, "water01", true);
			setup(15, "water02", true);
			setup(16, "water03", true);
			setup(17, "water04", true);
			setup(18, "water06", true);
			setup(19, "water07", true);
			setup(20, "water08", true);
			setup(21, "water09", true);
			setup(22, "water10", true);
			setup(23, "water11", true);
			setup(24, "water12", true);
			setup(25, "water13", true);
			
			setup(26, "road00", false);
			setup(27, "road01", false);
			setup(28, "road02", false);
			setup(29, "road03", false);
			setup(30, "road04", false);
			setup(31, "road05", false);
			setup(32, "road06", false);
			setup(33, "road07", false);
			setup(34, "road08", false);
			setup(35, "road09", false);
			setup(36, "road10", false);
			setup(37, "road11", false);
			setup(38, "road12", false);
			setup(39, "earth", false);
			setup(40, "wall", true);
			setup(41, "tree", true);
				
	}
	public void setup(int index, String imageName,boolean collison) {
		UtilityTool uTool= new UtilityTool();
		try {
			//WE WILL HANDLE ALL DUPLICATE INSTANTIATE, SCALE IMAGE, LINES HERE
			tile[index]=new Tile();
			tile[index].image= ImageIO.read(getClass().getResourceAsStream("/tiles/"+imageName+".png"));
			tile[index].image=uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
			tile[index].collison= collison;
			
		}catch(IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void draw(Graphics2D g2) {
		
		int worldColumn= 0;
		int worldRow=0;
		
		while(worldColumn < gp.maxWorldColumn && worldRow < gp.maxWorldRow) {
			int tileNum=mapTileNum[worldColumn][worldRow];
			
			int worldX= worldColumn * gp.tileSize; //WORLD POSITION OF PLAYER
			int worldY= worldRow * gp.tileSize;
			int screenX=worldX - gp.player.worldX + gp.player.screenX; //SCREEN POSITION FROM PLAYER
			int screenY= worldY - gp.player.worldY + gp.player.screenY;
			
			if(worldX+gp.tileSize> gp.player.worldX-gp.player.screenX &&  //THIS LOOP IS FOR TO SEE THE PART 
				worldX-gp.tileSize < gp.player.worldX+gp.player.screenX && //WE WANT TO SEE, NOT THE
				worldY+gp.tileSize> gp.player.worldY-gp.player.screenY && // ACTUAL MAP                                                                                                                 
				worldY-gp.tileSize < gp.player.worldY+gp.player.screenY) {
				g2.drawImage(tile[tileNum].image, screenX, screenY, null);
			}
			 
			
			worldColumn++;
			
		
			if(worldColumn == gp.maxWorldColumn) {
				worldColumn=0; //RESETING THE COLUMN 
				worldRow++;
				
			}
			
		}
	}	
	
}
