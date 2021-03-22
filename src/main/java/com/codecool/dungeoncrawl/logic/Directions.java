package com.codecool.dungeoncrawl.logic;

public enum Directions {

    North(0, -1),
    South(0, 1),
    West(-1, 0),
    East(1, 0);


    private final int cordX;
    private final int cordY;

    Directions(int cordX, int cordY) {
        this.cordX = cordX;
        this.cordY = cordY;
    }

    public int getCordX() {
        return cordX;
    }

    public int getCordY() {
        return cordY;
    }
}
