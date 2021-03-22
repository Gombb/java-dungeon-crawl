package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.util.RandomHelper;

public class Boss extends Actor{
    public Boss(Cell cell) {
        super(cell);
        this.setHealth(50);
        this.setAttack(RandomHelper.getRandomNumber(10, 20));
        this.setDefense(RandomHelper.getRandomNumber(5, 10));
    }

    @Override
    public String getTileName() {
        return "boss";
    }
}
