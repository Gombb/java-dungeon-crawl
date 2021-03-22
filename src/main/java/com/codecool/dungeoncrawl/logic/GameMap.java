package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;

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
}
