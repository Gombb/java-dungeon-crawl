package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameSave;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameSavesDaoJdbc implements GameSavesDao {

    private DataSource dataSource;

    public GameSavesDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public GameSave add(GameSave gamesave) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO game_saves (save_title, player_id, game_state_id) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, gamesave.getTitle());
            statement.setInt(2, gamesave.getPlayerId());
            statement.setInt(3, gamesave.getGameStateId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            gamesave.setId(resultSet.getInt(1));
            return gamesave;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameSave get(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT save_title, player_id, game_state_id FROM game_saves WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }else {
                GameSave gameSave = new GameSave(resultSet.getString(1));
                gameSave.setPlayerId(resultSet.getInt(2));
                gameSave.setGameStateId(resultSet.getInt(3));
                return gameSave;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameSave> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT save_title, player_id, game_state_id FROM game_saves";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            List <GameSave> gameSaveList = new ArrayList<>();
            while (!resultSet.next()){
                GameSave gameSave = new GameSave(resultSet.getString(1));
                gameSave.setPlayerId(resultSet.getInt(2));
                gameSave.setGameStateId(resultSet.getInt(3));
                gameSaveList.add(gameSave);
            }
            return gameSaveList;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
