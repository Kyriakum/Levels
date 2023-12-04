package com.kyriakum.levels.managers;

import com.kyriakum.levels.playerdata.PlayerData;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public final class PlayerManager {

    private final List<PlayerData> playerDataList = new ArrayList<>();

    public @NotNull Optional<PlayerData> getPlayerData(@NotNull Player player){
        for(PlayerData playerData : playerDataList){
            if(playerData.getUUID().equals(player.getUniqueId()))
                return Optional.of(playerData);
        }
        return Optional.empty();
    }

    public void addPlayer(@NotNull PlayerData playerData){
        if(playerDataList.contains(playerData)) return;
        playerDataList.add(playerData);
    }

    public void removePlayer(@NotNull PlayerData playerData){
        playerDataList.remove(playerData);
    }

    public @NotNull List<PlayerData> getPlayerDataList(){
        return new ArrayList<>(playerDataList);
    }


}
