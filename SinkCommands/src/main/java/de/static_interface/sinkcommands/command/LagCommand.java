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

package de.static_interface.sinkcommands.command;

import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.api.command.SinkCommand;
import de.static_interface.sinklibrary.api.command.annotation.DefaultPermission;
import de.static_interface.sinklibrary.api.command.annotation.Description;
import de.static_interface.sinklibrary.api.configuration.Configuration;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.text.DecimalFormat;

@DefaultPermission
@Description("Shows information about server lag")
public class LagCommand extends SinkCommand {

    public static final String PREFIX = ChatColor.DARK_PURPLE + "[Lag] " + ChatColor.RESET;

    public LagCommand(Plugin plugin, Configuration config) {
        super(plugin, config);
    }

    @Override
    public boolean onExecute(CommandSender sender, String label, String[] args) {
        double realTPS = SinkLibrary.getInstance().getSinkTimer().getAverageTPS();
        DecimalFormat decimalFormat = new DecimalFormat("##.0");
        String shownTPS = decimalFormat.format(realTPS);
        if (realTPS >= 18.5) {
            sender.sendMessage(PREFIX + ChatColor.GREEN + "Der Server läuft ohne Probleme!");
        } else if (realTPS >= 17) {
            sender.sendMessage(PREFIX + ChatColor.YELLOW + "Der Server könnte gerade etwas laggen!");
        } else {
            sender.sendMessage(PREFIX + ChatColor.RED + "Der Server laggt gerade!");
        }
        sender.sendMessage(PREFIX + "(TPS: " + shownTPS + ')');
        return true;
    }
}

