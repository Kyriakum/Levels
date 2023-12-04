package com.kyriakum.levels.listeners;

import com.kyriakum.levels.Levels;
import com.kyriakum.levels.playerdata.PlayerData;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
public final class PlayerConnectionListener implements Listener {

    private final @NotNull Levels levels;

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent e){
        UUID uuid = e.getUniqueId();
        levels.getDataStorage().retrieve(uuid).whenComplete((opt, exception) -> {
            if(exception != null){
                Bukkit.getLogger().warning(exception.getMessage());
            }else
            {
                opt.ifPresentOrElse(
                        playerData -> {
                            synchronized (levels.getPlayerManager()){
                                levels.getPlayerManager().addPlayer(playerData);
                            }
                        },
                        () -> {
                            PlayerData playerData = new PlayerData(uuid, 1, 10);
                            synchronized (levels.getPlayerManager()){
                                levels.getPlayerManager().addPlayer(playerData);
                            }
                             levels.getDataStorage().save(playerData);
                        }
                );
            }
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        synchronized (levels.getPlayerManager()) {
            levels.getPlayerManager().getPlayerData(player).ifPresentOrElse(data -> {
                player.setLevel(data.getLevel());
                player.setExp(data.getBarExpDisplayFloat());

            }, () -> {
                throw new NullPointerException();
            });
        }

    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        levels.getPlayerManager().getPlayerData(e.getPlayer()).ifPresentOrElse(data -> {
                levels.getDataStorage().save(data);
            levels.getPlayerManager().removePlayer(data);
        }, () -> {
            throw new NullPointerException();
        });
    }
}
