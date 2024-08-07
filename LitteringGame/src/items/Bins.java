
package items;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;


import javax.imageio.ImageIO;


import Main.GamePanel;


public class Bins {
GamePanel gp;
public int x, y;
public String type;
public BufferedImage image;


public Bins(GamePanel gp, String type, int x, int y) {
this.gp = gp;
this.type = type;
this.x = x;
this.y = y;


loadImage();
}
public String getType() {
return(type);
}
public void loadImage() {
try {
if (type.equals("recycle")) {
image = ImageIO.read(getClass().getResourceAsStream("/res/bins/recylingBin.png"));
} else if (type.equals("trash")) {
image = ImageIO.read(getClass().getResourceAsStream("/res/bins/trashBin.png"));
}
} catch (IOException e) {
e.printStackTrace();
}
}


public boolean isItemInBin(Items item) {
return item.x < x + gp.tileSize && item.x + gp.tileSize > x && item.y < y + gp.tileSize
&& item.y + gp.tileSize > y;
}


public void draw(Graphics2D g2) {
g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
}


}
