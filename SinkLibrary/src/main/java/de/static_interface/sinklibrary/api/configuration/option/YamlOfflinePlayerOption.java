/*
 * Copyright (c) 2013 - 2015 http://static-interface.de and contributors
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

package de.static_interface.sinklibrary.api.configuration.option;

import org.bukkit.OfflinePlayer;

import javax.annotation.Nullable;

public class YamlOfflinePlayerOption extends YamlOption<OfflinePlayer> {

    public YamlOfflinePlayerOption(String path, OfflinePlayer defaultValue) {
        super(path, defaultValue);
    }

    public YamlOfflinePlayerOption(String path, OfflinePlayer defaultValue, String comment) {
        super(path, defaultValue, comment);
    }

    public YamlOfflinePlayerOption(@Nullable YamlParentOption parent, String path, OfflinePlayer defaultValue) {
        super(parent, path, defaultValue);
    }

    public YamlOfflinePlayerOption(@Nullable YamlParentOption parent, String path, OfflinePlayer defaultValue,
                                   @Nullable String comment) {
        super(parent, path, defaultValue, comment);
    }

    @Override
    public OfflinePlayer getValue() {
        return getConfig().getYamlConfiguration().getOfflinePlayer(getPath(), getDefaultValue());
    }
}
