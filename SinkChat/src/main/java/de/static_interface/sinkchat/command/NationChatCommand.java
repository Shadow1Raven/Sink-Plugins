/*
 * Copyright (c) 2013 - 2014 http://static-interface.de and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.static_interface.sinkchat.command;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import de.static_interface.sinkchat.SinkChat;
import de.static_interface.sinkchat.TownyHelper;
import de.static_interface.sinklibrary.api.command.SinkCommand;
import de.static_interface.sinklibrary.api.command.annotation.Aliases;
import de.static_interface.sinklibrary.api.command.annotation.Description;
import de.static_interface.sinklibrary.api.exception.NotEnoughArgumentsException;
import de.static_interface.sinklibrary.configuration.LanguageConfiguration;
import de.static_interface.sinklibrary.util.BukkitUtil;
import org.apache.commons.cli.ParseException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

@Description("Chat Channel for Towny Nations")
@Aliases("tnc")
public class NationChatCommand extends SinkCommand implements CommandExecutor {

    public NationChatCommand(@Nonnull Plugin plugin) {
        super(plugin);
    }

    @Override
    protected boolean onExecute(CommandSender sender, String label, String[] args) throws ParseException {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        Resident resident = TownyHelper.getResident(player.getName());

        if (!resident.hasTown()) {
            player.sendMessage(LanguageConfiguration.SC_TOWNY_NOT_IN_TOWN.format());
            return true;
        }

        if (!resident.hasNation()) {
            player.sendMessage(LanguageConfiguration.SC_TOWNY_NOT_IN_NATION.format());
            return true;
        }

        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }

        Nation nation;
        Town town;
        try {
            town = resident.getTown();
            nation = town.getNation();
        } catch (NotRegisteredException ignored) //Shouldn't happen...
        {
            return true;
        }

        String msg = "";
        for (String arg : args) {
            msg += arg + ' ';
        }

        msg = msg.trim();

        String prefixName = TownyHelper.getFormattedResidentName(resident, false, true);

        String townPrefix = ChatColor.GREEN + "[" + TownyHelper.getFormattedTownName(town, true) + "] ";

        String
                formattedMessage =
                ChatColor.GRAY + "[" + ChatColor.GOLD + nation.getName() + ChatColor.GRAY + "] " + townPrefix + prefixName + ChatColor.GRAY + ": "
                + ChatColor.WHITE + msg;

        List<Player> sendPlayers = new ArrayList<>();

        for (Resident nationResident : nation.getResidents()) {
            if (nationResident.isNPC()) {
                continue;
            }
            Player onlineResident = BukkitUtil.getPlayer(nationResident.getName());
            if (onlineResident == null) {
                continue;
            }
            sendPlayers.add(onlineResident);
        }

        for (Player onlinePlayer : BukkitUtil.getOnlinePlayers()) {
            if (!onlinePlayer.hasPermission("sinkchat.townyspy")) {
                continue;
            }
            if (sendPlayers.contains(onlinePlayer)) {
                continue;
            }
            sendPlayers.add(onlinePlayer);
        }

        for (Player p : sendPlayers) {
            p.sendMessage(formattedMessage);
        }

        SinkChat.getInstance().getLogger().info(formattedMessage);
        return true;
    }
}
