/*
 * Copyright (c) 2013 adventuria.eu / static-interface.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.static_interface.sinkchat.command;

import de.static_interface.sinkchat.channel.ChannelHandler;
import de.static_interface.sinkchat.channel.IChannel;
import de.static_interface.sinkchat.channel.configuration.LanguageConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ChannelCommand extends JavaPlugin implements CommandExecutor
{
    public static final String PREFIX = ChatColor.GREEN + "[Channel] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {

        if (args.length == 0)
        {
            sendHelp(sender);
            return true;
        }

        String message;


        switch (args[0])
        {
            case "join":

                if (args.length < 2)
                {
                    sender.sendMessage(PREFIX + LanguageConfiguration.getLanguageString("messages.noChannelGiven"));
                    return true;
                }

                try
                {
                    ChannelHandler.getRegisteredChannel(args[1]).removeExceptedPlayer((Player) sender);
                }
                catch (NullPointerException e)
                {
                    message = PREFIX + LanguageConfiguration.getLanguageString("messages.channelUnknown").replace("$CHANNEL$", args[1]);
                    sender.sendMessage(message);
                    return true;
                }

                message = PREFIX + LanguageConfiguration.getLanguageString("messages.playerJoins").replace("$CHANNEL$", args[1]);
                ChatColor.translateAlternateColorCodes('&', message);
                sender.sendMessage(message);


                return true;
            case "leave":

                if (args.length < 2)
                {
                    sender.sendMessage(PREFIX + LanguageConfiguration.getLanguageString("messages.noChannelGiven"));
                    return true;
                }

                try
                {
                    ChannelHandler.getRegisteredChannel(args[1]).addExceptedPlayer((Player) sender);
                }
                catch (NullPointerException e)
                {   //Note: Do this more clean...
                    message = PREFIX + LanguageConfiguration.getLanguageString("messages.channelUnknown").replace("$CHANNEL$", args[1]);
                    sender.sendMessage(message);
                    return true;
                }

                message = PREFIX + LanguageConfiguration.getLanguageString("messages.playerLeaves").replace("$CHANNEL$", args[1]);
                sender.sendMessage(message);


                return true;
            case "list":
                message = PREFIX + LanguageConfiguration.getLanguageString("messages.list").replace("$CHANNELS$", ChannelHandler.getChannelNames());
                sender.sendMessage(message);
                return true;

            case "participating":
                sender.sendMessage(PREFIX + LanguageConfiguration.getLanguageString("messages.part"));
                for (IChannel target : ChannelHandler.getRegisteredChannels())
                {
                    if (target.contains((Player) sender))
                    {
                        continue;
                    }

                    sender.sendMessage(PREFIX + target.getChannelName());

                }
                return true;

            default:
                sendHelp(sender);
                return true;
        }
    }

    private static void sendHelp(CommandSender sender)
    {
        sender.sendMessage(PREFIX + LanguageConfiguration.getLanguageString("messages.help"));
        sender.sendMessage(PREFIX + "/ch join <channel>");
        sender.sendMessage(PREFIX + "/ch leave <channel>");
        sender.sendMessage(PREFIX + "/ch list");
        sender.sendMessage(PREFIX + "/ch participating");
    }
}
