package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameState;

import java.util.List;

public interface GameStateDao {
    GameState add(GameState state);
    void update(GameState state);
    GameState get(int id);
    List<GameState> getAll();
}
