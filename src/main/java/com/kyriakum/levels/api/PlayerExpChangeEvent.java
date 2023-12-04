package com.kyriakum.levels.api;

import com.kyriakum.levels.playerdata.PlayerData;
import com.kyriakum.levels.utils.OptionalPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class PlayerExpChangeEvent extends Event {
    private static final @NotNull HandlerList handlerList = new HandlerList();
    @Getter
    private final @NotNull Player player;
    @Getter
    private final @NotNull PlayerData playerData;

    public PlayerExpChangeEvent(@NotNull PlayerData playerData){
        this.playerData = playerData;
        if(OptionalPlayer.getPlayer(playerData.getUUID()).isEmpty()) throw new NullPointerException();
        player = OptionalPlayer.getPlayer(playerData.getUUID()).get();
    }

    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
    public static @NotNull HandlerList getHandlerList() {
        return handlerList;
    }
}
