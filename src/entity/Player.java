package entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.Keyhandler;
import main.UtilityTool;
import object.OBJ_Key;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

public class Player extends Entity{
	
	Keyhandler keyH;	
	public final int screenX; //ITS WHERE WE DRAW PLAYER IN THE SCREEN
	public final int screenY;
	public boolean attackCanceled=false;
	public ArrayList<Entity> inventory=new ArrayList<>();
	public final int maxInventorySize=20;
	
	//CREATING A PLAYER CONSTRUCTOR
	public Player(GamePanel gp, Keyhandler keyH) {
		super(gp); //THIS WAY WE INSTANTIATE ENTITY CLASS
		this.keyH=keyH;
		
		screenX=gp.screenWidth/2- (gp.tileSize/2); //THIS RETURNS THE HALFWAY POINT OF THE SCREEN
		screenY=gp.screenHeight/2- (gp.tileSize/2); //TO KEEP THE PLAYER AT CENTER OF MAP
		solidArea= new Rectangle(); //INSTANTIATE RECTANGLE CLASS
		solidArea.x=12;
		solidArea.y=24;
		solidAreaDefaultX=solidArea.x;
		solidAreaDefaultY=solidArea.y;
		
		solidArea.width= 20;
		solidArea.height=20;
		
//		attackArea.width=34;
//		attackArea.height=33;
		
		setDefaultValues();
		getPlayerImage();
		getPlayerAttackImage();
		setItems();
		
	}
	public void getPlayerImage() {
			
		up1=setup("/player/Player_up_1", gp.tileSize, gp.tileSize);
		up2=setup("/player/Player_up_2", gp.tileSize, gp.tileSize);
		down1=setup("/player/Player_down_1", gp.tileSize, gp.tileSize);
		down2=setup("/player/Player_down_2", gp.tileSize, gp.tileSize);
		left1=setup("/player/Player_left_1", gp.tileSize, gp.tileSize);
		left2=setup("/player/Player_left_2", gp.tileSize, gp.tileSize);
		right1=setup("/player/Player_right_1", gp.tileSize, gp.tileSize);
		right2=setup("/player/Player_right_2", gp.tileSize, gp.tileSize);
	}
	public void getPlayerAttackImage() {
		
		attackUp1=setup("/player/Player_attack_up1", gp.tileSize, gp.tileSize*2);
		attackUp2=setup("/player/Player_attack_up2", gp.tileSize, gp.tileSize*2);
		attackDown1=setup("/player/Player_attack_down1", gp.tileSize, gp.tileSize*2);
		attackDown2=setup("/player/Player_attack_down2", gp.tileSize, gp.tileSize*2);
		attackLeft1=setup("/player/Player_attack_left_1", gp.tileSize*2, gp.tileSize);
		attackLeft2=setup("/player/Player_attack_left_2", gp.tileSize*2, gp.tileSize);
		attackRight1=setup("/player/Player_attack_right_1", gp.tileSize*2, gp.tileSize);
		attackRight2=setup("/player/Player_attack_right_2", gp.tileSize*2, gp.tileSize);
	}

	public void setDefaultValues() {
		worldX=gp.tileSize * 23;
		worldY=gp.tileSize * 21;
		speed=4;
		direction="down";
		
		//PLAYER STATUS
		level=1;
		maxLife=6;
		life=maxLife;
		strength=1;
		dexterity=1;
		exp=0;
		nextLevelExp=5;
		coin=0;
		currentWeapon= new OBJ_Sword_Normal(gp);
		currentShield=new OBJ_Shield_Wood(gp);
		attack=getAttack();
		defense=getDefense();
	}
	
	public void setItems() {
		
		inventory.add(currentWeapon);
		inventory.add(currentShield);
		inventory.add(new OBJ_Key(gp));
		inventory.add(new OBJ_Key(gp));
	}
	public int getAttack() {
		
		attackArea=currentWeapon.attackArea;
		return attack=strength*currentWeapon.attackValue;	
	}
	public int getDefense() {
		
		return defense=dexterity*currentShield.defenseValue;
	}
	public void update() {
		//UPDATE THE PLAYER COORDINATES
		//KeyH means the keyhandler
		if(attacking==true) {
			attacking();
		}
		
		else if(keyH.upPressed==true || keyH.downPressed==true ||
				keyH.leftPressed==true || keyH.rightPressed==true || keyH.enterPressed==true) {
			
			
			if(keyH.upPressed == true) {
				direction = "up";
				
			}
			else if(keyH.downPressed == true) {
				direction = "down";
				
			}
			else if(keyH.leftPressed == true) {
				direction ="left";
				
			}
			else if(keyH.rightPressed == true) {
				direction = "right";
				
			}
			 //CHECK TILE COLLISON WITH collisonOn
			collisonOn= false;
			gp.cCheker.checkTile(this);
			
			//CHECK NPC COLLISON
			int npcIndex= gp.cCheker.checkEntity(this, gp.npc);
			interactNPC(npcIndex);
			
			//CHECK OBJECT COLLISON
			int objIndex =gp.cCheker.checkObject(this, true);
			pickUpObject(objIndex);
			
			//CHECK MONSTER COLLISON
			int monsterIndex =gp.cCheker.checkEntity(this, gp.monster);
			contactMonster(monsterIndex);
			
			//CHECK EVENT
			gp.eHandler.checkEvent();
			
			
			
			//IF TILE COLLISON IS FALSE, THEN PLAYER CAN MOVE
			if(collisonOn == false && keyH.enterPressed==false) {
				switch(direction) {
				case "up":
					worldY -=speed;
					break;
				case "down":
					worldY +=speed;
					break;
				case "left":
					worldX -=speed;
					break;
				case "right":
					worldX +=speed;
				break;
				}
			}
			
			if(gp.keyH.enterPressed==true && attackCanceled==false) {
				attacking=true;
				spriteCounter=0;
			}
			
			attackCanceled=false;
			gp.keyH.enterPressed=false;
			
			spriteCounter++;
			if(spriteCounter > 14) {
				if(spriteNum==1) {
					spriteNum = 2;
				}
				else if(spriteNum==2) {
					spriteNum=1;
				}
				spriteCounter=0;
			}
		}	
		if(invincible == true) {   //this is for even when the player is not moving, the invincible time doesnot stop
			invincibleCounter++;
			if(invincibleCounter>60) {
				invincible=false;
				invincibleCounter=0;
			}
		}
	}
	public void attacking() {
		spriteCounter++;
		
		if(spriteCounter<=5) {
			spriteNum=1;
		}
		if(spriteCounter>5 && spriteCounter<=25) {
			spriteNum=2;
			
			//save players current worldX, Y and solidArea
			int currentWorldX=worldX;
			int currentWorldY=worldY;
			int solidAreaWidth=solidArea.width;
			int solidAreaHeight=solidArea.height;
			
			//Adjust players worldX, Y for attack area
			switch(direction) {
			case "up": worldY -=attackArea.height; break;
			case "down": worldY +=attackArea.height; break;
			case "left": worldX -=attackArea.width; break;
			case "right": worldX +=attackArea.width; break;
			}
			//attack area becomes solid for collision
			solidArea.width=attackArea.width;
			solidArea.height=attackArea.height;
			int monsterIndex=gp.cCheker.checkEntity(this, gp.monster); //checking monster collision with attack area
			damageMonster(monsterIndex);
			
			//after monster collision is done we restore the players solid area
			worldX=currentWorldX;
			worldY=currentWorldY;
			solidArea.width=solidAreaWidth;
			solidArea.height=solidAreaHeight;
			
		}
		if(spriteCounter>25) {
			spriteNum=1;
			spriteCounter=0;
			attacking= false;
		}
	}
	
	public void pickUpObject(int i) {
		//USING THIS METHOD WE CAN PICK AN OBJECT
		if(i != 999) {
		
			String text;
			if(inventory.size()!=maxInventorySize) {
				inventory.add(gp.obj[i]);
				gp.playSE(1);
				text="You have picked up a " +gp.obj[i].name+ "!";
			}
			else {
				text="Your inventory is full.";
			}
			gp.ui.addMessage(text);
			gp.obj[i]=null;
		}
		
	}
	public void interactNPC(int i) {
		if(gp.keyH.enterPressed==true) {
			if(i != 999) {	
				attackCanceled=true;
					gp.gameState=gp.dialougeState;  //changing game state here
					gp.npc[i].speak();
			}
		}	
	}
	public void contactMonster(int i) {
		if(i != 999) {
			if(invincible == false) {
				gp.playSE(6);
				int damage=gp.monster[i].attack-defense;
				if(damage<0) {
					damage=0;
				}
				life -=damage;
				invincible=true;
			}			
		}
	}
	public void damageMonster(int i) {
		if(i!=999) {
			if(gp.monster[i].invincible==false) {
				
				gp.playSE(5);
				int damage=attack-gp.monster[i].defense;
				if(damage<0) {
					damage=0;
				}				
				gp.monster[i].life -=damage;
				gp.ui.addMessage(damage+ " damage!");
				
				gp.monster[i].invincible=true;
				gp.monster[i].damageReaction();
				
				if(gp.monster[i].life<=0) {
					gp.monster[i].dying=true;
					gp.ui.addMessage("You've Killed the "+gp.monster[i].name+"!");
					gp.ui.addMessage("Exp + "+gp.monster[i].exp);
					exp+=gp.monster[i].exp; //player recives exp
					
					checkLevelUp();
				}
			}
		}
	}
	
	public void checkLevelUp() {

		if(exp>= nextLevelExp) { //increasing player stats for next level
			level++;
			nextLevelExp=nextLevelExp*2;
			maxLife+=2;
			strength++;
			dexterity++;
			attack=getAttack();
			defense=getDefense();
			
			gp.gameState=gp.dialougeState;
			gp.ui.currentDialouge="You are level"+level+" now!\n"
					+"You feel a lot stronger!";
		}
		
	}
	public void selectItem() {
		
		int itemIndex=gp.ui.getItemIndexOnSlot();
		if(itemIndex< inventory.size()) { //this means we are not selecting a vacant slot
			
			Entity selectedItem=inventory.get(itemIndex);
			if(selectedItem.type==type_sword || selectedItem.type==type_axe) {
				currentWeapon=selectedItem;
				attack = getAttack();  //updating player's attack based on current weapon
			}
			if(selectedItem.type==type_shield) {
				currentShield=selectedItem;
				defense=getDefense();
			}
			if(selectedItem.type==type_consumable) {
				
				
			}
		}		
	}
	public void draw(Graphics2D g2) {
//DRAWING PLAYER SPRITE ACCORDING TO ITS DIRECTION	
		BufferedImage image=null;
		int tempScreenX=screenX;
		int tempScreenY=screenY;
		
		switch(direction) {
		
		case "up":
			if(attacking == false) {
				if(spriteNum == 1) {image = up1;}
				if(spriteNum==2) {image= up2;}
			}
			if(attacking==true) {
				tempScreenY=screenY-gp.tileSize;
				if(spriteNum == 1) {image = attackUp1;}
				if(spriteNum==2) {image= attackUp2;}
			}
			break;			
		case "down":
			if(attacking==false) {
				if(spriteNum == 1) {image = down1;}							
				if(spriteNum==2) {image= down2;}
			}			
			if(attacking==true) {
				if(spriteNum == 1) {image = attackDown1;}							
				if(spriteNum==2) {image= attackDown2;}
			}
			break;			
		case "left":
			if(attacking==false) {
				if(spriteNum == 1) {image = left1;}
				if(spriteNum==2) {image= left2;}
			}
			if(attacking==true) {
				tempScreenX=screenX-gp.tileSize;
				if(spriteNum == 1) {image = attackLeft1;}
				if(spriteNum==2) {image= attackLeft2;}
			}
			break;			
		case "right":
			if(attacking==false) {
				if(spriteNum == 1) {image = right1;}
				if(spriteNum==2) {image= right2;}
			}
			if(attacking==true) {
				if(spriteNum == 1) {image = attackRight1;}
				if(spriteNum==2) {image= attackRight2;}
			}
			break;			
		}
		g2.drawImage(image, tempScreenX, tempScreenY, null); // Draw player
		//g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height); // Draw RECTANGLE
				
		//DEBUG
		g2.setFont(new Font("Arial",Font.BOLD, 26));
		g2.setColor(Color.white);
		g2.drawString("Invinsible:"+invincibleCounter,10,100);
		
		
	}
}
