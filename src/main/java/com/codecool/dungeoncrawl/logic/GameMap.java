package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.items.*;

import java.util.ArrayList;

public class GameMap {
    private int level;
    private int width;
    private int height;
    private Cell[][] cells;
    private ArrayList <Actor> actorList = new ArrayList<>();

    private Player player;

    public GameMap(int width, int height, CellType defaultCellType, int level) {
        this.level = level;
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public void addActor(Actor actor){
        this.actorList.add(actor);
    }

    public ArrayList<Actor> getActorList (){
        return this.actorList;
    }

    public Cell[][] getCellsArray(){ return this.cells; }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getCurrentMap() {
        StringBuilder sb = new StringBuilder();
        sb.append(width).append(" ").append(height);
        sb.append("\n");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (getCell(j, i).getActor() != null) {
                    sb.append(switchTileNameToMarker(getCell(j, i).getActor().getTileName()));
                } else if (getCell(j, i).getItem() != null) {
                    sb.append(switchTileNameToMarker(getCell(j, i).getItem().getTileName()));
                } else {
                    sb.append(switchTileNameToMarker(getCell(j, i).getTileName()));
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private char switchTileNameToMarker(String tileName) {
        switch (tileName) {
            case "empty":
                return ' ';
            case "wall":
                return '#';
            case "floor":
                return '.';
            case "skeleton":
                return 's';
            case "ghost":
                return 'g';
            case "player":
                return '@';
            case "health_potion":
                return 'p';
            case "key":
                return 'k';
            case "weapon":
                return 'w';
            case "shield":
                return 'b';
            case "cheese":
                return 'c';
            case "apple":
                return 'a';
            case "boss":
                return 'B';
            case "gate":
                return 'G';
            default:
                throw new RuntimeException("Unrecognized character: '" + tileName + "'");
        }
    }
}
