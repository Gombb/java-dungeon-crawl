package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.util.RandomHelper;

public class Cheese extends Item {

    public Cheese(Cell cell) {
        super(cell, "Cheese", RandomHelper.getRandomNumber(5, 10));
    }

    @Override
    public String getTileName() {
        return "cheese";
    }
}
