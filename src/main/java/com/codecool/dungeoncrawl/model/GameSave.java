package com.codecool.dungeoncrawl.model;

public class GameSave extends BaseModel {
    private String title;
    private int playerId;
    private int gameStateId;

    public GameSave(String title){
        this.title = title;
    }

    public GameSave(String title, GameState gameState, PlayerModel playerModel){
        this.title = title;
        this.playerId = playerModel.getId();
        this.gameStateId = gameState.getId();
    }

    public int getGameStateId() {
        return gameStateId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setGameStateId(int gameStateId) {
        this.gameStateId = gameStateId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
}
