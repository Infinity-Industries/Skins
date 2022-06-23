package me.nes0x.skins.command;

import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import me.nes0x.skins.Skins;
import me.nes0x.skins.util.SkinUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static me.nes0x.skins.util.ChatUtil.translateText;

public class SkinsCommand implements CommandExecutor {
    private final Skins main;

    public SkinsCommand(final Skins main) {
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
        if (!player.hasPermission("skins.commands.skins")) {
            player.sendMessage(translateText(config.getString("messages.no-permission")));
            return true;
        }


        File playerSkinsFile = SkinUtil.getPlayerSkinsFile(main, player, player);
        if (playerSkinsFile == null) {
            return true;
        }

        YamlConfiguration playerSkins = YamlConfiguration.loadConfiguration(playerSkinsFile);
        List<ItemStack> skins = (List<ItemStack>) playerSkins.getList("skins");
        SGMenu menu = main.getSpiGUI().create(translateText(config.getString("settings.gui-title")), config.getInt("settings.gui-rows"));
        menu.setAutomaticPaginationEnabled(false);

        if (skins != null && !skins.isEmpty()) {
            for (ItemStack skin : skins) {
                SGButton skinButton = new SGButton(skin).withListener((InventoryClickEvent event) -> {
                    if (event.isLeftClick()) {
                        ItemStack item = player.getInventory().getItemInMainHand();
                        if (item.toString().contains(skin.getItemMeta().getDisplayName().toUpperCase())) {
                            ItemMeta meta = item.getItemMeta();
                            if (meta.hasCustomModelData() && meta.getCustomModelData() == skin.getItemMeta().getCustomModelData()) {
                                player.sendMessage(translateText(config.getString("messages.already-have-same-skin")));
                                return;
                            }
                            skins.remove(skin);
                            playerSkins.set("skins", skins);
                            try {
                                playerSkins.save(playerSkinsFile);
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }

                            meta.setCustomModelData(skin.getItemMeta().getCustomModelData());
                            item.setItemMeta(meta);
                            player.getInventory().setItemInMainHand(item);
                            player.updateInventory();
                            player.closeInventory();
                            player.sendMessage(translateText(config.getString("messages.skin-received")));
                        } else {
                            player.sendMessage(translateText(config.getString("messages.skin-set-error")));
                        }
                    }

                });
                menu.addButton(skinButton);
            }
        }
        player.openInventory(menu.getInventory());
        return true;
    }
}
