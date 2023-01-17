package main;

import entity.NPC_OldMan;
import monster.MON_Greenslime;
import object.OBJ_Axe;
import object.OBJ_Door;
import object.OBJ_Key;
import object.OBJ_Shield_Blue;

public class Asset{

	//INSTANTIATE OBJECTS AND DISPLAY ON THE MAP
	GamePanel gp;
	
	public Asset(GamePanel gp) {
		this.gp=gp;
	}
	
	public void objectSetter() {
		int i=0;
		gp.obj[i]=new OBJ_Key(gp);
		gp.obj[i].worldX=gp.tileSize*25;
		gp.obj[i].worldY=gp.tileSize*23;
		i++;
		gp.obj[i]=new OBJ_Key(gp);
		gp.obj[i].worldX=gp.tileSize*21;
		gp.obj[i].worldY=gp.tileSize*19;
		i++;
		gp.obj[i]=new OBJ_Axe(gp);
		gp.obj[i].worldX=gp.tileSize*33;
		gp.obj[i].worldY=gp.tileSize*21;
		i++;
		gp.obj[i]=new OBJ_Shield_Blue(gp);
		gp.obj[i].worldX=gp.tileSize*35;
		gp.obj[i].worldY=gp.tileSize*21;
		i++;
	}
	public void setNPC() {
		gp.npc[0]=new NPC_OldMan(gp);
		gp.npc[0].worldX=gp.tileSize*21;
		gp.npc[0].worldY=gp.tileSize*21;		

	}
	public void setMonster() {
		int i=0;
		gp.monster[i]=new MON_Greenslime(gp);
		gp.monster[i].worldX=gp.tileSize*23;
		gp.monster[i].worldY=gp.tileSize*36;
		i++;
		gp.monster[i]=new MON_Greenslime(gp);
		gp.monster[i].worldX=gp.tileSize*23;
		gp.monster[i].worldY=gp.tileSize*33;
		i++;
		gp.monster[i]=new MON_Greenslime(gp);
		gp.monster[i].worldX=gp.tileSize*22;
		gp.monster[i].worldY=gp.tileSize*31;
		i++;
		gp.monster[i]=new MON_Greenslime(gp);
		gp.monster[i].worldX=gp.tileSize*22;
		gp.monster[i].worldY=gp.tileSize*31;
		i++;
		gp.monster[i]=new MON_Greenslime(gp);
		gp.monster[i].worldX=gp.tileSize*22;
		gp.monster[i].worldY=gp.tileSize*34;
		i++;
		
	}
}
