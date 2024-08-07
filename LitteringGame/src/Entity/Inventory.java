package Entity;


import items.Items;






import java.util.ArrayList;


import Main.GamePanel;






public class Inventory {
GamePanel gp;
private ArrayList<Items> items;


public Inventory() {
items = new ArrayList<>();
}


public void clearList() {
items.clear();
}
public void addItem(Items item) {
items.add(item);
}


public void removeItem(Items item) {
items.remove(item);
}


public boolean isEmpty() {
return items.isEmpty();
}
public Items getItem(int i) {
return items.get(i);
}
public Items getFirstItem() {
if (!items.isEmpty()) {
return items.get(0);
}
return null;
}
public int getItemCount() {
return items.size();
}
}

