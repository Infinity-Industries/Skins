package me.nes0x.skins.util;

import org.bukkit.ChatColor;

public class ChatUtil {

    public static String translateText(String text) {
        if (text.isEmpty()) {
            return ChatColor.RED + "Error, you didnt set text for this!";
        }

        return ChatColor.translateAlternateColorCodes('&', text);
    }



}
