package me.nes0x.skins;

import com.samjakob.spigui.SpiGUI;
import me.nes0x.skins.command.*;
import me.nes0x.skins.listener.ResourcePackListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Skins extends JavaPlugin {
    private static SpiGUI spiGUI;

    @Override
    public void onEnable() {
        new Metrics(this, 15263);
        File userSkins = new File(getDataFolder(), "/skins");
        if (!userSkins.exists()) {
            userSkins.mkdir();
        }
        saveResource("pl-config-infinityskins.yml", true);
        spiGUI = new SpiGUI(this);
        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();
        getCommand("skin").setExecutor(new SkinCommand(this));
        getCommand("skin").setTabCompleter(new SkinTabCompleter(this));
        getCommand("giveskin").setExecutor(new GiveSkinCommand(this));
        getCommand("giveskin").setTabCompleter(new GiveSkinTabCompleter(this));
        getCommand("skins").setExecutor(new SkinsCommand(this));
        getServer().getPluginManager().registerEvents(new ResourcePackListener(this), this);
    }

    public SpiGUI getSpiGUI() {
        return spiGUI;
    }

}
