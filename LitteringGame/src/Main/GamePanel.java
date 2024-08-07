package Main;

import Entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import tiles.TileManager;
import Entity.Inventory;
import items.Bins;
import items.Items;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 16; // 16 x16 tile
    final int scale = 3;

    public int tileSize = originalTileSize * scale; // 48x48 tile
    public int maxScreenCol = 16;
    public int maxScreenRow = 12;
    public int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public int screenHeight = tileSize * maxScreenRow; // 576 pixels

    public int points = 0;

    int FPS = 60;

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    Player player = new Player(this, keyH);

    Items currentItem;
    List<Bins> bins = new ArrayList<>();
    Bins recyclingBin = new Bins(this, "recycle", 632, 175);
    Bins trash = new Bins(this, "trash", 632, 276);
    Inventory inventory = new Inventory();

    UI ui = new UI(this);

    List<String> facts;
    public int gameState;
    public int playState = 1;
    public int pauseState = 2;
    public int dialogueState = 3;
    public int finishedState = 4;

    public GamePanel() {
        spawnItem();
        inventory.clearList();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        facts = new ArrayList<>();
        facts.add("NYC could recycle enough aluminum cans per year to save over\n12 million gallons of gasoline. Thats enought to drive to the moon\nand back 50 times!!");
        facts.add("If NYC recycled all of our plastic bottles we could make enough\ncarpeting to cover all of Manhattan below 14th St. each\nyear!!!");
        facts.add("If we recycled all of our in NYC paper we could save enough energy\nto power all the houses in the Bronx and Staten Island\nevery day!!");
        facts.add("If we recycled all of our paper we could save enough energy to\npower 668,484 New York City homes!!!");
        facts.add("We could save enough energy to power 42,675 NYC homes per year\nby recycling bottles and jars!!!");
        facts.add("Did you know trash can attract pests like cockroaches and rats,\nwhich can damage our homes?");
        facts.add("New Yorkers throw away 1,579,600 pounds of plastic bottles and\njugs every week—that’s the weight of 100 elephants!");
        facts.add("The overflow of garbage blocks sidewalks and roads, making it\ndifficult for people to walk and drive");
        facts.add("Litter makes places less fun to visit and walk around. Keep our\nstreets clean to make them more inviting!");
        facts.add("If litter gets into our water systems, it can make the water we\ndrink dirty and unsafe");
        gameState = dialogueState;

        ui.currentDialouge = "Welcome to Clean Up Quest. This is game is to help you learn\nabout littering by sorting different types of trash into the different\ntypes of bins. Move around with W A S D. Press enter to exit and\npress p anytime to pause. Happy sorting!";
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        double drawInterval = 1000000000 / FPS; // 0.01666 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {
        if (gameState == playState) {
            player.update();
            checkItemCollision();
            checkTrashInBin();
        }
    }

    public void chooseFact() {
        int index = (int) (Math.random() * facts.size());
        ui.currentDialouge = facts.get(index);
        facts.remove(index);
    }

    public void checkItemCollision() {
        if (currentItem != null) {
            int playerX = player.x;
            int playerY = player.y;
            int playerSize = tileSize;

            int itemX = currentItem.x;
            int itemY = currentItem.y;
            int itemSize = tileSize;

            // Debugging output
            System.out.println("Player X: " + playerX + ", Y: " + playerY);
            System.out.println("Item X: " + itemX + ", Y: " + itemY);

            // Collision detection
            if (playerX < itemX + itemSize &&
                playerX + playerSize > itemX &&
                playerY < itemY + itemSize &&
                playerY + playerSize > itemY && inventory.getItemCount() == 0) {
                ui.showMesage("You picked up " + currentItem.type + " .Go throw it in the Garbage!");
                gameState = dialogueState;
                chooseFact();
                inventory.addItem(currentItem);
                currentItem = null;
                spawnItem();
                if (points > 5) {
                    gameState = finishedState;
                }
            }
        }
    }

    public void checkTrashInBin() {
        int playerX = player.x;
        int playerY = player.y;
        int playerSize = tileSize;

        int recyclingBinX = recyclingBin.x;
        int recyclingBinY = recyclingBin.y;
        int recyclingSize = tileSize;

        int trashX = trash.x;
        int trashY = trash.y;
        int trashSize = tileSize;

        // Iterate through each item in the inventory
        for (int i = 0; i < inventory.getItemCount(); i++) {
            Items binCheck = inventory.getItem(i); // Get each item

            if (binCheck == null) {
                continue; // Skip null items
            }

            // Check for recycling bin collision
            if (playerX < recyclingBinX + recyclingSize &&
                playerX + playerSize > recyclingBinX &&
                playerY < recyclingBinY + recyclingSize &&
                playerY + playerSize > recyclingBinY) {
                // Collision with recycling bin detected
                // checking if the trash is recyclable
                if (binCheck.getType().equals("bottle") || binCheck.getType().equals("paper")) {
                    points++;
                    inventory.removeItem(binCheck); // Remove the item from the inventory
                    ui.showMesage("Correct! adding one point");
                } else {
                    ui.showMesage("this is the wrong bin");
                }
            }

            // Check if the player is interacting with the trash bin
            if (playerX < trashX + trashSize &&
                playerX + playerSize > trashX &&
                playerY < trashY + trashSize &&
                playerY + playerSize > trashY) {
                // Check if the item type matches the criteria
                if (binCheck.getType().equals("fruit") || binCheck.getType().equals("trashBag")) {
                    points++;
                    inventory.removeItem(binCheck); // Remove the item from the inventory
                    ui.showMesage("Correct! adding one point");
                } else {
                    ui.showMesage("this is the wrong bin");
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        tileM.draw(g2);
        player.draw(g2);

        if (currentItem != null) {
            currentItem.draw(g2);
        }

        recyclingBin.draw(g2);
        trash.draw(g2);

        ui.draw(g2);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        g2.drawString("Score: " + points, 50, 30);
        g2.drawString("Items in Inventory: " + inventory.getItemCount(), 50, 70);

        if (inventory.getItemCount() > 0) {
            Items itemInInventory = inventory.getFirstItem();
            g2.setFont(new Font("Arial", Font.PLAIN, 18));
            g2.drawString("Current Item: " + itemInInventory.getType(), 50, 90);
            itemInInventory.drawCurrent(g2);
        }

        g2.dispose();
    }

    public void spawnItem() {
        currentItem = new Items(this);
    }
}
