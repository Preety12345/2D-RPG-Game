package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
	
	//GAME PANEL WILL WORK AS GAME SCREEN	
	//SCREEN SETTINGS
	final int originalTileSize=16;  //16x16 Tile size
	final int scale=3;
	public final int tileSize=originalTileSize * scale; //48x48 tile size
	
	public final int maxScreenColumn=16;  //How the screen pixel size is horizentally and vericaly
	public final int maxScreenRow=12;
	public final int screenWidth= tileSize * maxScreenColumn;  //768 pixel
	public final int screenHeight= tileSize * maxScreenRow;    //576 pixel
	
	//WORLD MAP PARAMETERS
	public final int maxWorldColumn=50;
	public final int maxWorldRow=50;
	
	//FPS
	int FPS=60;
	
	TileManager tileM= new TileManager(this); //INSTANSIATE TILEMANAGER CLASS
	public Keyhandler keyH=new Keyhandler(this); //INSATNSTIATE KEYHANDLER CLASS
	Thread gameThread;
	public CollisonChecker cCheker=new CollisonChecker(this); //INSANTIATE COLLISONCHECKER CLASS
	public UI ui=new UI(this); 					//INSTANTIATE UI CLASS
	public EventHandler eHandler= new EventHandler(this); //INSTANTIATE EVENTHANDLER CLASS
	
	//ENTITY AND OBJECT
	public Player player= new Player(this, keyH); //INSATNSTIATE PLAYER CLASS
	public Entity obj[]= new Entity[10]; 		//MAKE OBJECT ARRAY
	public Entity npc[]=new Entity[10];			 //MAKE NPC ARRAY
	public Entity monster[]=new Entity[20]; 	//MAKE MOSNTER ARRAY
	public Asset asset= new Asset(this);  		//INSTANTIATE ASSET CLASS 
	
	ArrayList<Entity> entityList=new ArrayList<>();  //CREATING AN ARRAYLIST OF ENTITY
	
	Sound music=new Sound(); //INSTANSIATE SOUND CLASS
	Sound se=new Sound();
	
	//GAME STATE
	public int gameState;
	public final int titleState=0;
	public final int playState=1;
	public final int pauseState=2;
	public final int dialougeState=3;
	public final int characterState=4;
			
	//Creating constructor of GamePanel
	public GamePanel() {
		
		this.setPreferredSize(new Dimension(screenWidth,screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}

	public void setupObject() {//CALLING THIS METHOD TO SET OBJECTS OR OTHER STUFF IN MAP
		asset.objectSetter();  
		asset.setNPC();
		asset.setMonster();
		playMusic(0);
		stopMusic();
		gameState=titleState;
	}
	
	public void startGame() {
		
		gameThread=new Thread(this); //this is passing the class "GamePanel" in this constructor
		gameThread.start(); //This is gonna automatically call run() method
	}
	
	//DELTA LOOP
	public void run() {
		//IN THIS METHOD WE WILL CREATE GAME LOOP
		double drawInterval=1000000000/FPS;
		double delta=0;
		long lastTime=System.nanoTime();
		long currentTime;
	
		while(gameThread !=null) {

//			//There are two things we will do in this loop
//			//UPDATE: update information such as character position
//			//DRAW: draw the screen with updated info
			currentTime= System.nanoTime();
			delta = delta + (currentTime - lastTime)/drawInterval; //how much time has passed
			lastTime=currentTime;
			if(delta>=1){
				update();
				repaint();  // This method is use to call paintComponent() method
				delta--;
			}
		}
	}
	
	public void update() {
		//Updates the player coordinates
		
		if(gameState==playState) {
			//player
			player.update();
			
			//NPC
			for(int i=0; i <npc.length; i++) {
				if(npc[i] != null) {
					npc[i].update();
				}
			}
			
			//MONSTER
			for(int i=0; i <monster.length; i++) {
				if(monster[i] != null) {
					if(monster[i].alive==true && monster[i].dying==false) {
						monster[i].update();
					}	
					if(monster[i].alive==false) {
						monster[i]=null;
					}
				}
			}
		}
		if(gameState==pauseState) {
			//nothing
		}
	}
	
	//To draw something we use this method
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		//DEBUG
		long drawStart=0;
		if(keyH.checkDrawTime==true) {
			drawStart=System.nanoTime();
		}		
		//TITLE SCREEN
		if(gameState == titleState) {
			ui.draw(g2);
		}
		else {
			
			//TO DRAW TILE
			tileM.draw(g2);
			
			//adding player, npc, object to the list
			entityList.add(player);
			
			for(int i=0; i<npc.length; i++) {
				if(npc[i]!=null) {
					entityList.add(npc[i]);
				}
			}
			for(int i=0; i<obj.length; i++) {
				if(obj[i]!=null) {
					entityList.add(obj[i]);
				}
			}
			for(int i=0; i<monster.length; i++) {
				if(monster[i]!=null) {
					entityList.add(monster[i]);
				}
			}
			//SORTING ENTITY ARRAYLIST
			Collections.sort(entityList, new Comparator<Entity>(){

				@Override
				public int compare(Entity e1, Entity e2) {
					int result=Integer.compare(e1.worldY, e2.worldY);
					return result;
				}
				
			});
			//DRAW ENTITIES
			for(int i=0; i<entityList.size(); i++) {
				entityList.get(i).draw(g2);
			}
			//EMPTY THE ENTITY ARRAYLIST
			entityList.clear();
			
			//TO DRAW UI
			ui.draw(g2);
		}	

		//DEBUG
				if(keyH.checkDrawTime==true) {
					long drawEnd=System.nanoTime();
					long passed=drawEnd-drawStart;
					g2.setColor(Color.white);
					g2.drawString("Draw Time="+passed, 10, 400);
					System.out.print("DrawTime="+passed);
				}
		g2.dispose();	
	}
	
	public void playMusic(int i) {
		music.setFile(i);
		music.play();
		music.loop();
	}
	
	public void stopMusic() {
		music.stop();
	}
	public void playSE(int i) {
		se.setFile(i);
		se.play();
	}
}
