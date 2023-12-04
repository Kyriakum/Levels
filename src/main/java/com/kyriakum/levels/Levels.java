package com.kyriakum.levels;

import com.kyriakum.levels.database.DataStorage;
import com.kyriakum.levels.database.MySqlStorage;
import com.kyriakum.levels.listeners.LevelsListener;
import com.kyriakum.levels.listeners.PlayerConnectionListener;
import com.kyriakum.levels.managers.PlayerManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;


public final class Levels extends JavaPlugin {

    @Getter
    private static Levels levels;
    @Getter
    private DataStorage dataStorage;
    @Getter
    private PlayerManager playerManager;

    @Override
    public void onEnable() {

        this.saveDefaultConfig();

        if(!getConfig().getBoolean("mysql.enabled")){
            Bukkit.getLogger().info("LEVELS >> Please enable MySQL in the config.yml!");
            this.setEnabled(false);
            return;
        }

        levels = this;
        dataStorage = new MySqlStorage();
        playerManager = new PlayerManager();
        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new LevelsListener(), this);
        loadOnlinePlayers();
    }
    @Override
    public void onDisable() {

        if(!getConfig().getBoolean("enabled")) return;

        saveOnlinePlayers();
        dataStorage.disable();
    }

    private void saveOnlinePlayers(){
        playerManager.getPlayerDataList().forEach(t->dataStorage.save(t).join());
    }

    private void loadOnlinePlayers(){
        for(Player player : Bukkit.getOnlinePlayers()){
                dataStorage.retrieve(player.getUniqueId()).thenAcceptAsync(opt -> {
                opt.ifPresentOrElse(playerManager::addPlayer, player::kick);
            }, task -> Bukkit.getScheduler().runTask(this, task));
        }
    }
}
