package me.gmx.plotmines;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.gmx.plotmines.command.CmdPlotMines;
import me.gmx.plotmines.config.Lang;
import me.gmx.plotmines.config.Settings;
import me.gmx.plotmines.core.BConfig;
import me.gmx.plotmines.handler.PlayerListener;
import me.gmx.plotmines.handler.PlotmineHandler;
import me.gmx.plotmines.handler.WorldListener;
import me.gmx.plotmines.util.ContentUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlotMines extends JavaPlugin {
    Logger logger;
    private static PlotMines ins;
    public File mineFile;
    public FileConfiguration mineConfig;
    public File contentFile;
    public PlotmineHandler handler;
    public FileConfiguration contentConfig;

    public WorldGuardPlugin wgp;
    public BConfig langConfig;
    public BConfig mainConfig;


    public void onEnable(){
        ins = this;
        logger = getLogger();
        if (getServer().getPluginManager().getPlugin("Worldguard") == null){
            getServer().getPluginManager().disablePlugin(this);
        }else{
            this.wgp = WorldGuardPlugin.inst();
        }
        this.langConfig = new BConfig(this,"Lang.yml");
        this.mainConfig = new BConfig(this,"Settings.yml");
        Lang.setConfig(this.langConfig);
        Settings.setConfig(this.mainConfig);
        registerCommands();
        registerListeners();
        initConfig();
        handler = new PlotmineHandler(getInstance());

        for (Player p : Bukkit.getOnlinePlayers()){

        }

    }

    public static PlotMines getInstance(){
        return ins;
    }

    public void reloadConfig() {
        this.langConfig = new BConfig(this,"Lang.yml");
        Lang.setConfig(this.langConfig);
        this.mainConfig = new BConfig(this,"Settings.yml");
        Settings.setConfig(this.mainConfig);


    }

    public FileConfiguration getMineConfig(){
        return this.mineConfig;
    }

    public FileConfiguration getContentConfig(){
        return this.contentConfig;
    }

    public void log(String message){
        logger.log(Level.INFO,"["+getDescription().getName()+"] " + message);
    }

    private void registerCommands(){
        getCommand("plotmine").setExecutor(new CmdPlotMines(getInstance()));
    }
    private void registerListeners(){
        Bukkit.getPluginManager().registerEvents(new WorldListener(),getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerListener(),getInstance());


    }
    public void saveMineConfig(){
        try{
            getMineConfig().save(mineFile);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void saveContentConfig(){
        try{
            getContentConfig().save(contentFile);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void initConfig() {

        try {
            mineFile = new File(getDataFolder(), "Mines.yml");
            if (!mineFile.exists()) {
                mineFile.getParentFile().mkdirs();
                saveResource("Mines.yml", false);
            }
            mineConfig= new YamlConfiguration();

            mineConfig.load(mineFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        try {
            contentFile = new File(getDataFolder(), "Contents.yml");
            if (!contentFile.exists()) {
                contentFile.getParentFile().mkdirs();
                saveResource("Contents.yml", false);
                contentConfig = new YamlConfiguration();
                contentConfig.load(contentFile);
                Set<MaterialData> a = new HashSet<MaterialData>();
                a.add(new MaterialData(Material.DIAMOND_BLOCK,(byte)0));
                ContentUtils.saveToConfig(a,"diamond");
                a.clear();
                a.add(new MaterialData(Material.EMERALD_BLOCK,(byte)0));
                ContentUtils.saveToConfig(a,"emerald");
                saveContentConfig();
                reloadConfig();
                return;
            }
            contentConfig= new YamlConfiguration();

            contentConfig.load(contentFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadMineConfig(){
        try {
            mineConfig.load(new File(getDataFolder(), "Mines.yml"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void reloadContentConfig(){
        try {
            contentConfig.load(new File(getDataFolder(), "Contents.yml"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
