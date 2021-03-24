package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
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
        } catch (SQLException e ){
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getHighestId(){
        try

        return null
    }

    @Override
    public PlayerModel get(int id) {
        return null;
    }

    @Override
    public List<PlayerModel> getAll() {
        return null;
    }
}
