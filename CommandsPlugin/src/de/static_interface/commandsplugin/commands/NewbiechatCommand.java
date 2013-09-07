package de.static_interface.commandsplugin.commands;

import de.static_interface.commandsplugin.CommandsPlugin;
import de.static_interface.commandsplugin.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class NewbiechatCommand implements CommandExecutor
{
    public static final String PREFIX = ChatColor.YELLOW + "[SupportChat] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }
        String message = Util.formatArrayToString(args, " ");

        CommandsPlugin.broadcast(PREFIX + CommandsPlugin.getSenderName(sender) + ChatColor.WHITE + ": " + message, "commandsplugin.newbiechat");
        return true;
    }
}
