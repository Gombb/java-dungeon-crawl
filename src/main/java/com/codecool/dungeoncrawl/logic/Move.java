package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.Directions;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Shield;
import com.codecool.dungeoncrawl.logic.items.Weapon;

import java.util.ArrayList;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Move {
    private GameMap map;


    public Move (GameMap map){
        this.map = map;

    }

    public void initRound(Directions direction){
        if (checkIfNeighbourIsActor(direction) != null){
            initCombat(map.getPlayer(), checkIfNeighbourIsActor(direction));
        }else{
            map.getPlayer().move(direction.getCordX(), direction.getCordY());
            moveNPCs();
        }
    }

    private Actor checkIfNeighbourIsActor(Directions direction){
        return map.getPlayer().getCell().getNeighbor(direction.getCordX(), direction.getCordY()).getActor();
    }

    private int calculateDamage(int attack, int defense){
        int damage = attack - defense;
        if (damage < 0){
            damage = attack;
        }
        return damage;
    }

    public void initCombat(Actor attacker, Actor defender){
        while (attacker.getHealth() > 0 && defender.getHealth() > 0){
            defender.setHealth(defender.getHealth() - calculateDamage(attacker.getAttack(), defender.getDefense()));
            attacker.setHealth(attacker.getHealth() - calculateDamage(defender.getAttack(), attacker.getDefense()));
        }
        if (map.getPlayer().getHealth() > 0) {
            defender.getCell().setActor(null);
            if (defender instanceof Skeleton) {
                defender.getCell().setItem(new Shield(defender.getCell()));
            } else if (defender instanceof Ghost) {
                defender.getCell().setItem(new Weapon(defender.getCell()));
            } else {
                defender.getCell().setItem(new Key(defender.getCell()));
            }
        }
    }
    public boolean moveValidator(Actor monster, Directions direction){
        Cell nextCell = monster.getCell().getNeighbor(direction.getCordX(), direction.getCordY());
        if (monster instanceof Ghost){
            if (direction == Directions.North && monster.getY() == 1) {
                return false;
            }else if (direction == Directions.South && monster.getY() == map.getHeight() -2) {
                return false;
            }else if (direction == Directions.East && monster.getX() == map.getWidth() -2) {
                return false;
            }else if (direction == Directions.West && monster.getX() == 1) {
                return false;
            }else {
                if (nextCell.getActor() != null){
                    return false;
                }else {
                    return true;
                }
            }
        }else if (monster instanceof Skeleton) {
            if ((!nextCell.getType().getTileName().equals("wall")) && nextCell.getActor() != null){
                return false;
            }else{
                return true;
            }
        }

        return true;

    }

    public void moveNPCs () {
        ArrayList<Actor> monsterList = monsterCounter();
        monsterList.forEach(monster -> {
            if (!(monster instanceof Boss)) {
                Directions randomDirection = null;
                int counter = 0;
                boolean moveIsValid = false;
                while (!moveIsValid) {
                    counter++;
                    randomDirection = getRandomDirection();
                    moveIsValid = moveValidator(monster, randomDirection);
                    if (counter > 4){
                        randomDirection = null;
                        break;
                    }
                }
                if (randomDirection != null) {
                    if (checkIfNeighbourIsActor(randomDirection) instanceof Player) {
                        initCombat(monster, map.getPlayer());
                    }
                    monster.move(randomDirection.getCordX(), randomDirection.getCordY());
                }
            }
        });
    }

    public Directions getRandomDirection() {
        Random random = new Random();
        return Directions.values()[random.nextInt(Directions.values().length)];
    }

    public ArrayList<Actor> monsterCounter() {
        ArrayList <Actor> monsterList = new ArrayList<>();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null){
                    Actor actor = cell.getActor();
                    if(actor instanceof Player){
                        assert false;
                    }else{
                        monsterList.add(actor);
                    }
                }
            }
        }
        return monsterList;
    }

}
