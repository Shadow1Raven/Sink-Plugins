package de.static_interface.chatplugin.channel.channels;

import de.static_interface.chatplugin.ChatPlugin;
import de.static_interface.chatplugin.channel.Channel;
import de.static_interface.chatplugin.channel.ChannelHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Vector;

public class TradeChannel extends JavaPlugin implements Channel
{

    Vector<Player> exceptedPlayers = new Vector<>();
    private char callByChar = '$';
    String PREFIX = ChatColor.GRAY + "[" + ChatColor.GOLD + "Handel" + ChatColor.GRAY + "]" + ChatColor.RESET;

    public TradeChannel(char callChar)
    {
        callByChar = callChar;
    }

    @Override
    public void addExceptedPlayer(Player player)
    {
        exceptedPlayers.add(player);
    }

    @Override
    public void removeExceptedPlayer(Player player)
    {
        exceptedPlayers.remove(player);
    }


    @Override
    public boolean contains(Player player)
    {
        return ( exceptedPlayers.contains(player) );
    }

    @Override
    public String getChannelName()
    {
        return "Handel";
    }

    @Override
    public void sendMessage(Player player, String message)
    {
        String formattedMessage = message.substring(1);
        if (player.hasPermission("chatplugin.color"))
        {
            formattedMessage = this.PREFIX + " [" + ChatPlugin.getGroup(player) + ChatColor.RESET + "] " + ChatPlugin.getDisplayName(player) + ": " + formattedMessage;
        }
        formattedMessage = ChatColor.translateAlternateColorCodes('&', formattedMessage);

        for (Player target : Bukkit.getOnlinePlayers())
        {
            if (! ( this.contains(target) ))
            {
                target.sendMessage(formattedMessage);
            }
        }
    }

    @Override
    public void registerChannel()
    {
        ChannelHandler.registerChannel(this, "Handel", PREFIX, callByChar);
    }
}
