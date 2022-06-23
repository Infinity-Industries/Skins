package me.nes0x.skins.command;

import me.nes0x.skins.Skins;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.StringUtil;

import java.util.*;

public class GiveSkinTabCompleter implements TabCompleter {
    private final Skins main;

    public GiveSkinTabCompleter(final Skins main) {
        this.main = main;
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String s, final String[] args) {
        FileConfiguration config = main.getConfig();
        if (!commandSender.hasPermission("skins.commands.giveskin")) {
            return new ArrayList<>();
        }

        if (args.length == 1) {
            List<String> players = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(player -> players.add(player.getName()));
            return StringUtil.copyPartialMatches(args[0], new ArrayList<>(), players);
        } else if (args.length == 2) {
            Set<String> categories = new TreeSet<>();
            config.getStringList("skins").forEach(
                    skin -> {
                        String[] skinSplit = skin.split(":");
                        categories.add(skinSplit[2]);
                    }
            );

            return StringUtil.copyPartialMatches(args[1], new ArrayList<>(), new ArrayList<>(categories));
        } else if (args.length == 3) {
            List<String> skins = new ArrayList<>();
            config.getStringList("skins").forEach(
                    skin -> {
                        String[] skinSplit = skin.split(":");
                        if (skinSplit[2].equalsIgnoreCase(args[1])) {
                            skins.add(skinSplit[0]);
                        }
                    }
            );
            return StringUtil.copyPartialMatches(args[2], new ArrayList<>(), skins);
        } else if (args.length == 4) {
            List<String> materials = new ArrayList<>();
            Arrays.stream(Material.values()).forEach(material ->
                    materials.add(material.name()));
            return StringUtil.copyPartialMatches(args[3], new ArrayList<>(), materials);
        }

        return new ArrayList<>();
    }
}
