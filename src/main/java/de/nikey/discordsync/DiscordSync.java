package de.nikey.discordsync;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class DiscordSync extends JavaPlugin {

    private JDA jda;
    public static TextChannel chatChannel;

    public static final Map<String,String> advancmentToDisplayMap = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();


        String botToken = getConfig().getString("bot-token");
        getLogger().info("Token: "+botToken);
        try {
            jda = JDABuilder.createDefault(botToken)
                    .build()
                    .awaitReady();
        }catch ( NoClassDefFoundError|InterruptedException | LoginException e) {
            e.printStackTrace();
        }

        if (jda == null) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        String channelID = getConfig().getString("chat-channel-id");
        if (channelID != null) {
            chatChannel = jda.getTextChannelById(channelID);
        }
        getLogger().info("Channel ID: "+channelID);


        ConfigurationSection advancementMap = getConfig().getConfigurationSection("advancementMap");
        if (advancementMap != null) {
            for (String key : advancementMap.getKeys(false)) {
                advancmentToDisplayMap.put(key,advancementMap.getString(key));
            }
        }

        jda.addEventListener(new DiscordListener());

        getServer().getPluginManager().registerEvents(new Events(),this);
    }

    @Override
    public void onDisable() {
        if (jda != null) jda.shutdownNow();
    }

    public static void sendEmbed(Player player, String content, boolean contentInAuthorLine, Color color) {
        if (chatChannel == null)return;

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor(
                        contentInAuthorLine ? content : player.getDisplayName(),null,
                        "https://crafatar.com/avatars/"+player.getUniqueId()+"?overlay=1"
                );

        if (!contentInAuthorLine) {
            embedBuilder.setDescription(content);
        }

        chatChannel.sendMessage(embedBuilder.build()).queue();
    }

    public static void sendMessage(String content) {
        if (chatChannel == null)return;
        chatChannel.sendMessage(content).queue();
    }
}