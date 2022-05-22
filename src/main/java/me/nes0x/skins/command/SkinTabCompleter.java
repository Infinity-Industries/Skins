package me.nes0x.skins.command;

import me.nes0x.skins.Skins;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SkinTabCompleter implements TabCompleter {
    private final Skins main;

    public SkinTabCompleter(final Skins main) {
        this.main = main;
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String s, final String[] args) {
        FileConfiguration config = main.getConfig();
        if (!commandSender.hasPermission("skins.commands.skin")) {
            return new ArrayList<>();
        }

        if (args.length == 1) {
            Set<String> categories = new TreeSet<>();
            config.getStringList("skins").forEach(
                    skin -> {
                        String[] skinSplit = skin.split(":");
                        categories.add(skinSplit[2]);
                    }
            );

            return StringUtil.copyPartialMatches(args[0], new ArrayList<>(), new ArrayList<>(categories));
        } else if (args.length == 2) {
            List<String> name = new ArrayList<>();
            config.getStringList("skins").forEach(
                    skin -> {
                        String[] skinSplit = skin.split(":");
                        if (skinSplit[2].equalsIgnoreCase(args[0])) {
                            name.add(skinSplit[0]);
                        }
                    }
            );
            return StringUtil.copyPartialMatches(args[0], new ArrayList<>(), name);
        }

        return new ArrayList<>();
    }
}
