package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameSave;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameDatabaseManager {
    private PlayerDao playerDao;
    private GameStateDao gameStateDao;
    private GameSavesDao gameSavesDao;

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        gameStateDao = new GameStateDaoJdbc(dataSource);
        gameSavesDao = new GameSavesDaoJdbc(dataSource);
    }
    
    public Integer getGameSavesIdForTitle(String title){
        List<GameSave> gameSaveList = gameSavesDao.getAll();
        for (GameSave gameSave : gameSaveList){
            if (gameSave.getTitle().equals(title)){
                return gameSave.getId();
            }
        }
        return null;
    }

    public List<String> getSaveTitles(){
        GameSave gameSave = gameSavesDao.get(2);
        List <GameSave> allSaves = gameSavesDao.getAll();
        List <String> saveTitles = new ArrayList<>();
        allSaves.forEach(save -> {
            saveTitles.add(save.getTitle());
        });
        return saveTitles;
    }

    public void updatePlayer(PlayerModel playerModel){
        playerDao.update(playerModel);
    }

    public void addToGameSaves(String title, PlayerModel playerModel, GameState gameState){
        gameSavesDao.add(new GameSave(title, gameState, playerModel));
    }

    public GameState saveGameState(String currentMap, PlayerModel playerModel) {
        Date currentDate = new Date(System.currentTimeMillis());
        GameState gameState = new GameState(currentMap, currentDate, playerModel);
        gameState = gameStateDao.add(gameState);
        return gameState;
    };

    public PlayerModel savePlayer(Player player) {
        PlayerModel model = new PlayerModel(player);
        model = playerDao.add(model);
        return model;
    }

    public int getHighestPlayerId() {
        ArrayList <Integer> idList = playerDao.getIdList();
        if (idList.size() == 0) return 0;
        return Collections.max(playerDao.getIdList());
    }

    private DataSource connect() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String dbName = System.getenv("DB_NAME");
        String user = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        System.out.println("Trying to connect");
        dataSource.getConnection().close();
        System.out.println("Connection ok.");

        return dataSource;
    }
}
