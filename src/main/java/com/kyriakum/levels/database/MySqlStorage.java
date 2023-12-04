package com.kyriakum.levels.database;

import com.kyriakum.levels.Levels;
import com.kyriakum.levels.playerdata.PlayerData;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class MySqlStorage implements DataStorage {

    private static final @NotNull HikariDataSource dataSource;

    static {
        HikariConfig hikariConfig = new HikariConfig();
        FileConfiguration config = Levels.getLevels().getConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + config.getString("mysql.host") + ":" + config.getInt("mysql.port") + "/" + config.getString("mysql.database"));
        hikariConfig.setUsername(config.getString("mysql.username"));
        hikariConfig.setPassword(config.getString("mysql.password"));
        hikariConfig.setMaximumPoolSize(10);

        dataSource = new HikariDataSource(hikariConfig);
    }

    public MySqlStorage(){
        setup();
    }
    private void setup(){
         String SQL = "CREATE TABLE IF NOT EXISTS LEVELS(uuid VARCHAR(36) NOT NULL PRIMARY KEY, level INT NOT NULL, exp INT NOT NULL)";

        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()){
            statement.executeUpdate(SQL);

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull CompletableFuture<Optional<PlayerData>> retrieve(@NotNull UUID uuid) {
        CompletableFuture<Optional<PlayerData>> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            String SQL = "SELECT * FROM LEVELS WHERE uuid = ?";
            Optional<PlayerData> playerData1 = Optional.empty();
            try(Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL)) {
                statement.setString(1, uuid.toString());
                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next()){
                    int level = resultSet.getInt("level");
                    int exp = resultSet.getInt("exp");
                   playerData1 = Optional.of(new PlayerData(uuid, level, exp));
                }
                resultSet.close();
                future.complete(playerData1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return future;
    }

    public void disable(){if(dataSource!=null) dataSource.close();}


    @Override
    public @NotNull CompletableFuture<Void> save(@NotNull PlayerData playerData) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync( () -> {
            String sql = "INSERT INTO LEVELS (uuid,level,exp) VALUES (?,?,?) ON DUPLICATE KEY UPDATE level = VALUES(level), exp = VALUES(exp)";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, playerData.getUUID().toString());
                preparedStatement.setInt(2, playerData.getLevel());
                preparedStatement.setInt(3, playerData.getExp());
                preparedStatement.executeUpdate();
                future.complete(null);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return future;
    }
}
