package me.nes0x.skins.command;

import me.nes0x.skins.Skins;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static me.nes0x.skins.util.ChatUtil.translateText;


// /skin <category> <name>
public class SkinCommand implements CommandExecutor {
    private final Skins main;

    public SkinCommand(final Skins main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        FileConfiguration config = main.getConfig();
        if (!(sender instanceof Player)) {
            Bukkit.getLogger().info(translateText(config.getString("messages.console-executor")));
            return true;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("skins.commands.skin")) {
            player.sendMessage(translateText(config.getString("messages.no-permission")));
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(translateText(config.getString("messages.invalid-usage")));
            return true;
        }

        for (String skin : config.getStringList("skins")) {
            String[] skinSplit = skin.split(":");
            if (args[0].equalsIgnoreCase(skinSplit[2]) && args[1].equalsIgnoreCase(skinSplit[0])) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.toString().contains(args[0].toUpperCase())) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta.hasCustomModelData() && meta.getCustomModelData() == Integer.parseInt(skinSplit[1])) {
                        player.sendMessage(translateText(config.getString("messages.already-have-same-skin")));
                        return true;
                    }
                    meta.setCustomModelData(Integer.parseInt(skinSplit[1]));
                    item.setItemMeta(meta);
                    player.getInventory().setItemInMainHand(item);
                    player.updateInventory();
                    player.sendMessage(translateText(config.getString("messages.skin-set-success").replace("%name%", args[1])));
                    return true;
                } else {
                    player.sendMessage(translateText(config.getString("messages.skin-set-error")));
                    return true;
                }
            }
        }
        player.sendMessage(translateText(config.getString("messages.unknown-skin")));
        return true;
    }
}
