package main;

public class EventHandler {
	
	GamePanel gp;
	EventRect eventRect[][];
	int previousEventX, previousEventY;
	boolean canTouchEvent=true;
	
	public EventHandler(GamePanel gp) {
		this.gp=gp;
		eventRect=new EventRect[gp.maxWorldColumn][gp.maxWorldRow]; //this provides rectangle on every single tile on the map.
		
		int col=0;
		int row=0;
		while(col<gp.maxWorldColumn && row<gp.maxWorldRow) {
			eventRect[col][row]=new EventRect();           //this coordinates are for solid area.
			eventRect[col][row].x=23;						//means for collision.
			eventRect[col][row].y=23;
			eventRect[col][row].width=2;
			eventRect[col][row].height=2;
			eventRect[col][row].eventRectDefaultX=eventRect[col][row].x;
			eventRect[col][row].eventRectDefaultY=eventRect[col][row].y;
			
			col++;
			if(col == gp.maxWorldColumn) {
				col=0;
				row++;
			}
		}			
	}
	public void checkEvent() {
		
		//check if player character is more than 1 tile away from event
		int xDistance = Math.abs(gp.player.worldX - previousEventX); //here prevousX and Y is the coords of the tile,
		int yDistance = Math.abs(gp.player.worldY - previousEventY);  // player hit when he fell in damage pit..
		int distance= Math.max(xDistance, yDistance);	//and we are checking if "distance" is greater than 16x16 tiles,
		if(distance> gp.tileSize) {						//if it is then player has moved 1 tile.
			canTouchEvent=true;
		}
		
		if(canTouchEvent == true) {
		if(hit(27,16,"any")==true) {damagePit(27, 16, gp.dialougeState);}
		if(hit(23,12,"up")==true){healingPool(23, 12, gp.dialougeState);}
		
		}
	}
	public void damagePit(int col, int row, int gameState) {
		gp.gameState=gameState;
		gp.playSE(6);
		gp.ui.currentDialouge="You've fallen into a pit";
		gp.player.life -=1;
		//eventRect[col][row].eventDone=true;
		canTouchEvent=false;
		
	}
	public void healingPool(int col, int row, int gameState) {
		if(gp.keyH.enterPressed == true) {
			gp.gameState=gameState;
			gp.player.attackCanceled=true;
			gp.playSE(2);
			gp.ui.currentDialouge="You drank from the healing pool.\nYour life has been recovered";
			gp.player.life=gp.player.maxLife;		
			gp.asset.setMonster();
		}		
	}
	public boolean hit(int col, int row, String reqDirection) {
		//THIS HIT() CHECKS EVENT COLLISON
		
		boolean hit=false;
		//getting players current solid position
		gp.player.solidArea.x=gp.player.worldX+gp.player.solidArea.x;
		gp.player.solidArea.y=gp.player.worldY+gp.player.solidArea.y;
		
		//eventRect's current solid position
		eventRect[col][row].x=col*gp.tileSize+eventRect[col][row].x;
		eventRect[col][row].y=row*gp.tileSize+eventRect[col][row].y;
		
		//checking if player solid area is colliding with eventRect solid area
		if(gp.player.solidArea.intersects(eventRect[col][row]) && eventRect[col][row].eventDone==false) {
			if(gp.player.direction.contentEquals(reqDirection)|| reqDirection.contentEquals("any")) {
				hit=true;
				
			previousEventX=gp.player.worldX;
			previousEventY=gp.player.worldY;
			}			
		}
		//reset the solidArea x and y after collision
		gp.player.solidArea.x=gp.player.solidAreaDefaultX;
		gp.player.solidArea.y=gp.player.solidAreaDefaultY;
		eventRect[col][row].x=eventRect[col][row].eventRectDefaultX;
		eventRect[col][row].y=eventRect[col][row].eventRectDefaultY;
		
		return hit;
	}
}
