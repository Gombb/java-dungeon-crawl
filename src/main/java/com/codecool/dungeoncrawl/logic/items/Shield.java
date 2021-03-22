package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.util.RandomHelper;

public class Shield extends Item {
    public static final String[] SHIELD_NAMES = {
            "Kingdom's Pride",
            "Hero's Calling",
            "Honor's Grasp",
            "Lich Shield Wall",
            "Howling Armament",
            "Crazed Oak Barricade",
            "Vanquisher Hardwood Armament",
            "Chaos, Vengeance of Blessings",
            "Kingdom's Honor, Destroyer of Stealth",
            "Amnesia, Reach of Grieving Widows"
    };

    public Shield(Cell cell) {
        super(cell, RandomHelper.getRandomName(SHIELD_NAMES), RandomHelper.getRandomNumber(3, 5));
    }

    @Override
    public String getTileName() {
        return "shield";
    }
}
