package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

public abstract class Actor implements Drawable {
    private Cell cell;
    private int health;
    protected String type;
    private int attack;
    private int defense;

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
        this.health = 100;
    }

    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        if (cell.getActor() instanceof Player && ((Player) cell.getActor()).isWallhackEnabled()) {
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        } else {
            if (!nextCell.getType().getTileName().equals("wall") && (!(nextCell.getType().getTileName().equals("gate")))) {
                cell.setActor(null);
                nextCell.setActor(this);
                cell = nextCell;
            }
        }
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getAttack(){
        return this.attack;
    }

    public int getDefense(){
        return this.defense;
    }

    public int getHealth() {
        return health;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public void setHealth (int health){
        this.health = health;
    }

    public String getType() {
        return type;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }
}
