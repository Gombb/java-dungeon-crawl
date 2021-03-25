package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Shield;
import com.codecool.dungeoncrawl.logic.items.Weapon;

import java.util.ArrayList;

public class Player extends Actor {

    ArrayList<Item> inventory = new ArrayList<>();
    private boolean hasKey;
    private boolean wallhack;
    private String characterName;
    private int id;

    public Player(Cell cell) {
        super(cell);
        this.setHealth(100);
        this.setAttack(5);
        this.setDefense(2);
        this.type = "player";
    }

    public String getTileName() {
        return "player";
    }

    public void setHasKey(boolean hasKey) {
        this.hasKey = true;
    }

    public boolean isHasKey() {
        return hasKey;
    }

    public boolean isWallhackEnabled () {
        return wallhack;
    }

    public void lootItem(Item item) {
        inventory.add(item);
        useItem(item);
        if (item instanceof Key) setHasKey(true);
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void useItem(Item item) {
        switch (item.getTileName()) {
            case "health_potion":
            case "cheese":
            case "apple":
                setHealth(Math.min(getHealth() + item.getValue(), 100));
                inventory.remove(item);
                break;
            case "weapon":
                setAttack(getAttack() + item.getValue());
                break;
            case "shield":
                setDefense(getDefense() + item.getValue());
                break;
        }
    }

    public void processCheatCode(String userInput) {
        switch (userInput) {
            case "wallhack":
                this.wallhack = true;
                break;
            case "god":
                this.setHealth(1000);
                break;
            case "armory":
                for (int i = 0; i < 3; i++) {
                    Item weapon = new Weapon(getCell());
                    Item shield = new Shield(getCell());
                    inventory.add(weapon);
                    useItem(weapon);
                    inventory.add(shield);
                    useItem(shield);
                }
                break;
            default:
        }
    }

    public String displayInventory() {
        ArrayList<String> displayableInventory = new ArrayList<>();
        inventory.forEach(item -> displayableInventory.add(item.getItemName()));
        if (displayableInventory.size() > 0) {
            return displayableInventory.toString();
        } else {
            return "No items in inventory";
        }
    }
}
