package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.util.RandomHelper;

public class Apple extends Item {
    public Apple(Cell cell) {
        super(cell, "Apple", RandomHelper.getRandomNumber(2, 5));
    }

    @Override
    public String getTileName() {
        return "apple";
    }
}
