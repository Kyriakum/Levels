package com.kyriakum.levels.listeners;

import com.kyriakum.levels.api.PlayerExpChangeEvent;
import com.kyriakum.levels.api.PlayerLevelUpEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class LevelsListener implements Listener {

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent e){

    }

    @EventHandler
    public void onLevelUp(PlayerLevelUpEvent e){

    }

    @EventHandler
    public void onExpGain(org.bukkit.event.player.PlayerExpChangeEvent e){
        e.setAmount(0);
    }
}
