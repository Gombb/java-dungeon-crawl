package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerDaoJdbc implements PlayerDao {
    private DataSource dataSource;

    public PlayerDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(PlayerModel player) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO player (player_name, hp, x, y, defense, attack) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, player.getPlayerName());
            statement.setInt(2, player.getHp());
            statement.setInt(3, player.getX());
            statement.setInt(4, player.getY());
            statement.setInt(5, player.getDefense());
            statement.setInt(6, player.getAttack());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            player.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(PlayerModel player) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE player SET hp = ?, x = ?, y = ?, defense = ?, attack = ? where id = ?";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, player.getHp());
            statement.setInt(2, player.getX());
            statement.setInt(3, player.getY());
            statement.setInt(4, player.getDefense());
            statement.setInt(5, player.getAttack());
            statement.setInt(6, player.getId());
            statement.executeUpdate();
        } catch (SQLException e ){
            throw new RuntimeException(e);
        }

    }

    @Override
    public ArrayList<Integer> getIdList(){
        ArrayList<Integer> idList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT id FROM player";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                idList.add(resultSet.getInt(1));
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return idList;
    }

    @Override
    public PlayerModel get(int id) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT player_name, hp, x, y FROM player WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (! resultSet.next()){
                return null;
            }else{
                PlayerModel playerModel = new PlayerModel(resultSet.getString(2), resultSet.getInt(4), resultSet.getInt(5));
                return playerModel;
            }


        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PlayerModel> getAll() {
        return null;
    }
}
