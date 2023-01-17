package entity;

import java.util.Random;

import main.GamePanel;

public class NPC_OldMan extends Entity{

	public NPC_OldMan(GamePanel gp) {
		super(gp);
		
		direction="down";
		speed=1;
		getImage();
		setDialouge();
	}
	public void getImage() {
		
		up1=setup("/npc/oldman_up1", gp.tileSize, gp.tileSize);
		up2=setup("/npc/oldman_up2", gp.tileSize, gp.tileSize);
		down1=setup("/npc/oldman_down1", gp.tileSize, gp.tileSize);
		down2=setup("/npc/oldman_down2", gp.tileSize, gp.tileSize);
		left1=setup("/npc/oldman_left1", gp.tileSize, gp.tileSize);
		left2=setup("/npc/oldman_left2", gp.tileSize, gp.tileSize);
		right1=setup("/npc/oldman_right1", gp.tileSize, gp.tileSize);
		right2=setup("/npc/oldman_right2", gp.tileSize, gp.tileSize);
	}

	public void setDialouge() {
		dialouges[0]="Hello human";
		dialouges[1]="I see that you have also come to \nthis island to find the treasure.";
		dialouges[2]="Many adventurers come here everyday \nto look for the lost treasure.";
		dialouges[3]="well, good luck on your journey.";
	}
	public void setAction() {//SETTING BEHAVIOUR OF CHARACTER
	
		actionLockCounter++;
		
		if(actionLockCounter == 120) {
			Random random=new Random();
			int i= random.nextInt(100)+1; //THIS PICKS A NUMBER FROM 1 - 100
			
			if(i<=25) {
				direction="up";
			}
			if(i>25 && i<50) {
				direction="down";
			}
			if(i>50 && i<75) {
				direction="left";
			}
			if(i>75 && i<=100) {
				direction="right";
			}
			actionLockCounter=0; //RESETING THE COUNTER
		}
	
	}
	public void speak() {
		
	super.speak();
	}
	
}
