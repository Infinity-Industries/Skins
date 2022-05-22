package me.nes0x.skins.util;

import me.nes0x.skins.Skins;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

import static me.nes0x.skins.util.ChatUtil.translateText;

public class SkinUtil {

    public static File getPlayerSkinsFile(Skins main, OfflinePlayer target, Player player) {
        File playerSkinsFile = new File(main.getDataFolder(), "/skins/" + target.getUniqueId() + ".yml");
        if (!playerSkinsFile.exists()) {
            try {
                if (!playerSkinsFile.createNewFile()) {
                    player.sendMessage(translateText(main.getConfig().getString("messages.error")));
                    return null;
                }
            } catch (IOException exception) {
                exception.printStackTrace();
                player.sendMessage(translateText(main.getConfig().getString("messages.error")));
                return null;
            }
        }
        return playerSkinsFile;
    }

}
