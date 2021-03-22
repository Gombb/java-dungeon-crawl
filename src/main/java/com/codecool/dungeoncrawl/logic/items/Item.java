package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

public abstract class Item implements Drawable {

    private Cell cell;
    private String name;
    private int value;


    public Item(Cell cell, String name, int value) {
        this.cell = cell;
        this.cell.setItem(this);
        this.name = name;
        this.value = value;
    }

    public Cell getCell() {
        return cell;
    }

    public int getValue() {return this.value;}

    public String getItemName() {return this.name;}

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }
}
