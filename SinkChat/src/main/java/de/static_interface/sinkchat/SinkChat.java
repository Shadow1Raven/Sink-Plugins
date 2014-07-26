/*
 * Copyright (c) 2014 http://adventuria.eu, http://static-interface.de and contributors
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.static_interface.sinkchat;

import com.palmergames.bukkit.towny.Towny;
import de.static_interface.sinkchat.channel.Channel;
import de.static_interface.sinkchat.channel.ChannelHandler;
import de.static_interface.sinkchat.channel.ChannelValues;
import de.static_interface.sinkchat.command.*;
import de.static_interface.sinkchat.listener.ChatListenerHighest;
import de.static_interface.sinkchat.listener.ChatListenerLowest;
import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.exceptions.NotInitializedException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class SinkChat extends JavaPlugin
{
    private static boolean initialized = false;
    private static Towny towny;

    public void onEnable()
    {
        if ( !checkDependencies() )
        {
            return;
        }

        if ( !initialized )
        {
            registerEvents();
            registerCommands();
            registerChannels();
            SinkLibrary.registerPlugin(this);
            initialized = true;
        }
    }

    private void registerChannels()
    {
        ChannelConfigurations config = new ChannelConfigurations();
        YamlConfiguration yamlConfig = config.getYamlConfiguration();
        ConfigurationSection section = yamlConfig.getConfigurationSection("Channels");
        for (String key : section.getKeys(false))
        {
            String pathPrefix = "Channels." + key + ".";

            String callChar = (String) config.get(pathPrefix + ChannelValues.CALLCHAR);
            boolean enabled = (boolean) config.get(pathPrefix + ChannelValues.ENABLED);
            String permission = (String) config.get(pathPrefix + ChannelValues.PERMISSION);
            String prefix = (String) config.get(pathPrefix + ChannelValues.PREFIX);
            boolean sendToIRC = (boolean) config.get(pathPrefix + ChannelValues.SEND_TO_IRC);
            int range = (int) config.get(pathPrefix + ChannelValues.RANGE);

            Channel channel = new Channel(key, callChar, enabled, permission, prefix, sendToIRC, range);
            ChannelHandler.registerChannel(channel);
        }
    }

    public void onDisable()
    {
        System.gc();
    }

    public static boolean isTownyAvailable()
    {
        return towny != null;
    }

    public static Towny getTowny()
    {
        return towny;
    }

    private boolean checkDependencies()
    {
        if ( Bukkit.getPluginManager().getPlugin("SinkLibrary") == null )
        {
            getLogger().log(Level.WARNING, "This Plugin requires SinkLibrary!");
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }

        Plugin tmp = Bukkit.getPluginManager().getPlugin("Towny");
        if ( tmp != null && tmp instanceof Towny )
        {
            towny = (Towny) tmp;
        }
        else
        {
            towny = null;
            SinkLibrary.getCustomLogger().log(Level.INFO, "Towny not found. Disabling Towny Chat features");
        }

        if ( !SinkLibrary.initialized )
        {
            throw new NotInitializedException();
        }
        return true;
    }

    private void registerEvents()
    {
        Bukkit.getPluginManager().registerEvents(new ChatListenerLowest(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListenerHighest(), this);
    }

    private void registerCommands()
    {
        getCommand("nick").setExecutor(new NickCommand());
        getCommand("channel").setExecutor(new ChannelCommand());
        getCommand("enablespy").setExecutor(new SpyCommands.EnableSpyCommand());
        getCommand("disablespy").setExecutor(new SpyCommands.DisablSpyCommand());
        if ( towny != null )
        {
            getCommand("nationchat").setExecutor(new NationChatCommand());
            getCommand("townchat").setExecutor(new TownChatCommand());
        }
    }
}