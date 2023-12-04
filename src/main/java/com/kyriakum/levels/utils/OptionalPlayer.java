package com.kyriakum.levels.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public final class OptionalPlayer {

    private OptionalPlayer(){}

    public static @NotNull Optional<Player> getPlayer(UUID uuid){
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }
    public static @NotNull Optional<Player> getPlayer(String name){
        return Optional.ofNullable(Bukkit.getPlayer(name));
    }


}
