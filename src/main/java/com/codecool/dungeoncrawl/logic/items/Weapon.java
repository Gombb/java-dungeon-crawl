package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.util.RandomHelper;

public class Weapon extends Item{
    public static final String[] WEAPON_NAMES = {
            "Etherius",
            "Silence",
            "Widow Maker",
            "Fire Infused Carver",
            "Eternal Mageblade",
            "Vengeful Glass Swiftblade",
            "Dire Mithril Sabre",
            "Nightcrackle, Promise of the Void",
            "Scar, Skewer of Mourning",
            "Doombringer, Longblade of the Lion"
    };


    public Weapon(Cell cell) {
        super(cell, RandomHelper.getRandomName(WEAPON_NAMES), RandomHelper.getRandomNumber(5, 10));
    }

    @Override
    public String getTileName() {
        return "weapon";
    }
}
