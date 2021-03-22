package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.util.RandomHelper;

public class Ghost extends Actor{
    public Ghost(Cell cell) {
        super(cell);
        this.type = "monster";
        this.setHealth(RandomHelper.getRandomNumber(25, 40));
        this.setAttack(RandomHelper.getRandomNumber(5, 10));
        this.setDefense(RandomHelper.getRandomNumber(1, 3));
    }

    @Override
    public void move(int dx, int dy) {

        Cell nextCell = this.getCell().getNeighbor(dx, dy);
        if (!(nextCell.getActor() instanceof Ghost))
            this.getCell().setActor(null);
            nextCell.setActor(this);
            this.setCell(nextCell);

    }

    @Override
    public String getTileName() {
        return "ghost";
    }
}
