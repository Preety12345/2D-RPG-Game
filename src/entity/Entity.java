package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {
	//THIS CLASS WILL HAVE VARIBALES FOR PLAYER/MONSTER/OTHER THINGS
	GamePanel gp;			
	public BufferedImage image, image2, image3;
	public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public Rectangle solidArea=new Rectangle(0,0,48,48);
	public Rectangle attackArea=new Rectangle(0,0,0,0);
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collison = false;	
	String dialouges[]=new String[20];
	
	//STATE
	public int worldX,worldY; //FOR WORLD MAP
	public String direction="down";
	public int spriteNum=1;   //SETTING DEFAULT DIRECTION	
	int dialougeIndex=0;
	public boolean invincible = false;
	public boolean collisonOn= false;
	boolean attacking=false;
	public boolean alive=true;
	public boolean dying=false;
	boolean hpBarOn=false;
	
	//COUNTER
	public int spriteCounter=0;
	public int invincibleCounter=0;
	public int actionLockCounter=0;
	int dyingCounter=0;
	int hpBarCounter=0;
					
	//CHARACTER STATUS
	public String name;
	public int maxLife;
	public int life;
	public int speed;
	public int level;
	public int strength;
	public int dexterity;
	public int exp;
	public int nextLevelExp;
	public int attack;
	public int defense;
	public int coin;
	public Entity currentWeapon;
	public Entity currentShield;
	
	//ITEM ATTRIBUTES
	public int attackValue;
	public int defenseValue;
	public String description="";
	
	//TYPE
	public int type; // 0=player, 1=npc, 2=monster 
	public final int type_player=0;
	public final int type_npc=1;
	public final int type_monster=2;
	public final int type_sword=3;
	public final int type_axe=4;
	public final int type_shield=5;
	public final int type_consumable=6;
	
	public Entity(GamePanel gp) {
		this.gp=gp;
	}
	public void setAction() {}
	public void damageReaction() {}
	public void speak() {
		if(dialouges[dialougeIndex]==null) {
			dialougeIndex=0;
		}
		gp.ui.currentDialouge=dialouges[dialougeIndex];
		dialougeIndex++;
		
		switch(gp.player.direction) { //FIXING NPC DIRECTION
		case "up":
			direction="down";
			break;
		case "down":
			direction="up";
			break;
		case "left":
			direction="right";
			break;
		case "right":
			direction="left";
			break;
		}
	}
	public void update() {
		setAction();
		
		collisonOn=false;
		gp.cCheker.checkTile(this); //PASSING NPC OLDMAN TO CHECK COLLISON
		gp.cCheker.checkObject(this, false);
		gp.cCheker.checkEntity(this, gp.npc);
		gp.cCheker.checkEntity(this, gp.monster);
		boolean contactPlayer= gp.cCheker.checkPlayer(this);
		
		if(this.type== type_monster && contactPlayer==true) {
			if(gp.player.invincible==false) {
				//we can give damage
				gp.playSE(6);
				int damage=attack-gp.player.defense;  //when monster contacts with player the player life will decrease
				if(damage<0) {							//based on this algorithm
					damage=0;
				}
				gp.player.life -=damage;
				gp.player.life -=1;
				gp.player.invincible=true;
			}
		}
				
		//IF TILE COLLISON IS FALSE, THEN PLAYER CAN MOVE
		if(collisonOn == false) {
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
		if(invincible == true) {   
			invincibleCounter++;
			if(invincibleCounter>60) {
				invincible=false;
				invincibleCounter=0;
			}
		}
	}
	public void draw(Graphics2D g2) {
		BufferedImage image=null;
		int screenX=worldX - gp.player.worldX + gp.player.screenX; //SCREEN POSITION FROM PLAYER
		int screenY= worldY - gp.player.worldY + gp.player.screenY;
		
		if(worldX+gp.tileSize> gp.player.worldX-gp.player.screenX &&  //THIS LOOP IS FOR TO SEE THE PART 
			worldX-gp.tileSize < gp.player.worldX+gp.player.screenX && //WE WANT TO SEE, NOT THE
			worldY+gp.tileSize> gp.player.worldY-gp.player.screenY && // ACTUAL MAP                                                                                                                 
			worldY-gp.tileSize < gp.player.worldY+gp.player.screenY) {	
			switch(direction) {
			
			case "up":
				if(spriteNum == 1) {image = up1;}
				if(spriteNum==2) {image= up2;}
				break;
			case "down":
				if(spriteNum == 1) {image = down1;}
				if(spriteNum==2) {image= down2;}
				break;
			case "left":
				if(spriteNum == 1) {image = left1;}
				if(spriteNum==2) {image= left2;}
				break;
			case "right":
				if(spriteNum == 1) {image = right1;}
				if(spriteNum==2) {image= right2;}
				break;	
			}
			//MONSTER HP bar
			if(type==2 && hpBarOn==true) {
				double oneScale=(double)gp.tileSize/maxLife;  //for reducing the monster HP bar
				double hpBarValue=oneScale*life;
				
				g2.setColor(new Color(35,35,35));
				g2.fillRect(screenX-1, screenY-16, gp.tileSize+2, 12);
				g2.setColor(new Color(255,0,30));
				g2.fillRect(screenX, screenY-15, (int)hpBarValue, 10);
				
				hpBarCounter++;
				if(hpBarCounter>300) {
					hpBarCounter=0;
					hpBarOn=false;
				}
			}			
			if(invincible==true) {
				hpBarOn=true;
				hpBarCounter=0;
				changeAlpha(g2,0.4f);
			}
			if(dying == true) {
				dyingAnimation(g2);
			}
			
			g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
			changeAlpha(g2,1f);
		}	
	}
	public void dyingAnimation(Graphics2D g2) {
		dyingCounter++;
		int i=5;
		//in every 5 frame, we switch the monsters alpha/transparency to 0 or 1
		if(dyingCounter<=i) { changeAlpha(g2, 0f);	}
		if(dyingCounter>i && dyingCounter<=i*2) {changeAlpha(g2, 1f);}
		if(dyingCounter>i*2 && dyingCounter<=i*3) {changeAlpha(g2, 0f);}
		if(dyingCounter>i*3 && dyingCounter<=i*4) {changeAlpha(g2, 1f);}
		if(dyingCounter>i*4 && dyingCounter<=i*5) {changeAlpha(g2, 0f);}
		if(dyingCounter>i*5 && dyingCounter<=i*6) {changeAlpha(g2, 1f);}
		if(dyingCounter>i*6 && dyingCounter<=i*7) {changeAlpha(g2, 0f);}
		if(dyingCounter>i*7 && dyingCounter<=i*8) {changeAlpha(g2, 1f);}
		
		if(dyingCounter>i*8) {
			dying=false;
			alive=false;
		}		
	}
	public void changeAlpha(Graphics2D g2, float alphaValue) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alphaValue));
	}
	public BufferedImage setup(String imagePath, int width, int height) {
		//THIS METHOD IS TO SCALE IMAGE
		UtilityTool uTool= new UtilityTool();
		BufferedImage image=null;
		try {
			image=ImageIO.read(getClass().getResourceAsStream(imagePath+".png"));
			image=uTool.scaleImage(image, width, height);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return image;
	}
}
