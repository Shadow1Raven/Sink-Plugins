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

package de.static_interface.sinkirc;

import de.static_interface.sinklibrary.BukkitUtil;
import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.events.*;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.pircbotx.Channel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class IrcListener implements Listener
{
    public static final String IRC_PREFIX = ChatColor.GRAY + "[IRC] " + ChatColor.RESET;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        String message = event.getJoinMessage();
        if ( message == null || message.isEmpty() )
        {
            return;
        }
        IrcUtil.sendMessage(SinkIRC.getMainChannel(), message);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        String message = event.getQuitMessage();
        if ( message == null || message.isEmpty() )
        {
            return;
        }
        IrcUtil.sendMessage(SinkIRC.getMainChannel(), message);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerKick(PlayerKickEvent event)
    {
        String reason = ": " + event.getReason();
        if ( event.getReason().isEmpty() ) reason = "!";
        de.static_interface.sinklibrary.User user = SinkLibrary.getUser(event.getPlayer());
        IrcUtil.sendMessage(SinkIRC.getMainChannel(), user.getDisplayName() + ChatColor.RESET + " has been kicked" + reason);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        String message = event.getDeathMessage();
        if ( message == null || message.isEmpty() )
        {
            return;
        }
        IrcUtil.sendMessage(SinkIRC.getMainChannel(), message);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onIRCSendMessage(IrcSendMessageEvent event)
    {
        IrcUtil.sendMessage(SinkIRC.getMainChannel(), event.getMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onIRCJoin(IrcJoinEvent event)
    {
        if ( event.getUser().equals(SinkIRC.getIRCBot().getUserBot()) )
        {
            return;
        }
        event.getChannel().send().message("Willkommen, " + event.getUser().getNick() + '!');
        BukkitUtil.broadcastMessage(IRC_PREFIX + ChatColor.GRAY + '[' + event.getChannel() + "] " + ChatColor.DARK_AQUA + event.getUser().getNick() + ChatColor.WHITE + " ist dem Kanal beigetreten.", false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onIRCKick(IrcKickEvent event)
    {
        String reason = event.getReason();
        if ( reason.equals(event.getRecipient().getNick()) || reason.equals(event.getUser().getNick()))
        {
            reason = "";
        }
        String formattedReason = "Grund: " + reason;
        if ( reason.isEmpty() || reason.equals("\"\"") )
        {
            formattedReason = "";
        }
        BukkitUtil.broadcastMessage(IRC_PREFIX + ChatColor.GRAY + '[' + event.getChannel() + "] " + ChatColor.DARK_AQUA + event.getRecipient().getNick() + ChatColor.WHITE + " wurde von " + event.getUser().getNick() + " aus dem Kanal geworfen. " + formattedReason, false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onIRCNickChange(IrcNickChangeEvent event)
    {
        BukkitUtil.broadcastMessage(IRC_PREFIX + ChatColor.GRAY + '[' + "Server" + "] " + ChatColor.DARK_AQUA + event.getOldNick() + ChatColor.WHITE + " ist jetzt als " + ChatColor.DARK_AQUA + event.getNewNick() + ChatColor.WHITE + " bekannt.", false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onIRCPart(IrcPartEvent event)
    {
        BukkitUtil.broadcastMessage(IRC_PREFIX + ChatColor.GRAY + '[' + event.getChannel().getName() + "] " + ChatColor.DARK_AQUA + event.getUser().getNick() + ChatColor.WHITE + " hat den Kanal verlassen (" + event.getReason() + ").", false);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onIRCPrivateMessage(IrcPrivateMessageEvent event)
    {
        String[] args = event.getMessage().split(" ");
        String cmd = args[0];
        List<String> tmp = new ArrayList<>(Arrays.asList(args));
        tmp.remove(cmd);
        args = tmp.toArray(new String[tmp.size()]);
        IrcUtil.handleCommand(cmd, args, event.getUser().getNick(), event.getUser(), event.getMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onIRCQuit(IrcQuitEvent event)
    {
        String formattedReason = " (" + event.getReason() + ')';
        if ( event.getReason().isEmpty() || event.getReason().equals("\"\"") )
        {
            formattedReason = "";
        }
        BukkitUtil.broadcastMessage(IRC_PREFIX + ChatColor.GRAY + '[' + "Server" + "] " + ChatColor.DARK_AQUA + event.getUser().getNick() + ChatColor.WHITE + " hat den IRC Server verlassen." + formattedReason, false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onIRCReceiveMessage(IrcReceiveMessageEvent event)
    {
        SinkLibrary.getCustomLogger().debug("onIRCReceiveMessage: user: " + event.getUser().getNick() + ", channel: "
                                                + event.getChannel().getName() + ", message: " + event.getMessage());
        String label = event.getMessage();
        Channel channel = event.getChannel();

        if ( (label.toLowerCase().contains("hello") || label.toLowerCase().contains("hi") ||
                label.toLowerCase().contains("huhu") || label.toLowerCase().contains("hallo") ||
                label.toLowerCase().contains("moin") || label.toLowerCase().contains("morgen"))
                && (label.toLowerCase().contains(SinkIRC.getIRCBot().getNick() + ' ')
                || label.toLowerCase().contains(SinkIRC.getIRCBot().getNick())
                || label.toLowerCase().contains(' ' + SinkIRC.getIRCBot().getNick() + ' ')) )
        {
            IrcUtil.sendMessage(channel, "Hallo, " + event.getUser().getNick());
            return;
        }

        if ( !label.toLowerCase().startsWith(IrcUtil.getCommandPrefix()) )
        {
            return;
        }
        label = label.replaceFirst(IrcUtil.getCommandPrefix(), "");
        String[] args = label.split(" ");
        String cmd = args[0];
        List<String> tmp = new ArrayList<>(Arrays.asList(args));
        tmp.remove(0);
        args = tmp.toArray(new String[tmp.size()]);
        IrcUtil.handleCommand(cmd, args, channel.getName(), event.getUser(), label);
    }
}
