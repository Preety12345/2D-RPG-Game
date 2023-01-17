package main;

import entity.Entity;

public class CollisonChecker {
	GamePanel gp;

	public CollisonChecker(GamePanel gp) {
		this.gp=gp;
	}
	
	public void checkTile(Entity entity) { //THIS METHOD IS FOR CHECKING TILE COLLISION
		
		//DETECT COLLISON COORDINATES
		int entityLeftWorldX= entity.worldX+ entity.solidArea.x; //finding the coords of rectangle/collion position
		int entityRightWorldX= entity.worldX+ entity.solidArea.x+entity.solidArea.width;
		int entityTopWorldY= entity.worldY+ entity.solidArea.y;
		int entityBottomWorldY= entity.worldY+ entity.solidArea.y+entity.solidArea.height;
		
		//BASED ON THE COORDINATES WE ARE FINDING THE COLUM AND ROW OF THE COLLISON OBJECT
		int entityLeftColumn= entityLeftWorldX/gp.tileSize;
		int entityRightColumn= entityRightWorldX/gp.tileSize;
		int entityTopRow= entityTopWorldY/gp.tileSize;
		int entityBottomRow= entityBottomWorldY/gp.tileSize;
		
		
		int tileNum1, tileNum2;
		switch(entity.direction) {
		
		case "up":
			entityTopRow= (entityTopWorldY- entity.speed)/gp.tileSize; //TRYING TO FIND WHERE PLAYER WILL GO TO DETECT COLLISON
			tileNum1=gp.tileM.mapTileNum[entityLeftColumn][entityTopRow];
			tileNum2=gp.tileM.mapTileNum[entityRightColumn][entityTopRow];
			if(gp.tileM.tile[tileNum1].collison == true || gp.tileM.tile[tileNum2].collison == true) {
				//TILENUM1 AND 2 ARE INDEX
				//CHECKING IF THESE TWO INDEXES ARE SOLID OR NOT FOR COLLISON
				entity.collisonOn=true;	
			}
			break;
		case "down":
			entityBottomRow= (entityBottomWorldY + entity.speed)/gp.tileSize; //TRYING TO FIND WHERE PLAYER WILL GO TO DETECT COLLISON
			tileNum1=gp.tileM.mapTileNum[entityLeftColumn][entityBottomRow];
			tileNum2=gp.tileM.mapTileNum[entityRightColumn][entityBottomRow];
			if(gp.tileM.tile[tileNum1].collison == true || gp.tileM.tile[tileNum2].collison == true) {
				//TILENUM1 AND 2 ARE INDEX
				//CHECKING IF THESE TWO INDEXES ARE SOLID OR NOT FOR COLLISON
				entity.collisonOn=true;	
			}
			break;
		case "left":
			entityLeftColumn= (entityLeftWorldX - entity.speed)/gp.tileSize; //TRYING TO FIND WHERE PLAYER WILL GO TO DETECT COLLISON
			tileNum1=gp.tileM.mapTileNum[entityLeftColumn][entityTopRow];
			tileNum2=gp.tileM.mapTileNum[entityLeftColumn][entityBottomRow];
			if(gp.tileM.tile[tileNum1].collison == true || gp.tileM.tile[tileNum2].collison == true) {
				//TILENUM1 AND 2 ARE INDEX
				//CHECKING IF THESE TWO INDEXES ARE SOLID OR NOT FOR COLLISON
				entity.collisonOn=true;
			}
			break;
		case "right":
			entityRightColumn= (entityRightWorldX + entity.speed)/gp.tileSize; //TRYING TO FIND WHERE PLAYER WILL GO TO DETECT COLLISON
			tileNum1=gp.tileM.mapTileNum[entityRightColumn][entityTopRow];
			tileNum2=gp.tileM.mapTileNum[entityRightColumn][entityBottomRow];
			if(gp.tileM.tile[tileNum1].collison == true || gp.tileM.tile[tileNum2].collison == true) {
				//TILENUM1 AND 2 ARE INDEX
				//CHECKING IF THESE TWO INDEXES ARE SOLID OR NOT FOR COLLISON
				entity.collisonOn=true;	
			}
			break;
			
		}
	}
	
	public int checkObject(Entity entity, boolean player) {
		//IN THIS METHOD WE CHECK IF PLAYER IS HITTING ANY OBJECT, IF HE IS THEN WE RETURN THE INDEX OF THE OBJECT
		
		int index=999;
		
		for(int i=0; i <gp.obj.length; i++) {
			if(gp.obj[i] != null) {
				//GET ENTITY'S SOLID AREA POSITION
				entity.solidArea.x=entity.worldX+entity.solidArea.x;
				entity.solidArea.y=entity.worldY+entity.solidArea.y;
				
				//GET OBJECTS SOLID AREA POSISTION
				gp.obj[i].solidArea.x=gp.obj[i].worldX+gp.obj[i].solidArea.x;
				gp.obj[i].solidArea.y=gp.obj[i].worldY+gp.obj[i].solidArea.y;
				
				switch(entity.direction) {
				//CHECK WHERE THE ENTITY WILL BE AFTER IT MOVED
				//CHECKING IF ENTITY AND OBJECT IS COLLIDING OR NOT
				case "up":	entity.solidArea.y -= entity.speed;	break;
				case "down": entity.solidArea.y += entity.speed; break;
				case "left": entity.solidArea.x -= entity.speed; break;
				case "right": entity.solidArea.x += entity.speed; break;
			}
				if(entity.solidArea.intersects(gp.obj[i].solidArea)) {
					if(gp.obj[i].collison == true) {
						entity.collisonOn = true;
					}
					//checking if its player or not
					if(player == true) {
						index=i;
					}								
			}
				entity.solidArea.x=entity.solidAreaDefaultX; //RESETING X AND Y TO 0
				entity.solidArea.y=entity.solidAreaDefaultY;
				gp.obj[i].solidArea.x=gp.obj[i].solidAreaDefaultX;
				gp.obj[i].solidArea.y=gp.obj[i].solidAreaDefaultY;
		}
	}
		return index;
	}
	
	//to check collison of entity or monster, CHECKING IF PLAYER IS HITTING NPC OR MONSTER
	public int checkEntity(Entity entity, Entity[] target) {
		
		int index=999;
		
		for(int i=0; i <target.length; i++) {
			if(target[i] != null) {
				//GET ENTITY'S SOLID AREA POSITION
				entity.solidArea.x=entity.worldX+entity.solidArea.x;
				entity.solidArea.y=entity.worldY+entity.solidArea.y;
				
				//GET OBJECTS SOLID AREA POSISTION
				target[i].solidArea.x=target[i].worldX+target[i].solidArea.x;
				target[i].solidArea.y=target[i].worldY+target[i].solidArea.y;
				
				switch(entity.direction) {
				//CHECK WHERE THE ENTITY WILL BE AFTER IT MOVED
				//CHECKING IF ENTITY AND OBJECT IS COLLIDING OR NOT
				case "up": entity.solidArea.y -= entity.speed;	break;
				case "down": entity.solidArea.y += entity.speed; break;
				case "left": entity.solidArea.x -= entity.speed; break;
				case "right": entity.solidArea.x += entity.speed; break;				
				}			
				if(entity.solidArea.intersects(target[i].solidArea)) {		
					if(target[i]!=entity) {
						entity.collisonOn = true;																	
						index = i;
					}										
				}												
				entity.solidArea.x=entity.solidAreaDefaultX; //RESETING X AND Y TO 0
				entity.solidArea.y=entity.solidAreaDefaultY;
				target[i].solidArea.x=target[i].solidAreaDefaultX;
				target[i].solidArea.y=target[i].solidAreaDefaultY;
		}		
	}
	return index;
}
	public boolean checkPlayer(Entity entity) {  //IN THIS METHOD WE ARE CHECKING IF NPC/MONSTER IS HITTING PLAYER OR NOT
		
		boolean contactPlayer=false;
		//GET ENTITY'S SOLID AREA POSITION
		entity.solidArea.x=entity.worldX+entity.solidArea.x;
		entity.solidArea.y=entity.worldY+entity.solidArea.y;
		
		//GET OBJECTS SOLID AREA POSISTION
		gp.player.solidArea.x=gp.player.worldX+gp.player.solidArea.x;
		gp.player.solidArea.y=gp.player.worldY+gp.player.solidArea.y;
		
		switch(entity.direction) {
		//CHECK WHERE THE ENTITY WILL BE AFTER IT MOVED
		//CHECKING IF ENTITY AND OBJECT IS COLLIDING OR NOT
		//checking npc collision
		case "up":entity.solidArea.y -= entity.speed;	break;
		case "down":entity.solidArea.y += entity.speed;	break;			
		case "left":entity.solidArea.x -= entity.speed;	break;			
		case "right":entity.solidArea.x += entity.speed;	break;		
	}
		if(entity.solidArea.intersects(gp.player.solidArea)) {				
			entity.collisonOn = true;	
			contactPlayer=true;
	}
		entity.solidArea.x=entity.solidAreaDefaultX; //RESETING X AND Y TO 0
		entity.solidArea.y=entity.solidAreaDefaultY;
		gp.player.solidArea.x=gp.player.solidAreaDefaultX;
		gp.player.solidArea.y=gp.player.solidAreaDefaultY;
		
		return contactPlayer;
	}
}
