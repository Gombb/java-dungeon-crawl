package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Shield extends Item {
    public Shield(Cell cell) {
        super(cell, "Shield");
    }

    @Override
    public String getTileName() {
        return "buckler";
    }
}
