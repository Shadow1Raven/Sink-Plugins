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

package de.static_interface.sinkchat;

import static de.static_interface.sinklibrary.configuration.LanguageConfiguration.*;

import de.static_interface.sinklibrary.*;
import de.static_interface.sinklibrary.configuration.*;
import de.static_interface.sinklibrary.user.*;
import de.static_interface.sinklibrary.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class Util {

    public static void sendMessage(IngameUser user, String message, int range) {
        for (Player p : BukkitUtil.getOnlinePlayers()) {

            if (p.getWorld() != user.getPlayer().getWorld()) {
                continue;
            }

            boolean isInRange = range > 0 && user.getDistance(p) <= range;
            IngameUser onlineUser = SinkLibrary.getInstance().getIngameUser(p);

            // Check for spy
            boolean canSpy = onlineUser.hasPermission("sinkchat.spy.all") || (onlineUser.hasPermission("sinkchat.spy")
                                                                              && !user.hasPermission("sinkchat.spy.bypass"));

            IngameUserConfiguration config = onlineUser.getConfiguration();

            if (isInRange) {
                p.sendMessage(message);
            } else if (canSpy && config.isSpyEnabled()) {
                p.sendMessage(getSpyPrefix() + message);
            }
        }
    }

    public static String getSpyPrefix() {
        return m("SinkChat.Prefix.Spy") + ' ' + ChatColor.RESET;
    }
}
