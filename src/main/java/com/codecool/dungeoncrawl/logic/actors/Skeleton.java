package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.util.RandomHelper;

public class Skeleton extends Actor {
    public Skeleton(Cell cell) {
        super(cell);
        this.type = "monster";
        this.setHealth(RandomHelper.getRandomNumber(10, 25));
        this.setAttack(RandomHelper.getRandomNumber(2, 5));
        this.setDefense(RandomHelper.getRandomNumber(1, 5));
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}
