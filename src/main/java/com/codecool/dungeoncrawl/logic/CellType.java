package com.codecool.dungeoncrawl.logic;

public enum CellType {
    EMPTY("empty"),
    FLOOR("floor"),
    WALL("wall"),
    GATE("gate");

    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }
}
