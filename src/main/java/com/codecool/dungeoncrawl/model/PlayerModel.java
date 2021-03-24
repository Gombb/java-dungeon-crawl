package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.actors.Player;

public class PlayerModel extends BaseModel {
    private String playerName;
    private int hp;
    private int attack;
    private int defense;
    private int x;
    private int y;
    private int id;

   public PlayerModel(String playerName, int x, int y) {
        this.playerName = playerName;
        this.x = x;
        this.y = y;
    }


    public PlayerModel(Player player) {
        this.playerName = player.getCharacterName();
        this.x = player.getX();
        this.y = player.getY();
        this.attack = player.getAttack();
        this.defense = player.getDefense();
        this.id = player.getId();
        this.hp = player.getHealth();
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
