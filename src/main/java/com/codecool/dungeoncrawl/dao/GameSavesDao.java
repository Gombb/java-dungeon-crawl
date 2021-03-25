package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameSave;

import java.util.List;

public interface GameSavesDao {
    GameSave add(GameSave gamesave);
    GameSave get(int id);
    List<GameSave> getAll();
}
