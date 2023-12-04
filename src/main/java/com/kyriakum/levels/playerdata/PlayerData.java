package com.kyriakum.levels.playerdata;

import com.kyriakum.levels.api.PlayerExpChangeEvent;
import com.kyriakum.levels.api.PlayerLevelUpEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@AllArgsConstructor
public final class PlayerData {

    private static final int BASE = 100;
    private static final double MULTIPLIER = 1.5;

    @Getter
    private final @NotNull UUID uuid;
    @Setter
    @Getter
    private int level;
    @Setter
    @Getter
    private int exp;

    public @NotNull UUID getUUID() {
        return uuid;
    }


    public void addExp(int amount){

        PlayerExpChangeEvent event = new PlayerExpChangeEvent(this);
        Bukkit.getPluginManager().callEvent(event);

        if(getRemainingExpForNext()-amount<0){
            level++;
            exp = Math.abs(getRemainingExpForNext()-amount);
            PlayerLevelUpEvent event1 = new PlayerLevelUpEvent(this);
            Bukkit.getPluginManager().callEvent(event1);
        } else exp+=amount;
    }

    public int getTotalExpForNext(){
        return (int)((double)BASE*Math.pow(MULTIPLIER,level-1));
    }

    public int getRemainingExpForNext(){
        return getTotalExpForNext()-exp;
    }

    public float getBarExpDisplayFloat(){
        int nextLevel = getTotalExpForNext();
       return ((float) exp / nextLevel);
    }
}
