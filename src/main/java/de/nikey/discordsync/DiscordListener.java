package de.nikey.discordsync;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class DiscordListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getChannel().equals(DiscordSync.chatChannel)) return;

        Member member = event.getMember();
        if (member == null || member.getUser().isBot()) return;

        String message = event.getMessage().getContentDisplay();

        Bukkit.broadcastMessage(ChatColor.BLUE + "<" + member.getEffectiveName() + ">" + ChatColor.RESET + " " + message);
    }
}
