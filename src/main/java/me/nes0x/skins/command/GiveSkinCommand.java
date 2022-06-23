package me.nes0x.skins.command;

import me.nes0x.skins.Skins;
import me.nes0x.skins.util.SkinUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static me.nes0x.skins.util.ChatUtil.translateText;


// /giveskin <player> <category> <name>
public class GiveSkinCommand implements CommandExecutor {
    private final Skins main;

    public GiveSkinCommand(final Skins main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        FileConfiguration config = main.getConfig();
        Player player = null;
        if (sender instanceof Player) {
            player  = (Player) sender;
        }


        if (player != null && !player.hasPermission("skins.commands.giveskin")) {
            player.sendMessage(translateText(config.getString("messages.no-permission")));
            return true;
        }

        if (args.length != 3) {
            if (player != null) {
                player.sendMessage(translateText(config.getString("messages.invalid-usage")));
            }
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            if (player != null) {
                player.sendMessage(translateText(config.getString("messages.unknown-player")));
            }
            return true;
        }

        File targetSkinsFile = SkinUtil.getPlayerSkinsFile(main, target, player);
        if (targetSkinsFile == null) {
            return true;
        }
        YamlConfiguration targetSkins = YamlConfiguration.loadConfiguration(targetSkinsFile);

        for (String skin : config.getStringList("skins")) {
            String[] skinSplit = skin.split(":");
            if (args[1].equalsIgnoreCase(skinSplit[2]) && args[2].equalsIgnoreCase(skinSplit[0])) {
                ItemStack item = new ItemStack(Material.valueOf(skinSplit[3].toUpperCase()));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(args[1]);
                meta.setCustomModelData(Integer.parseInt(skinSplit[1]));
                item.setItemMeta(meta);

                List<ItemStack> skins = new ArrayList<>();
                if (targetSkins.getList("skins") != null && !targetSkins.getList("skins").isEmpty()) {
                    skins = (List<ItemStack>) targetSkins.getList("skins");
                }
                skins.add(item);

                targetSkins.set("skins", skins);
                try {
                    targetSkins.save(targetSkinsFile);
                } catch (IOException exception) {
                    exception.printStackTrace();
                    if (player != null) {
                        player.sendMessage(translateText(config.getString("messages.error")));
                    }
                    return true;
                }
                if (player != null) {
                    player.sendMessage(translateText(config.getString("messages.skin-give-success").replace("%name%", skinSplit[0]).replace("%player%", target.getName())));
                } else {
                    Bukkit.getLogger().info(translateText(config.getString("messages.skin-give-success").replace("%name%", skinSplit[0]).replace("%player%", target.getName())));
                }
                return true;
            }
        }
        if (player != null) {
            player.sendMessage(translateText(config.getString("messages.unknown-skin")));
        }
        return true;
    }
}
