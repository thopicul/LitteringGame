

package items;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;


import javax.imageio.ImageIO;


import Main.GamePanel;


public class Items {
GamePanel gp;
public int x, y;
public BufferedImage image;
public String type;


public Items(GamePanel gp) {
this.gp = gp;
chooseTrash();
setDefaultValue();
loadImage();
}


public void setDefaultValue() {
x = (int) (Math.random() * (gp.screenWidth - (gp.tileSize * 2)));
y = (int) (Math.random() * (gp.screenHeight - (gp.tileSize * 2)));
}


public void chooseTrash() {
int trash = (int) (Math.random() * 4) + 1;


if (trash == 1) {
type = "bottle";
} else if (trash == 2) {
type = "fruit";
} else if (trash == 3) {
type = "paper";
} else if (trash == 4) {
type = "trashBag";
}
}
public String getType() {
return(type);
}


public void loadImage() {
try {
if (type.equals("trashBag")) {
image = ImageIO.read(getClass().getResourceAsStream("/res/trash/trash.png"));
} else if (type.equals("fruit")) {
image = ImageIO.read(getClass().getResourceAsStream("/res/trash/fruit.png"));
} else if (type.equals("paper")) {
image = ImageIO.read(getClass().getResourceAsStream("/res/trash/paper.png"));
} else if (type.equals("bottle")) {
image = ImageIO.read(getClass().getResourceAsStream("/res/trash/bottle.png"));
}


} catch (IOException e) {


}


}


public void draw(Graphics2D g2) {


g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
}
public void drawCurrent(Graphics2D g2) {


g2.drawImage(image, 40, 90, gp.tileSize, gp.tileSize, null);


}
}
