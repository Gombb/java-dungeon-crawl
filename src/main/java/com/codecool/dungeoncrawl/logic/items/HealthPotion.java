package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.util.RandomHelper;

public class HealthPotion extends Item {

    public HealthPotion(Cell cell) {
        super(cell, "Health Potion", RandomHelper.getRandomNumber(10, 25));
    }

    @Override
    public String getTileName() {
        return "health_potion";
    }
}
