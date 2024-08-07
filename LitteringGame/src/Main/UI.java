package Main;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.DecimalFormat;


public class UI {


GamePanel gp;
Font arial_40;
Graphics2D g2;
public boolean messageOn = false;
public String message = "";
int messageCounter = 0;
double playTime = 0.0;
public String currentDialouge = "";
DecimalFormat dFormat = new DecimalFormat("#0.00");
public UI(GamePanel gp) {
this.gp = gp;
arial_40 = new Font("Arial", Font.PLAIN, 40);
}
public void drawDialogueScreen(){
int x = gp.tileSize;
int y = gp.screenHeight - gp.tileSize*4;
int width = gp.screenWidth - (gp.tileSize*2);
int height = gp.tileSize*4;
drawSubWindow(x,y,width,height);


g2.setFont(g2.getFont().deriveFont(Font.PLAIN,20));
x+= gp.tileSize - 40;
y+= gp.tileSize;
for(String line : currentDialouge.split("\n")) {
g2.drawString(line,x,y);
y += 40;
}
}
public void drawSubWindow(int x, int y, int width, int height) {
Color c = new Color(0,0,0, 100);
g2.setColor(c);
g2.fillRoundRect(x, y, width, height,35,35 );
c= new Color(255,255,255);
g2.setColor(c);
g2.setStroke(new BasicStroke(5));
g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);


}
public void showMesage(String text) {
message = text;
messageOn = true;
}


public void draw(Graphics2D g2) {
this.g2 = g2;
if(gp.gameState == gp.dialogueState) {
drawDialogueScreen();
}
if(gp.gameState == gp.finishedState) {
g2.setFont(arial_40);
g2.setColor(Color.yellow);
int x = 0;
int y = gp.screenHeight/2;
g2.drawString("congratulations you finished the game", x, y);
g2.drawString("Time:" + dFormat.format(playTime), gp.tileSize*11, 65);
gp.gameThread = null;
}
if(gp.gameState == gp.playState) {
if (messageOn == true) {
playTime +=(double)1/60;
g2.setFont(g2.getFont().deriveFont(20F));
g2.drawString(message, (gp.tileSize/2)+ 30, gp.tileSize*5);
messageCounter++;
if(messageCounter > 100) {
messageCounter = 0;
messageOn = false;
}
}
}
if (gp.gameState == gp.pauseState) {
currentDialouge = "paused press enter to unpause";
drawDialogueScreen();
}


}
}


