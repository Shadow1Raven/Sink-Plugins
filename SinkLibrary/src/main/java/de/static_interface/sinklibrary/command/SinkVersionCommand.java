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

package de.static_interface.sinklibrary.command;

import de.static_interface.sinklibrary.api.command.SinkCommand;
import de.static_interface.sinklibrary.api.command.annotation.Aliases;
import de.static_interface.sinklibrary.api.command.annotation.Description;
import de.static_interface.sinklibrary.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.List;

@Description("Shows version and license info")
@Aliases("sversion")
public class SinkVersionCommand extends SinkCommand {

    public static final String PREFIX = ChatColor.BLUE + "[SinkLibrary] " + ChatColor.RESET;

    public SinkVersionCommand(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onExecute(CommandSender sender, String label, String[] args) {
        List<String> authorsList = getPlugin().getDescription().getAuthors();
        String authors = StringUtil.formatPlayerListToString(authorsList);
        sender.sendMessage(PREFIX + getPlugin().getDescription().getName() + " by " + authors);
        sender.sendMessage(PREFIX + "Version: " + getPlugin().getDescription().getVersion());
        sender.sendMessage(PREFIX + "Copyright © 2013 - 2014 static-interface.de");
        sender.sendMessage(PREFIX + "Source code is available at https://github.com/Static-Interface/Sink-Plugins");
        return true;
    }
}
