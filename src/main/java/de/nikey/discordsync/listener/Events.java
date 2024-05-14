package de.nikey.discordsync.listener;

import de.nikey.discordsync.DiscordSync;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {
    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        Component message = event.message();
        String serialize = PlainTextComponentSerializer.plainText().serialize(message);
        if (serialize.contains("@everyone")) {
            serialize.replaceAll("@everyone" , "everyone");
        } else if (serialize.contains("@here")) {
            serialize.replaceAll("@here" , "here");
        }
        DiscordSync.sendMessage("**<"+ event.getPlayer().getName()+ ">** " + serialize);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        DiscordSync.sendMessage("**<"+event.getPlayer().getName()+">** "+event.getMessage());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        DiscordSync.sendMessage("__**" + event.getPlayer().getName() + "** joined (" + Bukkit.getOnlinePlayers().size()+"/"+ Bukkit.getServer().getMaxPlayers()+ ")__" );
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        int online = Bukkit.getOnlinePlayers().size()-1;
        DiscordSync.sendMessage("__**" + event.getPlayer().getName() + "** left (" + online +"/"+ Bukkit.getServer().getMaxPlayers()+ ")__" );
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String deathMessage = event.getDeathMessage() == null ? player.getDisplayName() + " died" : event.getDeathMessage();
        DiscordSync.sendMessage(deathMessage);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event) {
        String advansmentKey = event.getAdvancement().getKey().getKey();
        String display = DiscordSync.advancmentToDisplayMap.get(advansmentKey);
        if (display == null) return;

        DiscordSync.sendMessage(event.getPlayer().getDisplayName() + " has made the advancement ["+display+"]");

    }
}
