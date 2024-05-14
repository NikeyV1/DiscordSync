package de.nikey.discordsync;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;

public class Events implements Listener {
    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        DiscordSync.sendMessage(event.getPlayer(), String.valueOf(event.message()),false, Color.GRAY);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        DiscordSync.sendMessage(event.getPlayer(),event.getPlayer().getDisplayName() + " joined" , true, Color.YELLOW);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        DiscordSync.sendMessage(event.getPlayer(),event.getPlayer().getDisplayName() + " left" , true, Color.YELLOW);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String deathMessage = event.getDeathMessage() == null ? player.getDisplayName() + " died" : event.getDeathMessage();
        DiscordSync.sendMessage(player,deathMessage,true,Color.RED);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event) {
        String advansmentKey = event.getAdvancement().getKey().getKey();
        String display = DiscordSync.advancmentToDisplayMap.get(advansmentKey);
        if (display == null) return;

        DiscordSync.sendMessage(event.getPlayer(),event.getPlayer().getDisplayName() + " has made the advancement ["+display+"]",true,Color.GREEN);

    }
}
