/*
 * Copyright (c) 2013 - 2014 http://adventuria.eu, http://static-interface.de and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.static_interface.sinkirc.irc_commands;

import de.static_interface.sinklibrary.BukkitUtil;
import de.static_interface.sinklibrary.SinkLibrary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ListCommand extends IrcCommand
{
    public ListCommand(Plugin plugin)
    {
        super(plugin);
    }

    @Override
    public boolean onExecute(CommandSender sender, String label, String[] args)
    {
        if ( BukkitUtil.getOnlinePlayers().size() == 0 )
        {
            sender.sendMessage("There are currently no online players");
            return true;
        }

        String onlineMessage = "Online Players (" + BukkitUtil.getOnlinePlayers().size() + '/' + Bukkit.getMaxPlayers() + "): ";

        boolean firstPlayer = true;
        for ( Player player : BukkitUtil.getOnlinePlayers() )
        {
            de.static_interface.sinklibrary.User user = SinkLibrary.getUser(player);
            if ( firstPlayer )
            {
                onlineMessage += user.getDisplayName();
                firstPlayer = false;
            }
            else
            {
                if ( onlineMessage.length() > 0 )
                {
                    onlineMessage += ChatColor.RESET + ", ";
                }

                onlineMessage += user.getDisplayName();
            }

            // standard length: 512 (inclusive headers)
            if ( onlineMessage.length() > 400 )
            {
                sender.sendMessage(onlineMessage);
                onlineMessage = "";
            }
        }

        if ( onlineMessage.length() > 0 )
        {
            sender.sendMessage(onlineMessage);
        }

        return true;
    }
}
