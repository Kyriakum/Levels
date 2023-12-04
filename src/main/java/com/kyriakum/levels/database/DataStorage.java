package com.kyriakum.levels.database;

import com.kyriakum.levels.playerdata.PlayerData;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface DataStorage {

    CompletableFuture<Optional<PlayerData>> retrieve(@NotNull UUID uuid);

    CompletableFuture<Void> save(@NotNull PlayerData playerData);

    default void disable(){
    //Used only by databases needed to close
    }
}
