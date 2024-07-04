package de.nikey.discordsync;

import de.nikey.discordsync.listener.DiscordListener;
import de.nikey.discordsync.listener.Events;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
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
    private static DiscordSync plugin;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;


        String botToken = getConfig().getString("bot-token");
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

        String ip = getConfig().getString("server-ip");

        sendEmbed("Server is starting!",ip,Color.GREEN);
    }

    @Override
    public void onDisable() {
        String ip = getConfig().getString("server-ip");
        sendEmbed("Server is stopping!",ip,Color.RED);

        try {
            wait(500);
            if (jda != null) jda.shutdownNow();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendEmbed(String content, String ip, Color color) {
        if (chatChannel == null)return;

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(color)
                .setAuthor(
                        content,null,
                        null
                );

        if (!ip.isEmpty()) {
            embedBuilder.setDescription(ip);
        }

        chatChannel.sendMessage(embedBuilder.build()).queue();
    }

    public static void sendMessage(String content) {
        if (chatChannel == null)return;
        chatChannel.sendMessage(content).queue();
    }

    public static DiscordSync getPlugin() {
        return plugin;
    }
}