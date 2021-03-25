package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.PlayerModel;

import java.util.ArrayList;
import java.util.List;

public interface PlayerDao {
    PlayerModel add(PlayerModel player);
    void update(PlayerModel player);
    PlayerModel get(int id);
    List<PlayerModel> getAll();
    ArrayList<Integer> getIdList();
}
